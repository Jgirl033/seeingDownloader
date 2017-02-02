/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接类
 *
 * @author admin Jgirl
 */
class MtimeDBConnect {

    private Connection conn;

    public MtimeDBConnect() {
        try {
            //加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/downloader?zeroDateTimeBehavior=convertToNull", "root", "gdufsiiip");
            System.out.println("连接成功！");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("混蛋 : " + ex.toString());
            System.out.println("连接失败！");
        }
    }

    public MtimeDBConnect(String str, String user, String password) {
        try {
            //加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection(str, user, password);
            System.out.println("连接成功！");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error : " + ex.toString());
            System.out.println("连接失败！");
        }
    }

    public Connection getDBConnection() {
        return this.conn;
    }
}
