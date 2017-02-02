/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.collector;

import team.beatles.file.Writer;
import team.beatles.web.WebConnect;
import team.beatles.web.WebPage;

/**
 * 豆瓣电影主页源代码下载类
 * @author admin Jgirl
 */
public class DoubanMoviePageCollector {

    private final String mid;

    /**
     * 使用豆瓣电影ID构造一个新的DoubanMoviePageCollector() 
     * @param mid 豆瓣电影ID
     */
    public DoubanMoviePageCollector(String mid) {
        this.mid = mid;
    }

    /**
     *
     */
    public void start() {

        String url = "https://movie.douban.com/subject/" + this.mid + "/";
        WebConnect conn = new WebConnect(url);
        String sourceCode = "";
        while (true) {
            WebPage page = conn.downloadPage();
            if (page.isSuccess()) {
                sourceCode = page.getSourceCode();
                break;
            } else {
                System.out.println("高能预警：爬取失败！！！！(╬▔皿▔)握草(╯‵□′)╯︵┻━┻    ");
            }
        }

        String filename = this.mid + ".txt";
        Writer w = new Writer("doc/client/douban/movie/", filename);
        w.write(sourceCode);
    }

}
