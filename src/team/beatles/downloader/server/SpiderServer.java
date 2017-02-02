/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.downloader.server;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.mtime.util.MtimeDBCheck;
import team.beatles.web.WebConnect;

/**
 * 获取新上架的电影名称以及对应的豆瓣ID和时光网ID，即控制节点
 *
 * @author admin Jgirl
 */
public class SpiderServer {

    /**
     * 使用"电影"关键字在百度上进行搜索，对其返回网页进行解析，获取新上架的电影名称列表
     *
     * @return List 新上架的电影名称列表
     */
    public static List<String> getMovieName() {
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?resource_id=6862&from_mid=1&&format=json&ie=utf-8&oe=utf-8&query=电影&sort_key=17&sort_type=1&stat0=&stat1=&stat2=&stat3=&pn=0&rn=8";;
        List<String> movieUnfinishedList = new ArrayList<>();
        WebConnect conn = new WebConnect(url);
        String sourceCode = conn.downloadPage().getSourceCode();
        Pattern pattern = Pattern.compile("\"ename\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(sourceCode);
        while (matcher.find()) {
            movieUnfinishedList.add(matcher.group(1).trim());
        }
        return movieUnfinishedList;
    }

    /**
     * 将新上架的电影名称在时光网上遍历检索，获取其对应的ID
     * @return List 新上架的电影的时光网ID列表
     */
    public static List<String> getMtimeMovieID() {
        List<String> movieIDUnfinishedList = new ArrayList<>();
        List<String> movieUnfinishedList = getMovieName();
        for (String movie : movieUnfinishedList) {

            StringBuilder string = new StringBuilder();
            String[] hex = movie.split("\\\\u");
            for (int i = 1; i < hex.length; i++) {
                // 转换出每一个代码点
                int data = Integer.parseInt(hex[i], 16);
                // 追加成string
                string.append((char) data);
            }

            String url = "http://service.channel.mtime.com/Search.api?Ajax_CallBack=true&Ajax_CallBackType=Mtime.Channel.Services&Ajax_CallBackMethod=GetSearchResult&Ajax_CrossDomain=1&Ajax_RequestUrl=http://search.mtime.com/search/?q=" + string + "&t=0&Ajax_CallBackArgument0=" + string + "&Ajax_CallBackArgument1=0&Ajax_CallBackArgument2=290&Ajax_CallBackArgument3=0&Ajax_CallBackArgument4=1";
            Pattern pattern = Pattern.compile("\"movieId\":(.*?),\"movieTitle\":");
            WebConnect conn = new WebConnect(url);
            Matcher matcher = pattern.matcher(conn.downloadPage().getSourceCode());
            MtimeDBCheck dbc = new MtimeDBCheck();
            if (matcher.find() && !dbc.isMovieExist(matcher.group(1).trim())) {
                movieIDUnfinishedList.add(matcher.group(1).trim());
            }
        }
        return movieIDUnfinishedList;
    }
}
