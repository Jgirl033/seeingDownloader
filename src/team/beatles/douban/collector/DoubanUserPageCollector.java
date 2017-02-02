/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.collector;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import team.beatles.file.Writer;
import static team.beatles.constant.Constant.doubanDR;

/**
 * 豆瓣电影用户主页源代码下载类
 *
 * @author admin Jgirl
 */
public class DoubanUserPageCollector {

    /**
     * 构造一个新的DoubanUserPageCollector()
     */
    public DoubanUserPageCollector() {

    }

    /**
     * 启动豆瓣用户主页网页源代码下载程序
     *
     * @param uidList 豆瓣用户ID列表
     */
    public void start(ArrayList<String> uidList) {

        for (String uid : uidList) {

            String userUrl = "https://www.douban.com/people/" + uid + "/";
            doubanDR.get(userUrl);
            String sourceCode = doubanDR.getPageSource().replaceAll("\n", "");//使用浏览器获取的网页源代码中含有换行符，需要过滤掉;
            Writer w = new Writer("doc/client/douban/user/", uid + ".txt");
            w.write(sourceCode);

            java.util.Random random = new java.util.Random();// 定义随机类
            int result = random.nextInt(3000) + 6000;// 返回[0,10)集合中的整数，注意不包括10
            try {
                Thread.sleep(result);
            } catch (InterruptedException ex) {
                Logger.getLogger(DoubanUserPageCollector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
