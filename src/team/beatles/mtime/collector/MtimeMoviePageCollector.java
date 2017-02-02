/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.collector;

import team.beatles.file.Writer;
import team.beatles.web.WebConnect;

/**
 * 时光网电影主页源代码下载类
 * @author admin Jgirl
 */
public class MtimeMoviePageCollector {

    private final String mid;

    /**
     * 使用电影ID构造一个新的MtimeMoviePageCollector()
     * @param mid 电影ID
     */
    public MtimeMoviePageCollector(String mid) {
        this.mid = mid;
    }

    /**
     * 启动时光网电影主页源代码下载程序，下载内容包括电影基本信息以及评分情况
     */
    public void start() {
        this.startInformation();
        this.startScore();
    }

    /**
     * 时光网电影基本信息源代码下载程序
     */
    public void startInformation() {
        String url = "http://movie.mtime.com/" + this.mid + "/";
        WebConnect conn = new WebConnect(url);
        String sourceCode = conn.downloadPage().getSourceCode();
        String filename = this.mid + ".txt";
        Writer w = new Writer("doc/mtime/movie/information/", filename);
        w.write(sourceCode);
    }

    /**
     * 时光网电影评分情况源代码下载程序
     */
    public void startScore() {

        String scoreUrl = "http://service.library.mtime.com/Movie.api?Ajax_CallBack=true&Ajax_CallBackType=Mtime.Library.Services&Ajax_CallBackMethod=GetMovieOverviewRating&Ajax_CrossDomain=1&Ajax_RequestUrl=http://movie.mtime.com/" + this.mid + "/&Ajax_CallBackArgument0=" + this.mid;
        WebConnect sourceCode = new WebConnect(scoreUrl);
        String scoreSourceCode = sourceCode.downloadPage().getSourceCode();
        String filename = this.mid + ".txt";
        Writer w = new Writer("doc/mtime/movie/score/", filename);
        w.write(scoreSourceCode);
    }

}
