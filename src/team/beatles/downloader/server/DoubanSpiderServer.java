/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.beatles.douban.collector.DoubanMoviePageCollector;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.spider.DoubanCommentSpider;
import team.beatles.douban.util.DoubanDBCheck;
import team.beatles.downloader.file.Writer;
import team.beatles.downloader.web.WebConnect;

/**
 * 获取新上架的电影名称以及对应的豆瓣ID和，即控制节点
 *
 * @author admin Jgirl
 */
public class DoubanSpiderServer implements Runnable {

    private final Socket socket;

    private final PrintWriter pw;//用来写
    private final BufferedReader br;//用来读

    /**
     * 构造一个新的SpiderServer()
     *
     * @param socket 与客户端连接的socket
     * @throws IOException
     */
    public DoubanSpiderServer(Socket socket) throws IOException {

        this.socket = socket;

        OutputStream socketOut = socket.getOutputStream();
        pw = new PrintWriter(new OutputStreamWriter(socketOut, "utf-8"), true);
        //得到网络输出字节流地址,并装饰成网络输出字符流

        InputStream socketIn = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(socketIn, "utf-8"));
        //得到网络输入字节流地址,并装饰成网络输入字符流
    }

    /**
     * 向客户端发送新上架的电影ID列表
     *
     * @param movieIDUnfinishedList 新上架的电影ID列表
     */
    public void sendMovie(List<String> movieIDUnfinishedList) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArrMovie = new JSONArray();

            for (String mid : movieIDUnfinishedList) {
                try {
                    JSONObject jsonObjMovie = new JSONObject();
                    jsonObjMovie.put("mid", mid);
                    jsonArrMovie.put(jsonObjMovie);
                } catch (JSONException ex) {
                    Logger.getLogger(DoubanSpiderServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            jsonObj.put("###movies###", jsonArrMovie);
            pw.println(jsonObj.toString());

        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 向客户端发送新的用户ID列表
     *
     * @param uidList 用户ID列表
     */
    public void sendUser(ArrayList<String> uidList) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArrUser = new JSONArray();

            for (String uid : uidList) {
                try {
                    JSONObject jsonObjUser = new JSONObject();
                    jsonObjUser.put("uid", uid);
                    jsonArrUser.put(jsonObjUser);
                } catch (JSONException ex) {
                    Logger.getLogger(DoubanSpiderServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            jsonObj.put("###users###", jsonArrUser);
            pw.println(jsonObj.toString());

        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 接收客户端发送过来的短评源代码
     *
     * @param commentMsg 短评源代码
     * @return ArrayList 从评论中提取的用户ID列表
     */
    public ArrayList<String> receiveComment(String commentMsg) {

        ArrayList<String> uidList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(commentMsg);
            JSONArray jsonArr = json.getJSONArray("###comments###");

            String filepath = "doc/server/douban/comment/";

            for (int i = 0; i < jsonArr.length(); i++) {
                String mid = jsonArr.getJSONObject(i).getString("mid");
                String commentSourceCode = jsonArr.getJSONObject(i).getString("comment");

                Writer wh = new Writer(filepath, mid + ".txt");
                wh.write(commentSourceCode, true);

                DoubanCommentSpider mcsh = new DoubanCommentSpider(mid, commentSourceCode);

                ArrayList<DoubanComment> commentList = mcsh.getComment("P");

                for (DoubanComment comment : commentList) {
                    String uid = comment.getDoubanCommentPK().getUid();
                    DoubanDBCheck dbc = new DoubanDBCheck();
                    if (!dbc.isUserExist(uid)) {
                        uidList.add(uid);
                    }
                }
            }

        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uidList;
    }

    /**
     * 接收客户端发送过来的用户源代码
     *
     * @param userMsg 短评源代码
     */
    public void receiveUser(String userMsg) {

//        ArrayList<String> uidList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(userMsg);
            JSONArray jsonArr = json.getJSONArray("###users###");

            String filepath = "doc/server/douban/user/";

            for (int i = 0; i < jsonArr.length(); i++) {
                String uid = jsonArr.getJSONObject(i).getString("uid");
                String profile = jsonArr.getJSONObject(i).getString("information");

                Writer wh = new Writer(filepath, uid + ".txt");
                wh.write(profile, true);
            }

        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
//        return uidList;
    }

    /**
     * 使用"电影"关键字在百度上进行搜索，对其返回网页进行解析，获取新上架的电影名称列表
     *
     * @return List 新上架的电影名称列表
     */
    public static List<String> getMovieName() {
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?resource_id=6862&from_mid=1&&format=json&ie=utf-8&oe=utf-8&query=电影&sort_key=17&sort_type=1&stat0=&stat1=&stat2=&stat3=&pn=0&rn=8";;
        List<String> movieUnfinishedList = new ArrayList<>();
        WebConnect conn = new WebConnect(url);
        String sourceCode = conn.downloadPage().getSourceCode();
        Pattern pattern = Pattern.compile("\"ename\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(sourceCode);
        while (matcher.find()) {
            movieUnfinishedList.add(matcher.group(1).trim());
        }
        return movieUnfinishedList;
    }

    /**
     * 将新上架的电影名称在豆瓣网上遍历检索，获取其对应的ID
     *
     * @return List 新上架的电影的时光网ID列表
     */
    public static List<String> getMovieID() {
        List<String> movieIDUnfinishedList = new ArrayList<>();
        List<String> movieUnfinishedList = getMovieName();
        for (String movie : movieUnfinishedList) {
            StringBuilder string = new StringBuilder();
            String[] hex = movie.split("\\\\u");
            for (int i = 1; i < hex.length; i++) {
                // 转换出每一个代码点
                int data = Integer.parseInt(hex[i], 16);
                // 追加成string
                string.append((char) data);
            }

            String url = "https://movie.douban.com/subject_search?search_text=" + string + "&cat=1002";
            Pattern pattern = Pattern.compile("<a class=\"nbg\" href=\".*?\" onclick=\".*?subject_id:'(.*?)'.*?\" title=\".*?\">.*?<img src=\".*?\" alt=\".*?\" class=\"\"/>.*?</a>");
            WebConnect conn = new WebConnect(url);
            Matcher matcher = pattern.matcher(conn.downloadPage().getSourceCode());
            DoubanDBCheck dbc = new DoubanDBCheck();
            if (matcher.find() && !dbc.isMovieExist(matcher.group(1).trim())) {
                movieIDUnfinishedList.add(matcher.group(1).trim());
            }
        }
        return movieIDUnfinishedList;
    }

    @Override
    public void run() {
        
        List<String> movieIDUnfinishedList = DoubanSpiderServer.getMovieID();
        for (String mid : movieIDUnfinishedList) {
            DoubanMoviePageCollector dmpc = new DoubanMoviePageCollector(mid);
            dmpc.start();
        }
        
        try {
            String msg = null;
            while ((msg = br.readLine()) != null) {
                if (msg.contains("###Hello###".subSequence(0, 10))) {
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "注册上线！");
                    pw.println("success");
                } else if (msg.contains("###Bye###".subSequence(0, 8))) {
                    //爬行节点请求退出
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "退出成功！");
                    socket.close();
                    break;
                } else if (msg.contains("###movies###".subSequence(0, 11))) {
                    //控制节点向爬行节点发送电影ID，以movies为信号量
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "请求新上架的电影ID！");
                    this.sendMovie(movieIDUnfinishedList);
                } else if (msg.contains("###comments###".subSequence(0, 13))) {
                    //控制节点向爬行节点发送电影短评，以comments为信号量
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "传来电影短评的源代码！");
                    ArrayList<String> uidList = this.receiveComment(msg);
                    this.sendUser(uidList);
                } else if (msg.contains("###users###".subSequence(0, 10))) {
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "传来新用户的源代码！");
                    this.receiveUser(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("Douban服务器出现数据传输异常：" + e.toString());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Mtime服务器出现套接字异常：" + e.toString());

            }
        }
    }
}
