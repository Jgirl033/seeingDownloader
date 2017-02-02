/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 多线程服务器，用于服务多个客户端
 * @author admin Jgirl
 */
public class MtimeSpiderThreadServer {

    private final int port = 8181;
    private final ServerSocket server;
    private final int POOL_SIZE = 3;
    private final ExecutorService executorService;

    /**
     * 构造一个新的SpiderThreadServer()
     *
     * @throws IOException
     */
    public MtimeSpiderThreadServer() throws IOException {
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
                executorService.execute(new MtimeSpiderServer(socket));//接受一个客户请求,从线程池中拿出一个线程专门处理该客户
            } catch (IOException ex) {
                Logger.getLogger(MtimeSpiderThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 服务器主函数
     * @param arg 主函数命令行参数
     * @throws IOException
     */
    public static void main(String arg[]) throws IOException {
        new MtimeSpiderThreadServer().service();
    }
}
