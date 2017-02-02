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
import team.beatles.file.Reader;
import team.beatles.mtime.collector.MtimeCommentPageCollector;

/**
 * 网页源代码程序下载客户端，即爬行节点
 *
 * @author admin Jgirl
 */
public class SpiderClient {

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
    public SpiderClient(String ip, String port) throws IOException {
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

    public void sendComment(ArrayList<String> midList, String startIndex, String endIndex) {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArrComment = new JSONArray();

        for (String mid : midList) {
            MtimeCommentPageCollector mcpc = new MtimeCommentPageCollector(mid);
            mcpc.start("h", Integer.parseInt(startIndex), Integer.parseInt(endIndex));
            mcpc.start("n", Integer.parseInt(startIndex), Integer.parseInt(endIndex));

            JSONObject jsonObjComment = new JSONObject();

            try {
                Reader rh = new Reader("doc/client/mtime/comment/hot/", mid + ".txt");
                Reader rn = new Reader("doc/client/mtime/comment/new/", mid + ".txt");

                jsonObjComment.put("mid", mid);
                jsonObjComment.put("hot_comment", rh.read());
                jsonObjComment.put("new_comment", rn.read());

                jsonArrComment.put(jsonObjComment);

            } catch (JSONException ex) {
                Logger.getLogger(SpiderClientJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {
            jsonObj.put("###comments###", jsonArrComment);//再将这个json格式的的数组放到最终的json对象中。
        } catch (JSONException ex) {
            Logger.getLogger(SpiderClientJFrame.class.getName()).log(Level.SEVERE, null, ex);
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
     * 客户端关闭与服务器连接
     *
     * @throws IOException
     */
    public void close() throws IOException {
        client.close();//断开客户端与服务器的连接
    }

}
