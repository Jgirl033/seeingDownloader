/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.spider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.downloader.file.Reader;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.entity.MtimeCommentPK;

/**
 * 电影短评爬取类
 *
 * @author admin Jgirl
 */
public class MtimeCommentSpider {

    private final String mid;
    private final String commentSourceCode;
    private final String status;

    /**
     * 使用电影ID构造一个新的MtimeCommentSpider()
     *
     * @param mid
     * @param status
     */
    public MtimeCommentSpider(String mid, String status) {
        this.mid = mid;
        String filepath;
        String filename = this.mid + ".txt";
        if (status == "n") {
            filepath = "doc/server/mtime/comment/new/";
        } else {
            filepath = "doc/server/mtime/comment/hot/";
        }
        Reader rc = new Reader(filepath, filename);
        this.commentSourceCode = rc.read();
        this.status = status;
    }

    public MtimeCommentSpider(String mid, String status, String commentSourceCode) {
        this.mid = mid;
        this.commentSourceCode = commentSourceCode;
        this.status = status;
    }

    /**
     * 获取某部电影的短评列表
     *
     * @return ArrayList 某部电影的短评列表
     */
    public ArrayList<MtimeComment> getComment() {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<MtimeComment> commentList = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "<h3>(.*?)</h3>.*?<a target=\"_blank\" title=\"(.*?)\" href=\"http://my.mtime.com/(.*?)/\">.*?</a>.*?<p class=\"px14\">.*?<p class=\"mt6 px12 clearfix\">(.*?)<div class=\"mt10\"><a href=\".*?\" target=\"_blank\" entertime=\"(.*?)\"></a></div>");
        Matcher matcher = pattern.matcher(this.commentSourceCode);

        try {
            while (matcher.find()) {
                
                MtimeCommentPK commentPK = new MtimeCommentPK();
                commentPK.setMid(Integer.parseInt(this.mid));
                commentPK.setUid(matcher.group(3).replaceAll("<.*?>", "").trim());
                MtimeComment comment = new MtimeComment();
                comment.setMtimeCommentPK(commentPK);
                comment.setStatus(this.status);
                comment.setSource(3);

                Pattern patternRating = Pattern.compile("<span class=\"db_point ml6\">(.*?)</span>");
                Matcher matcherRating = patternRating.matcher(matcher.group(4));
                if (matcherRating.find()) {
                    comment.setRating(Double.parseDouble(matcherRating.group(1).replaceAll("<.*?>", "").trim()));
                } else {
                    comment.setRating(-1);
                }

                comment.setTime(format.parse(matcher.group(5).replaceAll("<.*?>", "").trim()));
                comment.setComment(matcher.group(1).replaceAll("<.*?>", ""));
                System.out.println(this.mid + "的" + comment.getStatus() + "的评论为：" + comment.getComment());
                commentList.add(comment);
            }
            System.out.println(this.mid + "时光最" + this.status + "爬取完毕");
        } catch (ParseException ex) {
            Logger.getLogger(MtimeCommentSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commentList;
    }
}
