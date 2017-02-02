/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.spider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.entity.DoubanCommentPK;
import team.beatles.file.Reader;

/**
 * 豆瓣电影短评源代码解析类
 *
 * @author admin Jgirl
 */
public class DoubanCommentSpider {

    private final String mid;
    private final String commentSourceCode;

    /**
     * 默认使用读取文件的方式获取电影短评源代码，使用电影ID构造一个新的DoubanCommentSpider()
     *
     * @param mid 豆瓣电影ID
     */
    public DoubanCommentSpider(String mid) {
        this.mid = mid;
        String filename = this.mid + ".txt";
        Reader rc = new Reader("doc/douban/comment/", filename);
        this.commentSourceCode = rc.read();
    }

    /**
     * 使用电影ID和电影短评源代码构造一个新的DoubanCommentSpider()
     *
     * @param mid 电影ID
     * @param commentSourceCode 电影短评源代码
     */
    public DoubanCommentSpider(String mid, String commentSourceCode) {
        this.mid = mid;
        this.commentSourceCode = commentSourceCode;
    }

    /**
     * 获取某部电影的短评列表
     *
     * @param status 发表短评的用户是否看过电影
     * @return ArrayList 某部电影的短评列表
     */
    public ArrayList<DoubanComment> getComment(String status) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<DoubanComment> commentList = new ArrayList<>();

        Pattern pattern = Pattern.compile("<div class=\"comment-item\" data-cid=\".*?\">.*?<div class=\"avatar\">.*?<a title=\"(.*?)\" href=\"https://www.douban.com/people/(.*?)/\">.*?<img src=\"(.*?)\".*?>.*?</a>.*?</div>.*?<div class=\"comment\">.*?<span class=\"votes pr5\">(.*?)</span>.*?<span class=\"allstar(.*?) rating\" title=\".*?\"></span>.*?<span class=\"comment-time \">(.*?)</span>.*?<p class=\"\">(.*?)</p>.*?</div>");
        Matcher matcher = pattern.matcher(this.commentSourceCode);
        while (matcher.find()) {

            DoubanCommentPK commentPK = new DoubanCommentPK();
            commentPK.setMid(Integer.parseInt(this.mid));
            commentPK.setUid(matcher.group(2).replaceAll("<.*?>", "").trim());
            DoubanComment comment = new DoubanComment();
            comment.setDoubanCommentPK(commentPK);
            comment.setStatus(status);
            comment.setSource(1);
            comment.setAgreement(Integer.parseInt(matcher.group(4)));
            comment.setRating(Double.parseDouble(matcher.group(5).replaceAll("<.*?>", "").trim()));
            try {
                comment.setTime(format.parse(matcher.group(6).replaceAll("<.*?>", "").trim()));
            } catch (ParseException ex) {
                Logger.getLogger(DoubanCommentSpider.class.getName()).log(Level.SEVERE, null, ex);
            }
            comment.setComment(matcher.group(7).replaceAll("<.*?>", ""));
            System.out.println(this.mid + "的评论为：" + comment.getComment());
            commentList.add(comment);
        }
        return commentList;
    }
}
