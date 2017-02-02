/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.file.Reader;
import team.beatles.mtime.entity.MtimeMovie;
import team.beatles.mtime.entity.MtimeMovieScore;

/**
 * 爬取时光网电影基本信息并提供通过类中的方法能够获取电影相关文件的本地存储路径
 *
 * @author admin Jgirl
 */
public class MtimeMovieSpider {

    private final String mid;
    private final MtimeMovie movie;
    private final String informationSourceCode;
    private final String scoreSourceCode;

    /**
     * 使用时光网电影ID构造一个新的MtimeMovieSpider()
     *
     * @param mid 时光网电影唯一ID
     */
    public MtimeMovieSpider(String mid) {
        this.movie = new MtimeMovie();
        this.mid = mid;

        String filename = mid + ".txt";
        Reader ri = new Reader("doc/mtime/movie/information/", filename);
        Reader rs = new Reader("doc/mtime/movie/score/", filename);
        this.informationSourceCode = ri.read();
        this.scoreSourceCode = rs.read();
    }

    /**
     * 获取电影基本信息
     * @return Movie 某部电影的基本信息
     */
    public MtimeMovie getMovie() {
        movie.setMid(Integer.parseInt(this.mid));
        movie.setJsonSrc("./" + this.mid);

        Pattern patternName = Pattern.compile("<h1.*?\">(.*?)</h1>.*?<dl class=\"info_l\">");
        Matcher matcherName = patternName.matcher(this.informationSourceCode);
        if (matcherName.find()) {
            movie.setName(matcherName.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternDirector = Pattern.compile("导演：.*?<a href=\".*?\" target=\"_blank\" rel=\"v:directedBy\">(.*?)</a>");
        Matcher matcherDirector = patternDirector.matcher(this.informationSourceCode);
        if (matcherDirector.find()) {
            movie.setDirector(matcherDirector.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternScreenWriter = Pattern.compile("编剧：.*?</strong>.*?<a href=\".*?\" target=\"_blank\">(.*?)</a>");
        Matcher matcherScreenWriter = patternScreenWriter.matcher(this.informationSourceCode);
        if (matcherScreenWriter.find()) {
            movie.setScreenwriter(matcherScreenWriter.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternCompany = Pattern.compile("发行公司：.*?</strong>.*?<a href=\".*?\" target=\"_blank\">(.*?)</a>");
        Matcher matcherCompany = patternCompany.matcher(this.informationSourceCode);
        if (matcherCompany.find()) {
            movie.setCompany(matcherCompany.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternArea = Pattern.compile("国家地区：.*?</strong>.*?<a href=\".*?\" target=\"_blank\">(.*?)</a>.*?<span>");
        Matcher matcherArea = patternArea.matcher(this.informationSourceCode);
        if (matcherArea.find()) {
            movie.setArea(matcherArea.group(1).replaceAll("<.*?>", "").trim());
        }

        Pattern patternSynopsis = Pattern.compile("剧情：.*?</h4>.*?<p class=\"mt6 lh18\">(.*?)</p>");
        Matcher matcherSynopsis = patternSynopsis.matcher(this.informationSourceCode);
        if (matcherSynopsis.find()) {
            movie.setSynopsis(matcherSynopsis.group(1).replaceAll("<.*?>", "").trim());
        }

        //获取电影演员列表
        Pattern patternPerformance = Pattern.compile("<dl class=\"main_actor\">.*?<a href=\".*?\" target=\"_blank\" rel=\"v:starring\">(.*?)</a>.*?</p>.*?</dl>");
        Matcher matcherPerformance = patternPerformance.matcher(this.informationSourceCode);
        String performance = "";
        while (matcherPerformance.find()) {
            performance += matcherPerformance.group(1).replaceAll("<.*?>", "").trim() + "#";
        }
        movie.setPerformer(performance);

        MtimeMovieScore s = this.getScore();
        movie.setRating(s.getRating());
        movie.setEvaluationNumber(s.getEvaluation_number());
        movie.setBookOffice(s.getBook_office());

        return this.movie;
    }

    /**
     * 获取电影的评分情况
     * @return MtimeMovieScore 某部电影的评分情况
     */
    public MtimeMovieScore getScore() {
        //获取电影评价信息
        MtimeMovieScore s = new MtimeMovieScore(this.mid);
        Pattern patternScore = Pattern.compile("\"Usercount\":(.*?),.*?\"Rank\":(.*?),\"TotalBoxOffice\":\"(.*?)\",");
        Matcher matcherScore = patternScore.matcher(this.scoreSourceCode);
        if (matcherScore.find()) {
            s.setRating(Double.valueOf(matcherScore.group(2).trim()));
            s.setEvaluation_number(Integer.valueOf(matcherScore.group(1).trim()));
            s.setBook_office(Double.valueOf(matcherScore.group(3).trim()));
            System.out.println(s.getBook_office());
        } else {
            System.out.println("高能预警：评价正则表达式匹配不正确！");
        }
        return s;
    }
}
