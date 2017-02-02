/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.collector;

import java.util.logging.Level;
import java.util.logging.Logger;
import team.beatles.downloader.file.Writer;
import static team.beatles.constant.Constant.doubanDR;

/**
 * 豆瓣电影短评源代码下载类
 *
 * @author admin Jgirl
 */
public class DoubanCommentPageCollector {

    private final String mid;

    /**
     * 使用豆瓣电影ID构造一个新的DoubanCommentPageCollector()
     *
     * @param mid 豆瓣电影ID
     */
    public DoubanCommentPageCollector(String mid) {
        this.mid = mid;
    }
    
    

    /**
     * 启动豆瓣电影短评网页源代码下载程序
     *
     * @param status "P"是指发表短评的作者看过电影，"F"是指发表短评的作者想看电影
     * @param start 从哪一条电影短评开始解析
     * @param end 在哪一条电影短评结束解析
     */
    public void start(String status, int start, int end) {

        for (int i = start; i < end; i += 20) {
            //获取评论的网页源代码
            String url = "https://movie.douban.com/subject/" + this.mid + "/comments?start=" + Integer.toString(i) + "&limit=20&sort=new_score&status=" + status;
            doubanDR.get(url); //打开短评页面
            String sourceCode = doubanDR.getPageSource().replaceAll("\n", "");//使用浏览器获取的网页源代码中含有换行符，需要过滤掉;

            //将源代码写入文件
            Writer w = new Writer("doc/client/douban/comment/", this.mid + ".txt");
            w.write(sourceCode, true);

            //进程休眠，防止被反爬虫发现
            java.util.Random random = new java.util.Random();// 定义随机类
            int result = random.nextInt(3000) + 6000;// 返回[0,10)集合中的整数，注意不包括10
            try {
                Thread.sleep(result);
            } catch (InterruptedException ex) {
                Logger.getLogger(DoubanCommentPageCollector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
