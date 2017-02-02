/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.entity.DoubanMovie;
import team.beatles.douban.entity.DoubanUser;

/**
 * 数据库查询类
 *
 * @author admin Jgirl
 */
public class DoubanDBSelect {

    private final Connection dbc;

    /**
     * 构造一个新的DBSelect()
     */
    public DoubanDBSelect() {
        this.dbc = new DoubanDBConnect().getDBConnection();
    }

    /**
     * 通过电影名字在数据库中查询，并返回电影信息
     *
     * @param name 电影名字
     * @return Movie 某部电影的基本信息
     */
    public DoubanMovie getMovieByName(String name) {
        DoubanMovie m = new DoubanMovie();
        try {
            String sql = "SELECT * FROM douban_movie where name = '" + name + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int mid = rs.getInt("mid");
                m.setMid(mid);
                m.setName(name);
                m.setImgSrc(rs.getString("img_src"));
                m.setDirector(rs.getString("director"));
                m.setScreenwriter(rs.getString("screenwriter"));
                m.setPerformer("performer");
                m.setStyle(rs.getString("style"));
                m.setArea(rs.getString("area"));
                m.setLanguage(rs.getString("language"));
                m.setReleaseTime(rs.getString("release_time"));
                m.setRuntime(rs.getString("runtime"));
                m.setSynopsis(rs.getString("synopsis"));
                m.setAward(rs.getString("award"));
                m.setLike(rs.getString("like"));
                m.setJsonSrc(rs.getString("json_src"));
                m.setBookOffice(rs.getDouble("book_office"));
                m.setRating(rs.getDouble("rating"));
                m.setEvaluationNumber(rs.getInt("evaluation_number"));
                m.setOne(rs.getDouble("one"));
                m.setTwo(rs.getDouble("two"));
                m.setThree(rs.getDouble("three"));
                m.setFour(rs.getDouble("four"));
                m.setFive(rs.getDouble("five"));
            } else {

            }
        } catch (SQLException ex) {
            Logger.getLogger(DoubanDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }

    /**
     * 通过电影名字获取该电影的短评
     *
     * @param name 电影名字
     * @return ArrayList 某部电影的短评列表
     */
    public ArrayList<DoubanComment> getCommentByName(String name) {
        ArrayList<DoubanComment> commentList = new ArrayList<>();
        try {
            DoubanMovie m = this.getMovieByName(name);
            String sql = "SELECT * FROM douban_comment where mid = " + m.getMid();
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DoubanComment c = new DoubanComment();
                c.setDoubanUser(this.getUserById(rs.getString("uid")));
                c.setDoubanMovie(this.getMovieByName(name));
                c.setSource(rs.getInt("source"));
                c.setStatus(rs.getString("status"));
                c.setAgreement(rs.getInt("agreement"));
                c.setTime(rs.getDate("time"));
                c.setComment(rs.getString("comment"));
                commentList.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoubanDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commentList;
    }

    /**
     * 通过豆瓣用户ID获取用户的名字、性别、地区等信息
     *
     * @param uid 豆瓣用户唯一ID
     * @return User 豆瓣用户基本信息
     */
    public DoubanUser getUserById(String uid) {
        DoubanUser u = new DoubanUser();
        try {
            String sql = "SELECT * FROM douban_user where uid = '" + uid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setUid(uid);
                u.setName(rs.getString("name"));
                u.setSource(rs.getInt("source"));
                u.setSex(rs.getInt("sex"));
                u.setArea(rs.getString("area"));
                u.setConstellation(rs.getInt("constellation"));
                u.setAge(rs.getInt("age"));
                u.setEducation("education");
                u.setJob(rs.getString("job"));
                System.out.println(u.getName());
                System.out.println("------------------------------------------------------------------");

            } else {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoubanDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }
}
