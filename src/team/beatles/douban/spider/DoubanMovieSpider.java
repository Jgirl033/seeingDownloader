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
    private final String informationSourceCode;

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
        this.informationSourceCode = r.read();

    }

    /**
     * 通过电影名字获取某部电影的基本信息及评分，包括豆瓣、海报链接、导演、编剧、演员、风格、地区、语言、上映时间、片长、剧情简介、获奖情况、相似电影、电影相关信息文件存储本地路径
     *
     * @return Movie 某部电影的基本信息
     */
    public DoubanMovie getMovie() {

        movie.setMid(Integer.parseInt(this.mid));

        Pattern patternName = Pattern.compile("<title>(.*?)</title>");
        Matcher matcherName = patternName.matcher(this.informationSourceCode);
        if (matcherName.find()) {
            movie.setName(matcherName.group(1).replaceAll("<.*?>", "").replace("(豆瓣)", "").trim());
        }

        movie.setImgSrc(this.getImage());

        Pattern patternDirector = Pattern.compile("<span class='pl'>导演</span>:(.*?)</span>");
        Matcher matcherDirector = patternDirector.matcher(this.informationSourceCode);
        if (matcherDirector.find()) {
            movie.setDirector(matcherDirector.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternScreenwriter = Pattern.compile("<span class='pl'>编剧</span>:(.*?)</span>");
        Matcher matcherScreenwriter = patternScreenwriter.matcher(this.informationSourceCode);
        if (matcherScreenwriter.find()) {
            movie.setScreenwriter(matcherScreenwriter.group(1).replaceAll("<.*?>", "").trim());
        }else{
            movie.setScreenwriter("");
        }

        Pattern patternPerformance = Pattern.compile("<span class='pl'>主演</span>:(.*?)</span>");
        Matcher matcherPerformance = patternPerformance.matcher(this.informationSourceCode);
        String performance = "";
        if (matcherPerformance.find()) {
            performance = matcherPerformance.group(1).replaceAll("<.*?>", "").trim();
        }
        movie.setPerformer(performance);

        Pattern patternStyle = Pattern.compile("<span class=\"pl\">类型:(.*?)</span>");
        Matcher matcherStyle = patternStyle.matcher(this.informationSourceCode);
        if (matcherStyle.find()) {
            movie.setStyle(matcherStyle.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternArea = Pattern.compile("<span class=\"pl\">制片国家/地区:(.*?)<br/>");
        Matcher matcherArea = patternArea.matcher(this.informationSourceCode);
        if (matcherArea.find()) {
            movie.setArea(matcherArea.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternLanguage = Pattern.compile("<span class=\"pl\">语言:</span>(.*?)<br/>");
        Matcher matcherLanguage = patternLanguage.matcher(this.informationSourceCode);
        if (matcherLanguage.find()) {
            movie.setLanguage(matcherLanguage.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternReleaseTime = Pattern.compile("<span class=\"pl\">上映日期:(.*?)</span>");
        Matcher matcherReleaseTime = patternReleaseTime.matcher(this.informationSourceCode);
        if (matcherReleaseTime.find()) {
            movie.setReleaseTime(matcherReleaseTime.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternRuntime = Pattern.compile("<span class=\"pl\">片长:</span>.*?<span property=\"v:runtime\".*?>(.*?)分钟.*?</span>");
        Matcher matcherRuntime = patternRuntime.matcher(this.informationSourceCode);
        if (matcherRuntime.find()) {
            movie.setRuntime(matcherRuntime.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternSynopsis = Pattern.compile("v:summary.*?>(.*?)</span>");
        Matcher matcherSynopsis = patternSynopsis.matcher(this.informationSourceCode);
        if (matcherSynopsis.find()) {
            movie.setSynopsis(matcherSynopsis.group(1).replaceAll("<.*?>", "").trim());
        }

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
        Matcher matcherScore = patternScore.matcher(this.informationSourceCode);
        if (matcherScore.find()) {
            movie.setRating(Double.valueOf(matcherScore.group(1).trim()));
            movie.setEvaluationNumber(Integer.valueOf(matcherScore.group(2).trim()));
            movie.setFive(Double.valueOf(matcherScore.group(3).trim()));
            movie.setFour(Double.valueOf(matcherScore.group(4).trim()));
            movie.setThree(Double.valueOf(matcherScore.group(5).trim()));
            movie.setTwo(Double.valueOf(matcherScore.group(6).trim()));
            movie.setOne(Double.valueOf(matcherScore.group(7).trim()));
        } else {
            System.out.println("高能预警：解析电影评价情况的正则表达式匹配不正确！");
        }
        return this.movie;
    }

    /**
     * 获取某部电影的海报链接
     *
     * @return String 某部电影的海报路径
     */
    public String getImage() {
        String imagePath = "";
        Pattern pattern = Pattern.compile("<a class=\".*?\" href=\".*?\" title=\"点击看更多海报\">.*?<img src=\"(.*?)\" title=\"点击看更多海报\" alt=\".*?\" rel=\"v:image\" />.*?</a>");
        Matcher matcher = pattern.matcher(this.informationSourceCode);
        if (matcher.find()) {
            imagePath = matcher.group(1);
        }
        return imagePath;
    }

    /**
     * 获取看过某部电影的用户也喜欢看的相关电影
     *
     * @return ArrayList 电影获奖情况列表
     */
    public ArrayList<String> getRecommendation() {
        ArrayList<String> recommendation = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"recommendations-bd\">(.*?)</div>");
        Matcher matcher = pattern.matcher(this.informationSourceCode);
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
     *
     * @return ArrayList 电影获奖情况列表
     */
    public ArrayList<String> getAwardsSituation() {
        String url = "https://movie.douban.com/subject/" + this.mid + "/awards/";
        ArrayList<String> awards = new ArrayList<>();
        WebPage awardsPage = new WebConnect(url).downloadPage();

        String awardsSourceCode = awardsPage.getSourceCode();
        Pattern patternContent = Pattern.compile("<div id=\"content\">(.*?)</div>");
        Matcher matcherContent = patternContent.matcher(awardsSourceCode);

        String content;
        if (matcherContent.find()) {
            content = matcherContent.group(1);
        }else{
            content = "";
        }
        
        Pattern pattern = Pattern.compile("<ul class=\"award\">.*?<li>(.*?)</li>.*?<li>(.*?)</li>.*?</ul>");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            awards.add(matcher.group(1).replaceAll("<.*?>", "").trim() + ":" + matcher.group(2).replaceAll("<.*?>", "").trim());
        }
        return awards;
    }
}
