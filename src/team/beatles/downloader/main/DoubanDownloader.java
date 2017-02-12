/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import team.beatles.douban.collector.DoubanCommentPageCollector;
import team.beatles.douban.collector.DoubanMoviePageCollector;
import team.beatles.douban.collector.DoubanUserPageCollector;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.spider.DoubanCommentSpider;
import team.beatles.douban.spider.DoubanLogin;
import static team.beatles.constant.Constant.doubanDR;
import team.beatles.downloader.server.DoubanSpiderServer;

/**
 *
 * @author admin
 */
public class DoubanDownloader {

    public static void main(String[] args) throws IOException {

        List<String> movieIDUnfinishedList = DoubanSpiderServer.getMovieID();

        doubanDR = DoubanLogin.getWebDriver();
        for (String mid : movieIDUnfinishedList) {
            //1.爬取电影基本信息源代码
            DoubanMoviePageCollector dmpc = new DoubanMoviePageCollector(mid);
            dmpc.start();

            //2.爬取用户评论源代码并解析
            DoubanCommentPageCollector dcpa = new DoubanCommentPageCollector(mid);
            dcpa.start("P", 1, 2);
            DoubanCommentSpider dcs = new DoubanCommentSpider(mid);
            ArrayList<DoubanComment> commentList = dcs.getComment("P");
            HashSet<DoubanComment> set = new HashSet<>(commentList);
            ArrayList<DoubanComment> commentSet = new ArrayList<>(set);

            //3.从评论中获取用户ID            
            ArrayList<String> uidList = new ArrayList<>();
            for (DoubanComment comment : commentSet) {
                String uid = comment.getDoubanCommentPK().getUid();
                uidList.add(uid);
            }
            
            //4.爬取用户主页源代码
            DoubanUserPageCollector dupc = new DoubanUserPageCollector();
            dupc.start(uidList);
        }

    }
}
