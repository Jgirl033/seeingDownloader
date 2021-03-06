/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.collector;

import java.util.ArrayList;
import team.beatles.downloader.file.Writer;
import team.beatles.downloader.web.WebConnect;

/**
 * 时光网电影短评页面源代码下载类
 * @author admin Jgirl
 */
public class MtimeCommentPageCollector {

    private final String mid;

    /**
     * 使用电影ID构造一个新的MtimeCommentPageCollector()
     * @param mid 电影ID
     */
    public MtimeCommentPageCollector(String mid) {
        this.mid = mid;
    }

    /**
     * 启动时光网电影短评源代码下载程序，下载内容包括最新短评以及最热短评
     * @param status "h"表示最热短评，"n"表示最新短评
     * @param start 短评首页数
     * @param end 短评尾页数
     * @return ArrayList 某部电影的短评页面源代码列表
     */
    public ArrayList start(String status, int start, int end) {

        String filepath;
        ArrayList<String> commentSourceCodeList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            //获取评论的网页源代码
            String url;
            if (status == "n") {
                filepath = "doc/client/mtime/comment/new/";
                if (i == 1) {
                    url = "http://movie.mtime.com/" + this.mid + "/reviews/short/new.html";
                } else {
                    url = "http://movie.mtime.com/" + this.mid + "/reviews/short/new-" + i + ".html";
                }
            } else {
                filepath = "doc/client/mtime/comment/hot/";
                if (i == 1) {
                    url = "http://movie.mtime.com/" + this.mid + "/reviews/short/hot.html";
                } else {
                    url = "http://movie.mtime.com/" + this.mid + "/reviews/short/hot-" + i + ".html";
                }
            }

            WebConnect conn = new WebConnect(url);
            String sourceCode = conn.downloadPage().getSourceCode();

            //将源代码写入文件
            Writer w = new Writer(filepath, this.mid + ".txt");
            w.write(sourceCode, true);
            
            commentSourceCodeList.add(sourceCode);
        }
        
        return commentSourceCodeList;
    }
}
