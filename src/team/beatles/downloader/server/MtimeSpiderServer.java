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
import team.beatles.downloader.file.Writer;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.spider.MtimeCommentSpider;
import team.beatles.mtime.util.MtimeDBCheck;
import team.beatles.downloader.web.WebConnect;

/**
 * 获取新上架的电影名称以及对应的豆瓣ID和时光网ID，即控制节点
 *
 * @author admin Jgirl
 */
public class SpiderServer implements Runnable {

    private final Socket socket;

    private final PrintWriter pw;//用来写
    private final BufferedReader br;//用来读

    /**
     * 构造一个新的SpiderServer()
     *
     * @param socket 与客户端连接的socket
     * @throws IOException
     */
    public SpiderServer(Socket socket) throws IOException {

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
                    Logger.getLogger(SpiderServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            jsonObj.put("###movies###", jsonArrMovie);
            pw.println(jsonObj.toString());

        } catch (JSONException ex) {
            Logger.getLogger(SpiderServer.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(SpiderServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            jsonObj.put("###users###", jsonArrUser);
            pw.println(jsonObj.toString());

        } catch (JSONException ex) {
            Logger.getLogger(SpiderServer.class.getName()).log(Level.SEVERE, null, ex);
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

            String filepathHot = "doc/server/mtime/comment/hot/";
            String filepathNew = "doc/server/mtime/comment/new/";

            for (int i = 0; i < jsonArr.length(); i++) {
                String mid = jsonArr.getJSONObject(i).getString("mid");
                String hotComment = jsonArr.getJSONObject(i).getString("hot_comment");
                String newComment = jsonArr.getJSONObject(i).getString("new_comment");

                Writer wh = new Writer(filepathHot, mid + ".txt");
                wh.write(hotComment, true);

                Writer wn = new Writer(filepathNew, mid + ".txt");
                wn.write(newComment, true);

                MtimeCommentSpider mcsh = new MtimeCommentSpider(mid, "h", hotComment);
                MtimeCommentSpider mcsn = new MtimeCommentSpider(mid, "n", newComment);

                ArrayList<MtimeComment> hotCommentList = mcsh.getComment();
                ArrayList<MtimeComment> newCommentList = mcsn.getComment();
                ArrayList<MtimeComment> commentList = new ArrayList<>();
                commentList.addAll(newCommentList);
                commentList.addAll(hotCommentList);

                MtimeDBCheck dbc = new MtimeDBCheck();
                for (MtimeComment comment : commentList) {
                    String uid = comment.getMtimeCommentPK().getUid();
                    if (!dbc.isUserExist(uid)) {
                        uidList.add(uid);
                    }
                }
            }

        } catch (JSONException ex) {
            Logger.getLogger(SpiderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uidList;
    }

    /**
     * 接收客户端发送过来的用户源代码
     *
     * @param userMsg 短评源代码
     * @return ArrayList 从评论中提取的用户ID列表
     */
    public ArrayList<String> receiveUser(String userMsg) {

        ArrayList<String> uidList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(userMsg);
            JSONArray jsonArr = json.getJSONArray("###users###");

            String filepathProfile = "doc/server/mtime/user/profile/";
            String filepathComment = "doc/server/mtime/user/comment/";

            for (int i = 0; i < jsonArr.length(); i++) {
                String uid = jsonArr.getJSONObject(i).getString("uid");
                String profile = jsonArr.getJSONObject(i).getString("profile");
                String comment = jsonArr.getJSONObject(i).getString("comment");

                Writer wh = new Writer(filepathProfile, uid + ".txt");
                wh.write(profile, true);

                Writer wn = new Writer(filepathComment, uid + ".txt");
                wn.write(comment, true);
            }

        } catch (JSONException ex) {
            Logger.getLogger(SpiderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uidList;
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
     * 将新上架的电影名称在时光网上遍历检索，获取其对应的ID
     *
     * @return List 新上架的电影的时光网ID列表
     */
    public static List<String> getMtimeMovieID() {
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

            String url = "http://service.channel.mtime.com/Search.api?Ajax_CallBack=true&Ajax_CallBackType=Mtime.Channel.Services&Ajax_CallBackMethod=GetSearchResult&Ajax_CrossDomain=1&Ajax_RequestUrl=http://search.mtime.com/search/?q=" + string + "&t=0&Ajax_CallBackArgument0=" + string + "&Ajax_CallBackArgument1=0&Ajax_CallBackArgument2=290&Ajax_CallBackArgument3=0&Ajax_CallBackArgument4=1";
            Pattern pattern = Pattern.compile("\"movieId\":(.*?),\"movieTitle\":");
            WebConnect conn = new WebConnect(url);
            Matcher matcher = pattern.matcher(conn.downloadPage().getSourceCode());
            MtimeDBCheck dbc = new MtimeDBCheck();
            if (matcher.find() && !dbc.isMovieExist(matcher.group(1).trim())) {
                movieIDUnfinishedList.add(matcher.group(1).trim());
            }
        }
        return movieIDUnfinishedList;
    }

    @Override
    public void run() {
        List<String> movieIDUnfinishedList = SpiderServer.getMtimeMovieID();
        try {
            String msg = null;
            while ((msg = br.readLine()) != null) {
                if (msg.contains("Hello".subSequence(0, 4))) {
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
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
