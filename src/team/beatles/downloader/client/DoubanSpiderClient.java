/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.beatles.douban.collector.DoubanCommentPageCollector;
import team.beatles.douban.collector.DoubanUserPageCollector;
import team.beatles.downloader.file.Reader;

/**
 * 网页源代码程序下载客户端，即爬行节点
 *
 * @author admin Jgirl
 */
public class DoubanSpiderClient {

    Socket client;
    PrintWriter pw;//用来写
    BufferedReader br;//用来读

    /**
     * 使用服务器ip地址以及端口号创建socket连接
     *
     * @param ip 服务器ip地址
     * @param port 服务器服务端口号
     * @throws IOException
     */
    public DoubanSpiderClient(String ip, String port) throws IOException {
        client = new Socket(ip, Integer.parseInt(port));
        //主动向服务器发起连接,实现TCP中三次握手的过程。
        //若不成功(网络问题,地址错误,服务器资源紧张等),抛出错误，其错误信息交由调用者处理。
        //若成功,做下面两件事情。    

        OutputStream socketOut = client.getOutputStream();
        pw = new PrintWriter(new OutputStreamWriter(socketOut, "utf-8"), true);
        //得到网络输出字节流地址,并装饰成网络输出字符流

        InputStream socketIn = client.getInputStream();
        br = new BufferedReader(new InputStreamReader(socketIn, "utf-8"));
        //得到网络输入字节流地址,并装饰成网络输入字符流
    }

    /**
     * 客户端向服务器发送消息
     *
     * @param msg String 消息内容
     */
    public void send(String msg) {
        pw.println(msg);//写入网卡输入流,由系统调用底层函数，经网卡发送给服务器
    }

    /**
     * 发送爬取的用户源代码给服务器
     *
     * @param uidList 用户ID列表
     */
    public void sendUser(ArrayList<String> uidList) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArrUser = new JSONArray();

            DoubanUserPageCollector mupa = new DoubanUserPageCollector();
            mupa.start(uidList);

            for (String uid : uidList) {

                Reader rp = new Reader("doc/client/douban/user/", uid + ".txt");

                try {
                    JSONObject jsonObjUser = new JSONObject();
                    jsonObjUser.put("uid", uid);
                    jsonObjUser.put("information", rp.read());
                    jsonArrUser.put(jsonObjUser);
                } catch (JSONException ex) {
                    Logger.getLogger(DoubanSpiderClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            jsonObj.put("###users###", jsonArrUser);
            pw.println(jsonObj.toString());

        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 发送爬取的电影短评列表给服务器
     *
     * @param midList 电影的时光网ID
     * @param startIndex 短评首页数
     * @param endIndex 短评尾页数
     */
    public void sendComment(ArrayList<String> midList, String startIndex, String endIndex) {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArrComment = new JSONArray();

        for (String mid : midList) {
            DoubanCommentPageCollector mcpc = new DoubanCommentPageCollector(mid);
            mcpc.start("P", Integer.parseInt(startIndex), Integer.parseInt(endIndex));

            JSONObject jsonObjComment = new JSONObject();

            try {
                Reader rh = new Reader("doc/client/douban/comment/", mid + ".txt");

                jsonObjComment.put("mid", mid);
                jsonObjComment.put("comment", rh.read());

                jsonArrComment.put(jsonObjComment);

            } catch (JSONException ex) {
                Logger.getLogger(MtimeSpiderClientJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {
            jsonObj.put("###comments###", jsonArrComment);//再将这个json格式的的数组放到最终的json对象中。
        } catch (JSONException ex) {
            Logger.getLogger(MtimeSpiderClientJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.println(jsonObj.toString());
    }

    /**
     * 客户端接收服务器消息
     *
     * @return String 消息内容
     * @throws IOException
     */
    public String receive() throws IOException {
        String msg = br.readLine();//只接收一行信息
        return msg;
    }

    /**
     * 接收服务器发送过来的新上架的电影ID数据
     *
     * @param midMsg 电影ID数据
     * @return ArrayList 处理电影ID数据，将其转化为列表
     * @throws IOException
     */
    public ArrayList<String> receiveMovie(String midMsg) throws IOException {

        ArrayList<String> midList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(midMsg);
            JSONArray jsonArrMovie = json.getJSONArray("###movies###");
            for (int i = 0; i < jsonArrMovie.length(); i++) {
                midList.add(jsonArrMovie.getJSONObject(i).getString("mid"));
            }
        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return midList;

    }

    /**
     * 接收服务器发送过来的新生成的用户ID数据
     *
     * @param uidMsg 用户ID数据
     * @return ArrayList 处理用户ID数据，将其转化为列表
     * @throws IOException
     */
    public ArrayList<String> receiveUser(String uidMsg) throws IOException {

        ArrayList<String> uidList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(uidMsg);
            JSONArray jsonArrMovie = json.getJSONArray("###users###");
            for (int i = 0; i < jsonArrMovie.length(); i++) {
                uidList.add(jsonArrMovie.getJSONObject(i).getString("uid"));
            }
        } catch (JSONException ex) {
            Logger.getLogger(DoubanSpiderClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uidList;

    }

    /**
     * 客户端关闭与服务器连接
     *
     * @throws IOException
     */
    public void close() throws IOException {
        client.close();//断开客户端与服务器的连接
    }

}
