/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.entity.MtimeMovie;
import team.beatles.mtime.entity.MtimeUser;

/**
 * 数据库查询类
 *
 * @author admin Jgirl
 */
public class MtimeDBSelect {

    private final Connection dbc;

    /**
     * 构造一个新的DBSelect()
     */
    public MtimeDBSelect() {
        this.dbc = new MtimeDBConnect().getDBConnection();
    }

    /**
     * 通过电影名字在数据库中查询，并返回电影信息
     *
     * @param name 电影名字
     * @return Movie 某部电影的基本信息
     */
    public MtimeMovie getMovieByName(String name) {
        MtimeMovie m = new MtimeMovie();
        try {
            String sql = "SELECT * FROM mtime_movie where name = '" + name + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int mid = rs.getInt("mid");
                m.setMid(mid);
                m.setName(name);
                m.setDirector(rs.getString("director"));
                m.setScreenwriter(rs.getString("screenwriter"));
                m.setPerformer("performer");
                m.setCompany(rs.getString("company"));
                m.setArea(rs.getString("area"));
                m.setSynopsis(rs.getString("synopsis"));
                m.setJsonSrc(rs.getString("json_src"));
                m.setBookOffice(rs.getDouble("book_office"));
                m.setRating(rs.getDouble("rating"));
                m.setEvaluationNumber(rs.getInt("evaluation_number"));
            } else {

            }
        } catch (SQLException ex) {
            Logger.getLogger(MtimeDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }

    /**
     * 通过豆瓣用户ID获取用户的名字、性别、地区等信息
     *
     * @param uid 豆瓣用户唯一ID
     * @return User 豆瓣用户基本信息
     */
    public MtimeUser getUserById(String uid) {
        MtimeUser u = new MtimeUser();
        try {
            String sql = "SELECT * FROM mtime_user where uid = '" + uid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setUid(uid);
                u.setName(rs.getString("name"));
                u.setSource(rs.getInt("source"));
                u.setSex(rs.getString("sex"));
                u.setConstellation(rs.getString("constellation"));
                u.setAge(rs.getInt("age"));
                u.setArea(rs.getString("area"));
                u.setEducationJob("education_job");
                u.setTags(rs.getString("tags"));
            } else {

            }
        } catch (SQLException ex) {
            Logger.getLogger(MtimeDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    /**
     * 通过电影名字获取该电影的短评
     *
     * @param name 电影名字
     * @return ArrayList 某部电影的短评列表
     */
    public ArrayList<MtimeComment> getCommentByName(String name) {
        ArrayList<MtimeComment> commentList = new ArrayList<>();
        try {
            MtimeMovie m = this.getMovieByName(name);
            String sql = "SELECT * FROM mtime_comment where mid = " + m.getMid();
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MtimeComment c = new MtimeComment();
                c.setMtimeUser(this.getUserById(rs.getString("uid")));
                c.setMtimeMovie(this.getMovieByName(name));
                c.setSource(rs.getInt("source"));
                c.setStatus(rs.getString("status"));
                c.setRating(rs.getDouble("rating"));
                c.setTime(rs.getDate("time"));
                c.setComment(rs.getString("comment"));
                commentList.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MtimeDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commentList;
    }

}
