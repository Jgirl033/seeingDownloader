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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.douban.collector.DoubanCommentPageCollector;
import team.beatles.douban.collector.DoubanMoviePageCollector;
import team.beatles.douban.collector.DoubanUserPageCollector;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.spider.DoubanCommentSpider;
import team.beatles.douban.spider.DoubanLogin;
import static team.beatles.constant.Constant.doubanDR;
import team.beatles.douban.entity.DoubanCollect;
import team.beatles.douban.entity.DoubanWish;
import team.beatles.douban.spider.DoubanUserSpider;
import team.beatles.downloader.file.Writer;
import team.beatles.downloader.web.WebConnect;
import team.beatles.downloader.web.WebPage;

/**
 *
 * @author admin
 */
public class DoubanDownloader {

    public static void downloadUserMovie(ArrayList<String> uidList) {
        doubanDR = DoubanLogin.getWebDriver();

        for (String uid : uidList) {

            DoubanUserSpider doubanUserSpider = new DoubanUserSpider(uid);
            ArrayList<String> midUrlList = new ArrayList<>();
            ArrayList<DoubanCollect> collectMovieList = doubanUserSpider.getCollectMovie();
            for (DoubanCollect dbc : collectMovieList) {
                if (dbc.getOneUrl() != null) {
                    midUrlList.add(dbc.getOneUrl());
                }
                if (dbc.getTwoUrl() != null) {
                    midUrlList.add(dbc.getTwoUrl());
                }
                if (dbc.getThreeUrl() != null) {
                    midUrlList.add(dbc.getThreeUrl());
                }
                if (dbc.getFourUrl() != null) {
                    midUrlList.add(dbc.getFourUrl());
                }
                if (dbc.getFiveUrl() != null) {
                    midUrlList.add(dbc.getFiveUrl());
                }
            }

            ArrayList<DoubanWish> wishMovieList = doubanUserSpider.getWishMovie();
            for (DoubanWish dbw : wishMovieList) {
                if (dbw.getOneUrl() != null) {
                    midUrlList.add(dbw.getOneUrl());
                }
                if (dbw.getTwoUrl() != null) {
                    midUrlList.add(dbw.getTwoUrl());
                }
                if (dbw.getThreeUrl() != null) {
                    midUrlList.add(dbw.getThreeUrl());
                }
                if (dbw.getFourUrl() != null) {
                    midUrlList.add(dbw.getFourUrl());
                }
                if (dbw.getFiveUrl() != null) {
                    midUrlList.add(dbw.getFiveUrl());
                }
            }
            for (String midUrl : midUrlList) {
                System.out.println(midUrl);
                String mid = "";
                doubanDR.get(midUrl);

                Pattern pattern = Pattern.compile("https://movie.douban.com/subject/(.*?)/");
                Matcher matcher = pattern.matcher(midUrl);
                if (matcher.find()) {
                    mid = matcher.group(1).trim();
                }
                String filename = mid + ".txt";
                Writer w = new Writer("doc/server/douban/movie/", filename);
                w.write(doubanDR.getPageSource());
               
                java.util.Random random = new java.util.Random();// 定义随机类
                int result = random.nextInt(3000) + 6000;// 返回[0,10)集合中的整数，注意不包括10
                try {
                    Thread.sleep(result);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DoubanDownloader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

//        List<String> movieIDUnfinishedList = DoubanSpiderServer.getMovieID();
        String[] movieIDUnfinishedList = {"26815856", "26766397", "26745334", "26711487", "26691509", "26425081", "25934014", "3230115"};

        for (String mid : movieIDUnfinishedList) {
            //1.爬取电影基本信息源代码
            DoubanMoviePageCollector dmpc = new DoubanMoviePageCollector(mid);
            dmpc.start();

            //2.爬取用户评论源代码并解析
//            DoubanCommentPageCollector dcpa = new DoubanCommentPageCollector(mid);
//            dcpa.start("P", 1, 2);
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

            DoubanDownloader.downloadUserMovie(uidList);

//            //4.爬取用户主页源代码
//            DoubanUserPageCollector dupc = new DoubanUserPageCollector();
//            dupc.start(uidList);
        }

    }
}
