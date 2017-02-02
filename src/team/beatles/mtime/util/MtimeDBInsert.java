/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import team.beatles.mtime.entity.MtimeComment;
import team.beatles.mtime.entity.MtimeMovie;
import team.beatles.mtime.entity.MtimeUser;

/**
 * 数据库插入类
 *
 * @author admin Jgirl
 */
public class MtimeDBInsert {

    private final Connection dbc;
    private final String database;

    /**
     * 构造一个新的DBInsert()
     */
    public MtimeDBInsert() {
        this.dbc = new MtimeDBConnect().getDBConnection();
        this.database = "downloader";
    }

    /**
     * 存储电影基本信息到数据库
     *
     * @param m 某部电影的基本信息
     */
    public void saveMovie(MtimeMovie m) {
        //往数据表movie插入数据
        try {
            //执行SQL语句
            String insql;
            insql = "REPLACE INTO  `" + this.database + "`.`mtime_movie` (`mid` ,`name` ,`director` ,`screenwriter` ,`performer` ,`company`,`area` ,`synopsis` ,`json_src` ,`book_office`,`rating` ,`evaluation_number` ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ps = this.dbc.prepareStatement(insql);
            ps.setInt(1, m.getMid());
            ps.setString(2, m.getName());
            ps.setString(3, m.getDirector());
            ps.setString(4, m.getScreenwriter());
            ps.setString(5, m.getPerformer());
            ps.setString(6, m.getCompany());
            ps.setString(7, m.getArea());
            ps.setString(8, m.getSynopsis());
            ps.setString(9, m.getJsonSrc());
            ps.setDouble(10, m.getBookOffice());
            ps.setDouble(11, m.getRating());
            ps.setInt(12, m.getEvaluationNumber());
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("插入movie表成功");
            }
        } catch (Exception ex) {
            System.out.println("辣鸡，存不进去啊");
            System.out.println("Error : " + ex.toString());
        }
    }

    /**
     * 批量存储发表短评的用户到数据库
     *
     * @param userList 某部电影发表短评的用户列表
     */
    public void saveUser(ArrayList<MtimeUser> userList) {
        try {
            String insql;
            insql = "REPLACE INTO  `" + this.database + "`.`mtime_user` (`uid` ,`name` ,`source`,`sex`,`constellation`,`age`,`area`,`education_job`,`tags`,`birthday`) VALUES (?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ps = this.dbc.prepareStatement(insql);
            for (MtimeUser user : userList) {
                //执行SQL语句
                ps.setString(1, user.getUid());
                ps.setString(2, user.getName());
                ps.setInt(3, user.getSource());
                ps.setString(4, user.getSex());
                ps.setString(5, user.getConstellation());
                ps.setInt(6, user.getAge());
                ps.setString(7, user.getArea());
                ps.setString(8, user.getEducationJob());
                ps.setString(9, user.getTags());
                ps.setString(10, user.getBirthday());
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("辣鸡，存不进去啊");
            System.out.println("Error : " + ex.toString());
        }
    }

    /**
     * 批量存储电影短评到数据库
     *
     * @param commentList 某部电影的短评列表
     */
    public void saveComment(ArrayList<MtimeComment> commentList) {
        try {
            String insql = "REPLACE INTO  `" + this.database + "`.`mtime_comment` (`mid` ,`uid`,`source` ,`status` ,`rating` ,`time` ,`comment`) VALUES (?,?,?,?,?,?,?);";
            PreparedStatement prest = this.dbc.prepareStatement(insql);
            MtimeDBCheck dbcc = new MtimeDBCheck();
            for (MtimeComment comment : commentList) {
                if (dbcc.isCommentExist(comment.getMtimeCommentPK().getUid(), String.valueOf(comment.getMtimeCommentPK().getMid()))) {
                    continue;
                }
                prest.setInt(1, comment.getMtimeCommentPK().getMid());
                prest.setString(2, comment.getMtimeCommentPK().getUid());
                prest.setInt(3, 2);
                prest.setString(4, comment.getStatus());
                prest.setDouble(5, comment.getRating());
                java.sql.Date sqlDate = new java.sql.Date(comment.getTime().getTime());
                prest.setDate(6, sqlDate);
                prest.setString(7, comment.getComment());
                prest.addBatch();
            }
            prest.executeBatch();
        } catch (SQLException ex) {
            System.out.println("辣鸡，存不进去啊");
            Logger.getLogger(MtimeDBInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
