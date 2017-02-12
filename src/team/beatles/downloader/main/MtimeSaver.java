/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.main;

import java.util.ArrayList;
import team.beatles.mtime.collector.MtimeMoviePageCollector;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.entity.MtimeMovie;
import team.beatles.mtime.entity.MtimeUser;
import team.beatles.mtime.spider.MtimeCommentSpider;
import team.beatles.mtime.spider.MtimeMovieSpider;
import team.beatles.mtime.spider.MtimeUserSpider;
import team.beatles.mtime.util.MtimeDBInsert;

/**
 *
 * @author admin
 */
public class MtimeSaver {

    public static void main(String[] args) {
        String[] midList = {"208325","225095","230769","231591","233227","234178","235457","237054"};

        for (String mid : midList) {
            
            MtimeMoviePageCollector mmc = new MtimeMoviePageCollector(mid);
            mmc.start();
            
            MtimeMovieSpider movieSpider = new MtimeMovieSpider(mid);
            MtimeMovie movie = movieSpider.getMovie();

            MtimeDBInsert dbi = new MtimeDBInsert();
            dbi.saveMovie(movie);

            MtimeCommentSpider mcsh = new MtimeCommentSpider(mid, "h");
            MtimeCommentSpider mcsn = new MtimeCommentSpider(mid, "n");

            ArrayList<MtimeComment> hotCommentList = mcsh.getComment();
            ArrayList<MtimeComment> newCommentList = mcsn.getComment();
            ArrayList<MtimeComment> commentList = new ArrayList<>();
            commentList.addAll(newCommentList);
            commentList.addAll(hotCommentList);

            ArrayList<MtimeUser> userList = new ArrayList<>();
            for (MtimeComment comment : commentList) {
                String uid = comment.getMtimeCommentPK().getUid();
                MtimeUserSpider userSpider = new MtimeUserSpider(uid);
                MtimeUser user = userSpider.getUser();
                userList.add(user);
            }

            dbi.saveUser(userList);
            dbi.saveComment(commentList);
        }

    }
}
