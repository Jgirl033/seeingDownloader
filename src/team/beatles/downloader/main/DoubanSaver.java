/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.entity.DoubanMovie;
import team.beatles.douban.entity.DoubanUser;
import team.beatles.douban.spider.DoubanCommentSpider;
import team.beatles.douban.spider.DoubanMovieSpider;
import team.beatles.douban.spider.DoubanUserSpider;
import team.beatles.douban.util.DoubanDBCheck;
import team.beatles.douban.util.DoubanDBInsert;
import team.beatles.downloader.server.DoubanSpiderServer;

/**
 *
 * @author admin
 */
public class DoubanSaver {

    public static void main(String[] args) throws IOException {

        List<String> movieIDUnfinishedList = DoubanSpiderServer.getMovieID();
        for (String mid : movieIDUnfinishedList) {
            //1.解析电影基本信息源代码，获取信息
            DoubanMovieSpider dms = new DoubanMovieSpider(mid);
            DoubanMovie m = dms.getMovie();
            System.out.println("1.电影的名字：" + m.getName());
            System.out.println("2.电影的剧情简介：" + m.getSynopsis());
            System.out.println("3.电影的获奖情况：" + m.getAward());
            System.out.println("4.电影的评分：" + m.getRating());

            //2.存储电影信息到数据库
            DoubanDBInsert dbi = new DoubanDBInsert();
            dbi.saveMovie(m);

            //3.解析用户评论源代码获取用户ID
            DoubanCommentSpider dcs = new DoubanCommentSpider(mid);
            ArrayList<DoubanComment> commentList = dcs.getComment("P");

            ArrayList<String> uidList = new ArrayList<>();
            for (DoubanComment comment : commentList) {
                String uid = comment.getDoubanCommentPK().getUid();
                uidList.add(uid);
            }

            //5.解析用户源代码获取信息
            ArrayList<DoubanUser> doubanUserList = new ArrayList<>();
            for (String uid : uidList) {
                System.out.print(uid);
                DoubanUserSpider dup = new DoubanUserSpider(uid);
                DoubanUser doubanUser = new DoubanUser(uid);
                doubanUser.setName(dup.getName());
                doubanUser.setArea(dup.getArea());
                doubanUser.setSource(1);
                doubanUserList.add(doubanUser);
            }

            //6.存储用户和短评
            dbi.saveUser(doubanUserList);
            dbi.saveComment(commentList);

        }
    }
}
