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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 判断数据是否存在于数据库
 *
 * @author admin Jgirl
 */
public class DoubanDBCheck {

    private final Connection dbc;

    /**
     * 构造一个新的DBCheck()
     */
    public DoubanDBCheck() {
        this.dbc = new DoubanDBConnect().getDBConnection();
    }

    /**
     * 使用豆瓣用户ID判断用户是否已在数据库中
     *
     * @param uid 豆瓣用户唯一ID
     * @return boolean 表示用户数据是否存在于数据库中
     */
    public boolean isUserExist(String uid) {
        boolean result = false;
        try {
            String sql = "SELECT * FROM douban_user where uid = '" + uid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(DoubanDBCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     *
     * @param uid
     * @param mid
     * @return
     */
    public boolean isCommentExist(String uid, String mid) {
        boolean result = false;
        try {
            String sql = "SELECT * FROM douban_comment where uid = '" + uid + "' and mid='" + mid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(DoubanDBCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     *
     * @param mid
     * @return
     */
    public boolean isMovieExist(String mid) {
        boolean result = false;
        try {
            String sql = "SELECT * FROM douban_movie where mid = '" + mid + "'";
            PreparedStatement ps = this.dbc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(DoubanDBCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
