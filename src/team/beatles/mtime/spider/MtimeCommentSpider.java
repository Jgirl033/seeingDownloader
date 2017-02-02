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
import team.beatles.file.Reader;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.entity.MtimeCommentPK;

/**
 * 电影短评爬取类
 *
 * @author admin Jgirl
 */
public class MtimeCommentSpider {

    private final String mid;

    /**
     * 使用电影ID构造一个新的MtimeCommentSpider()
     *
     * @param mid
     */
    public MtimeCommentSpider(String mid) {
        this.mid = mid;
    }

    /**
     * 获取某部电影的短评列表
     *
     * @param status "h"表示最热短评，"n"表示最新短评
     * @return ArrayList 某部电影的短评列表
     */
    public ArrayList<MtimeComment> getComment(String status) {

        String filepath;
        String filename = this.mid + ".txt";
        if (status == "n") {
            filepath = "doc/server/mtime/comment/new/";
        } else {
            filepath = "doc/server/mtime/comment/hot/";
        }
        Reader rc = new Reader(filepath, filename);
        String commentSourceCode = rc.read();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<MtimeComment> commentList = new ArrayList<>();

        Pattern pattern = Pattern.compile("<h3>(.*?)</h3>.*?<a target=\"_blank\" title=\"(.*?)\" href=\"http://my.mtime.com/(.*?)/\">.*?</a>.*?<p class=\"px14\">.*?<p class=\"mt6 px12 clearfix\">.*?<span class=\"db_point ml6\">(.*?)</span></p>.*? <div class=\"mt10\"><a href=\".*?\" target=\"_blank\" entertime=\"(.*?)\"></a></div> ");
        Matcher matcher = pattern.matcher(commentSourceCode);
        while (matcher.find()) {

            MtimeCommentPK commentPK = new MtimeCommentPK();
            commentPK.setMid(Integer.parseInt(this.mid));
            commentPK.setUid(matcher.group(3).replaceAll("<.*?>", "").trim());
            MtimeComment comment = new MtimeComment();
            comment.setMtimeCommentPK(commentPK);
            comment.setStatus(status);
            comment.setSource(3);
            comment.setRating(Double.parseDouble(matcher.group(4).replaceAll("<.*?>", "").trim()));
            try {
                comment.setTime(format.parse(matcher.group(5).replaceAll("<.*?>", "").trim()));
            } catch (ParseException ex) {
                Logger.getLogger(MtimeCommentSpider.class.getName()).log(Level.SEVERE, null, ex);
            }
            comment.setComment(matcher.group(1).replaceAll("<.*?>", ""));
            System.out.println(this.mid + "的" + comment.getStatus() + "的评论为：" + comment.getComment());
            commentList.add(comment);
        }
        return commentList;
    }
}
