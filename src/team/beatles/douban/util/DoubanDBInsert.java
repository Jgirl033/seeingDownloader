/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import team.beatles.douban.entity.DoubanComment;
import team.beatles.douban.entity.DoubanMovie;
import team.beatles.douban.entity.DoubanUser;

/**
 * 数据库插入类
 *
 * @author admin Jgirl
 */
public class DoubanDBInsert {

    private final Connection dbc;
    private final String database;

    /**
     * 构造一个新的DBInsert()
     */
    public DoubanDBInsert() {
        this.dbc = new DoubanDBConnect().getDBConnection();
        this.database = "downloader";
    }

    /**
     * 存储电影基本信息到数据库
     *
     * @param m 某部电影的基本信息
     */
    public void saveMovie(DoubanMovie m) {
        //往数据表movie插入数据
        try {
            //执行SQL语句
            String insql;
            insql = "REPLACE INTO  `" + this.database + "`.`douban_movie` (`mid` ,`name` ,`img_src` ,`director` ,`screenwriter` ,`performer` ,`style`,`area` ,`language`,`release_time` ,`runtime` ,`synopsis` ,`award` ,`like` ,`json_src` ,`book_office`,`rating` ,`evaluation_number` ,`one` ,`two` ,`three` ,`four`,`five`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ps = this.dbc.prepareStatement(insql);
            ps.setInt(1, m.getMid());
            ps.setString(2, m.getName());
            ps.setString(3, m.getImgSrc());
            ps.setString(4, m.getDirector());
            ps.setString(5, m.getScreenwriter());
            ps.setString(6, m.getPerformer());
            ps.setString(7, m.getStyle());
            ps.setString(8, m.getArea());
            ps.setString(9, m.getLanguage());
            ps.setString(10, m.getReleaseTime());
            ps.setString(11, m.getRuntime());
            ps.setString(12, m.getSynopsis());
            ps.setString(13, m.getAward());
            ps.setString(14, m.getLike());
            ps.setString(15, m.getJsonSrc());
            ps.setDouble(16, m.getBookOffice());
            ps.setDouble(17, m.getRating());
            ps.setInt(18, m.getEvaluationNumber());
            ps.setDouble(19, m.getOne());
            ps.setDouble(20, m.getTwo());
            ps.setDouble(21, m.getThree());
            ps.setDouble(22, m.getFour());
            ps.setDouble(23, m.getFive());
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("插入movie表成功");
            }
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
            Logger.getLogger(DoubanDBInsert.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.dbc.close();
            } catch (SQLException ex) {
                System.err.print(DoubanDBInsert.class.getName() + "关闭数据库连接出现异常！");
                Logger.getLogger(DoubanDBInsert.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 批量存储电影短评到数据库
     *
     * @param commentList 某部电影的短评列表
     */
    public void saveComment(ArrayList<DoubanComment> commentList) {
        try {
            String insql = "REPLACE INTO  `" + this.database + "`.`douban_comment` (`mid` ,`uid`,`source` ,`status` ,`rating` ,`agreement` ,`time` ,`comment`) VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement prest = this.dbc.prepareStatement(insql);
            System.out.println(commentList);
            DoubanDBCheck dbcc = new DoubanDBCheck();
            for (DoubanComment comment : commentList) {
                if (dbcc.isCommentExist(comment.getDoubanCommentPK().getUid(), String.valueOf(comment.getDoubanCommentPK().getMid()))) {
                    continue;
                }
                System.out.println(comment.getComment());
                prest.setInt(1, comment.getDoubanCommentPK().getMid());
                prest.setString(2, comment.getDoubanCommentPK().getUid());
                prest.setInt(3, 1);
                prest.setString(4, comment.getStatus());
                prest.setDouble(5, comment.getRating());
                prest.setInt(6, comment.getAgreement());
                java.sql.Date sqlDate = new java.sql.Date(comment.getTime().getTime());
                prest.setDate(7, sqlDate);
                prest.setString(8, comment.getComment());
                prest.addBatch();
            }
            prest.executeBatch();
        } catch (SQLException ex) {
            System.out.println("辣鸡，存不进去啊");
            Logger.getLogger(DoubanDBInsert.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.dbc.close();
            } catch (SQLException ex) {
                System.err.print(DoubanDBInsert.class.getName() + "关闭数据库连接出现异常！");
                Logger.getLogger(DoubanDBInsert.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 批量存储发表短评的用户到数据库
     *
     * @param userList 某部电影发表短评的用户列表
     */
    public void saveUser(ArrayList<DoubanUser> userList) {
        try {
            String insql;
            insql = "REPLACE INTO  `" + this.database + "`.`douban_user` (`uid` ,`name` ,`source`,`area`) VALUES (?,?,?,?);";
            PreparedStatement ps = this.dbc.prepareStatement(insql);
            for (DoubanUser user : userList) {
                ps.setString(1, user.getUid());
                ps.setString(2, user.getName());
                ps.setInt(3, user.getSource());
                ps.setString(4, user.getArea());
                System.out.println(user.getArea());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
            Logger.getLogger(DoubanDBInsert.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.dbc.close();
            } catch (SQLException ex) {
                System.err.print(DoubanDBInsert.class.getName() + "关闭数据库连接出现异常！");
                Logger.getLogger(DoubanDBInsert.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
