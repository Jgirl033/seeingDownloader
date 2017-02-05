/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.main;

import java.util.ArrayList;
import java.util.List;
import team.beatles.mtime.collector.MtimeCommentPageCollector;
import team.beatles.mtime.collector.MtimeMoviePageCollector;
import team.beatles.mtime.collector.MtimeUserPageCollector;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.spider.MtimeCommentSpider;
import static team.beatles.constant.Constant.mtimeDR;
import team.beatles.downloader.server.MtimeSpiderServer;
import team.beatles.mtime.spider.MtimeLogin;

/**
 *
 * @author admin
 */
public class MtimeDownloader {

    public static void main(String[] args) {
        mtimeDR = MtimeLogin.getWebDriver();

        List<String> movieIDUnfinishedList = MtimeSpiderServer.getMovieID();

        for (String mid : movieIDUnfinishedList) {
            MtimeMoviePageCollector mmc = new MtimeMoviePageCollector(mid);
            mmc.start();

            MtimeCommentPageCollector mcpc = new MtimeCommentPageCollector(mid);
            mcpc.start("h", 1, 11);
            mcpc.start("n", 1, 11);

            MtimeCommentSpider mcsh = new MtimeCommentSpider(mid, "h");
            MtimeCommentSpider mcsn = new MtimeCommentSpider(mid, "n");

            ArrayList<MtimeComment> hotCommentList = mcsh.getComment();
            ArrayList<MtimeComment> newCommentList = mcsn.getComment();
            ArrayList<MtimeComment> commentList = new ArrayList<>();
            commentList.addAll(newCommentList);
            commentList.addAll(hotCommentList);

            //3.从评论中获取用户ID
            ArrayList<String> uidList = new ArrayList<>();
            for (MtimeComment comment : commentList) {
                uidList.add(comment.getMtimeCommentPK().getUid());
            }

            //4.爬取用户源代码主页
            MtimeUserPageCollector mupa = new MtimeUserPageCollector();
            mupa.start(uidList);
        }

    }
}
