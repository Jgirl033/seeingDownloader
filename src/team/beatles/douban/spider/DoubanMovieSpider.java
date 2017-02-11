/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.douban.spider;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.douban.entity.DoubanMovie;
import team.beatles.downloader.file.Reader;
import team.beatles.downloader.web.WebConnect;
import team.beatles.downloader.web.WebPage;

/**
 * 爬取豆瓣电影基本信息并提供通过类中的方法能够获取电影相关文件的本地存储路径
 *
 * @author admin Jgirl
 */
public class DoubanMovieSpider {

    private final String mid;
    private final DoubanMovie movie;
    private final String sourceCode;

    /**
     * 使用豆瓣电影ID构造一个新的DoubanMovieSpider()
     *
     * @param mid 豆瓣电影唯一ID
     */
    public DoubanMovieSpider(String mid) {
        this.movie = new DoubanMovie();
        this.mid = mid;
 
        String filename = mid + ".txt";
        System.out.println("该电影的源代码路径是：" + filename);
        Reader r = new Reader("doc/server/douban/movie/", filename);
        this.sourceCode = r.read();
       
    }

    /**
     * 通过电影名字获取某部电影的基本信息及评分，包括豆瓣、海报链接、导演、编剧、演员、风格、地区、语言、上映时间、片长、剧情简介、获奖情况、相似电影、电影相关信息文件存储本地路径
     *
     * @return Movie 某部电影的基本信息
     */
    public DoubanMovie getMovie(){

        Pattern patternInfomation = Pattern.compile("<title>(.*?)</title>.*?<span class='pl'>导演</span>:(.*?)</span><br/>.*?<span class='pl'>编剧</span>:(.*?)</span><br/>.*?<span class='pl'>主演</span>:(.*?)</span><br/>.*?<span class=\"pl\">类型:(.*?)</span><br/>.*?<span class=\"pl\">制片国家/地区:(.*?)<br/>.*?<span class=\"pl\">语言:</span>(.*?)<br/>.*?<span class=\"pl\">上映日期:(.*?)</span><br/>.*?<span class=\"pl\">片长:</span>.*?<span property=\"v:runtime\".*?>(.*?)分钟.*?</span>.*?v:summary.*?>(.*?)</span>");
        Matcher matcherInformation = patternInfomation.matcher(this.sourceCode);
        if (matcherInformation.find()) {
            System.out.println(matcherInformation.group(1));
            movie.setMid(Integer.parseInt(this.mid));
            movie.setName(matcherInformation.group(1).replaceAll("<.*?>", "").replace("(豆瓣)", "").trim());
            movie.setImgSrc(this.getImage());
            movie.setDirector(matcherInformation.group(2).replaceAll("<.*?>", "").trim());
            movie.setScreenwriter(matcherInformation.group(3).replaceAll("<.*?>", "").trim());
            movie.setPerformer(matcherInformation.group(4).replaceAll("<.*?>", "").trim());
            movie.setStyle(matcherInformation.group(5).replaceAll("<.*?>", "").trim());
            movie.setArea(matcherInformation.group(6).replaceAll("<.*?>", "").trim());
            movie.setLanguage(matcherInformation.group(7).replaceAll("<.*?>", "").trim());
            movie.setReleaseTime(matcherInformation.group(8).replaceAll("<.*?>", "").trim());
            movie.setRuntime(matcherInformation.group(9).replaceAll("<.*?>", "").trim());
            movie.setSynopsis(matcherInformation.group(10).replaceAll("<.*?>", "").trim());
            String award = "";
            for (String item : this.getAwardsSituation()) {
                award += item.replaceAll("<.*?>", "").trim() + "#";
            }
            movie.setAward(award);
            String like = "";
            for (String item : this.getRecommendation()) {
                like += item.replaceAll("<.*?>", "").trim() + "#";
            }
            movie.setLike(like);
            movie.setJsonSrc("./" + this.mid);

            Pattern patternScore = Pattern.compile("<.*?rating_num.*?>(.*?)</strong>.*?v:votes.*?>(.*?)</span>.*?title=\"力荐.*?rating_per\">(.*?)%</span>.*?title=\"推荐.*?rating_per\">(.*?)%</span>.*?title=\"还行.*?rating_per\">(.*?)%</span>.*?title=\"较差.*?rating_per\">(.*?)%</span>.*?title=\"很差.*?rating_per\">(.*?)%</span>");
            Matcher matcherScore = patternScore.matcher(this.sourceCode);
            if (matcherScore.find()) {
                movie.setRating(Double.valueOf(matcherScore.group(1).trim()));
                movie.setEvaluationNumber(Integer.valueOf(matcherScore.group(2).trim()));
                movie.setFive(Double.valueOf(matcherScore.group(3).trim()));
                movie.setFour(Double.valueOf(matcherScore.group(4).trim()));
                movie.setThree(Double.valueOf(matcherScore.group(5).trim()));
                movie.setTwo(Double.valueOf(matcherScore.group(6).trim()));
                movie.setOne(Double.valueOf(matcherScore.group(7).trim()));
            } else {
                System.out.println("高能预警：正则表达式匹配不正确！");
            }
        }
        return this.movie;
    }

    /**
     * 获取某部电影的海报链接
     * @return String 某部电影的海报路径
     */
    public String getImage() {
        String imagePath = "";
        Pattern pattern = Pattern.compile("<a class=\".*?\" href=\".*?\" title=\"点击看更多海报\">.*?<img src=\"(.*?)\" title=\"点击看更多海报\" alt=\".*?\" rel=\"v:image\" />.*?</a>");
        Matcher matcher = pattern.matcher(this.sourceCode);
        if (matcher.find()) {
            imagePath = matcher.group(1);
        }
        return imagePath;
    }

    /**
     * 获取看过某部电影的用户也喜欢看的相关电影
     * @return ArrayList 电影获奖情况列表
     */
    public ArrayList<String> getRecommendation() {
        ArrayList<String> recommendation = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"recommendations-bd\">(.*?)</div>");
        Matcher matcher = pattern.matcher(this.sourceCode);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            pattern = Pattern.compile("<a href.*?>.*?<img.*?alt=\"(.*?)\"");
            matcher = pattern.matcher(tmp);
            while (matcher.find()) {
                recommendation.add(matcher.group(1));
            }
        }
        return recommendation;
    }

    /**
     * 获取某部电影的获奖情况
     * @return ArrayList 电影获奖情况列表
     */
    public ArrayList<String> getAwardsSituation() {
        String url = "https://movie.douban.com/subject/" + this.mid + "/awards/";
        ArrayList<String> awards = new ArrayList<>();
        WebPage awardsPage = new WebConnect(url).downloadPage();
        Pattern pattern = Pattern.compile("<ul class=\"award\">.*?<li>(.*?)</li>.*?<li>(.*?)</li>.*?</ul>");
        Matcher matcher = pattern.matcher(awardsPage.getSourceCode());
        while (matcher.find()) {
            awards.add(matcher.group(1).replaceAll("<.*?>", "").trim() + ":" + matcher.group(2).replaceAll("<.*?>", "").trim());
        }
        return awards;
    }
}
