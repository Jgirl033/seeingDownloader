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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 判断数据是否存在于数据库
 *
 * @author admin Jgirl
 */
public class MtimeDBCheck {

    private final Connection dbc;

    /**
     * 构造一个新的DBCheck()
     */
    public MtimeDBCheck() {
        this.dbc = new MtimeDBConnect().getDBConnection();
    }

    /**
     * 使用时光网电影ID判断电影是否已在数据库中
     *
     * @param mid 时光网电影ID
     * @return boolean "true"表示电影已存入数据库中，"false"表示电影未存入数据库中
     */
    public boolean isMovieExist(String mid) {
        boolean result = false;
        try {
            String sql = "SELECT * FROM mtime_movie where mid = '" + mid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(MtimeDBCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * 使用时光网用户ID判断用户是否已在数据库中
     *
     * @param uid 时光网用户唯一ID
     * @return boolean "true"表示用户数据已存入数据库中，"false"表示用户数据未存入数据库中
     */
    public boolean isUserExist(String uid) {
        boolean result = false;
        try {
            String sql = "SELECT * FROM douban_user where uid = '" + uid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(MtimeDBCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * 使用时光网用户ID以及电影ID判断评论是否已在数据库中
     *
     * @param uid 时光网用户ID
     * @param mid 时光网电影ID
     * @return boolean "true"表示评论已存入数据库中，"false"表示评论未存入数据库中
     */
    public boolean isCommentExist(String uid, String mid) {
        boolean result = false;
        try {
            String sql = "SELECT * FROM douban_comment where uid = '" + uid + "' and mid='" + mid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(MtimeDBCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
