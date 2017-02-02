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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.beatles.file.Writer;

/**
 *
 * @author admin
 */
public class SpiderThreadServer {

    private final int port = 8181;
    private final ServerSocket server;
    private final int POOL_SIZE = 3;
    private final ExecutorService executorService;

    /**
     *
     * @throws IOException
     */
    public SpiderThreadServer() throws IOException {
        server = new ServerSocket(this.port);//开启8181端口，初始化服务器,服务器端只需要端口参数      
        //创建线程池
        //Runtime的availableProcessors()方法返回当前系统的CPU的数目
        //系统的CPU越多，线程池中工作线程的数目也越多
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        System.out.println("服务器开启成功！");
    }

    /**
     * 服务器等待接收客户端请求连接
     */
    public void service() {
        Socket socket = null;

        while (true) {
            try {
                //服务器处于长期开启状态
                socket = server.accept();//等待客户端请求连接，如果有连接即生成一个套接字
                executorService.execute(new Handler(socket));//接受一个客户请求,从线程池中拿出一个线程专门处理该客户
            } catch (IOException ex) {
                Logger.getLogger(SpiderThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String arg[]) throws IOException {
        new SpiderThreadServer().service();
    }
}

class Handler implements Runnable {     //定义线程类，处理每个服务器与各个客户端连接的具体事务

    private final Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(new OutputStreamWriter(socketOut, "utf-8"), true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn, "utf-8"));
    }

    @Override
    public void run() {
        List<String> movieIDUnfinishedList = SpiderServer.getMtimeMovieID();
        try {
            BufferedReader br = getReader(socket);
            PrintWriter pw = getWriter(socket);
            String msg = null;
            while ((msg = br.readLine()) != null) {
                if (msg.contains("Hello".subSequence(0, 4))) {
                    pw.println("success");
                } else if (msg.contains("###Bye###".subSequence(0, 8))) {
                    //爬行节点请求退出
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "退出成功！");
                    socket.close();
                    break;
                } else if (msg.contains("###movies###".subSequence(0, 11))) {
                    //控制节点向爬行节点发送电影ID，以movies为信号量
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "注册上线！");
                    
                    JSONObject jsonObj = new JSONObject();
                    JSONArray jsonArrMovie = new JSONArray();

                    for (String mid : movieIDUnfinishedList) {
                        JSONObject jsonObjMovie = new JSONObject();
                        jsonObjMovie.put("mid", mid);
                        jsonArrMovie.put(jsonObjMovie);
                    }
                    jsonObj.put("###movies###", jsonArrMovie);
                    pw.println(jsonObj.toString());
                    System.out.println(jsonObj.toString());
                    
                } else if (msg.contains("###comments###".subSequence(0, 10))) {
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "传来电影短评的源代码！");

                    JSONObject json = new JSONObject(msg);
                    JSONArray jsonArr = json.getJSONArray("###comments###");

                    String filepathHot = "doc/server/mtime/comment/hot/";
                    String filepathNew = "doc/server/mtime/comment/new/";

                    for (int i = 0; i < jsonArr.length(); i++) {
                        Writer wh = new Writer(filepathHot, jsonArr.getJSONObject(i).getString("mid") + ".txt");
                        wh.write(jsonArr.getJSONObject(i).getString("hot_comment"), true);

                        Writer wn = new Writer(filepathNew, jsonArr.getJSONObject(i).getString("mid") + ".txt");
                        wn.write(jsonArr.getJSONObject(i).getString("new_comment"), true);
                    }
                }

            }
        } catch (IOException e) {
        } catch (JSONException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
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
