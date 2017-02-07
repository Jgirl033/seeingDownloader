/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 网络连接类
 *
 * @author admin Jgirl
 */
public class WebConnect {

    private String url;
    private String decodeType;

    /**
     * 构造一个新的WebConnection()
     *
     * @param url
     */
    public WebConnect(String url) {
        this.url = url;
        this.decodeType = "utf-8";
    }

    /**
     * 通过url、网页编码构造一个新的WebConnection()
     *
     * @param url
     * @param decodeType
     */
    public WebConnect(String url, String decodeType) {
        this.url = url;
        this.decodeType = decodeType;
    }

    /**
     * 获取网页源代码
     *
     * @return WebPage 网页源代码以及是否爬取成功的信息
     */
    public WebPage downloadPage() {

        String webpage = "";
        BufferedReader in = null;
        WebPage page = new WebPage();
        System.out.println("爬取的链接为" + this.url);
        try {
            URL realURL = new URL(this.url);
            URLConnection conn = realURL.openConnection();
            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), this.decodeType));
            String line;
            while ((line = in.readLine()) != null) {
                webpage = webpage + line;
            }
            if (webpage.length() == 0) {
                page.setSuccess(false);
                System.out.println(this.url + "的源代码大小是" + webpage.length());
            } else {
                page.setSuccess(true);
                System.out.println(this.url + "的源代码大小是" + webpage.length());
            }
            page.setSourceCode(webpage);
        } catch (Exception e) {
            System.out.println(WebConnect.class.getName() + "爬取以下链接出错" + this.url);
            System.err.println(e.toString());
        } finally {
            return page;
        }
    }

}
