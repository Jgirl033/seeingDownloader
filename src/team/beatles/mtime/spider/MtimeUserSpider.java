/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.spider;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team.beatles.downloader.file.Reader;
import team.beatles.mtime.entity.MtimeUser;

/**
 * 爬取时光网用户的基本资料信息
 *
 * @author admin Jgirl
 */
public class MtimeUserSpider {

    private final String uid;
    private final String profileSourceCode;
    private final String commentSourceCode;

    /**
     * 使用时光网用户ID构造一个新的MtimeUserSpider()
     *
     * @param uid 时光网用户唯一ID
     */
    public MtimeUserSpider(String uid) {
        this.uid = uid;
        String filename = this.uid + ".txt";
        Reader ru = new Reader("doc/server/mtime/user/profile/", filename);
        Reader rc = new Reader("doc/server/mtime/user/comment/", filename);
        this.profileSourceCode = ru.read();
        this.commentSourceCode = rc.read();
    }

    /**
     * 获取时光网用户昵称
     *
     * @return String 时光网用户昵称
     */
    public String getName() {
        String name = "";
        Pattern pattern = Pattern.compile("<h3 class=\\\\\"fl hei px24\\\\\">(.*?)</h3>");
        Matcher matcher = pattern.matcher(this.commentSourceCode);
        if (matcher.find()) {
            name = matcher.group(1).trim();
        }
        return name;
    }

    /**
     * 获取时光网用户所在地信息
     *
     * @return String 时光网用户所在地
     */
    public String getArea() {
        String area = "";
        Pattern pattern = Pattern.compile("<p class=\\\\\"mt9\\\\\">(.*?)</p>");
        Matcher matcher = pattern.matcher(this.commentSourceCode);
        if (matcher.find()) {
            area = matcher.group(1).split("，")[1];
        } else {

        }
        return area;
    }

    /**
     * 获取时光网用户的性别信息
     *
     * @return String 时光网用户性别
     */
    public String getSex() {
        String sex = "";
        Pattern pattern = Pattern.compile("<p class=\\\\\"mt9\\\\\">(.*?)，.*?</p>");
        Matcher matcher = pattern.matcher(this.commentSourceCode);
        if (matcher.find()) {
            sex = matcher.group(1).trim();
        }
        return sex;
    }

    /**
     * 获取时光网用户的用户标签信息
     *
     * @return String 时光网用户标签
     */
    public String getTag() {
        String tmpSourceCode;
        Pattern pattern1 = Pattern.compile("<!--标签-->.*?<div class=\\\\\"pt20 tr_link\\\\\"> <h4 class=\\\\\"px14\\\\\">.*?</h4> <p class=\\\\\"mt9 pb9\\\\\">(.*?)</p>.*?</div> ");
        Matcher matcher1 = pattern1.matcher(this.commentSourceCode);
        if (matcher1.find()) {
            tmpSourceCode = matcher1.group().replaceAll("<.*?>", "").trim();
        } else {
            tmpSourceCode = this.profileSourceCode;
        }

        String tags = "";
        Pattern pattern2 = Pattern.compile("<a href=\\\\\".*?\\\\\" class=\\\\\"mr6\\\\\">(.*?)</a>");
        Matcher matcher2 = pattern2.matcher(tmpSourceCode);

        while (matcher2.find()) {
            tags += matcher2.group(1).trim() + "#";
        }
        return tags;
    }

    /**
     * 获取时光网用户的教育或职业信息
     *
     * @return String 时光网用户的教育或职业信息
     */
    public String getEducationJob() {
        //获取教育&职业部分的源代码
        Pattern pattern1 = Pattern.compile("<h4 class=\\\\\"px14\\\\\">教育&职业</h4>.*?<ul class=\\\\\"my_hotlist mt9\\\\\">(.*?)</ul> ");
        Matcher matcher1 = pattern1.matcher(this.commentSourceCode);
        String content = "";
        String eduJob = "";
        if (matcher1.find()) {
            content = matcher1.group(1);
        }

        Pattern pattern2 = Pattern.compile("<a.*?>(.*?)</a>");
        Matcher matcher2 = pattern2.matcher(content);
        while (matcher2.find()) {
            eduJob += matcher2.group(1).trim() + "#";
        }
        return eduJob;

    }

    /**
     * 获取时光网用户生日信息
     *
     * @return String 时光网用户生日
     */
    public String getBirthday() {
        String birthday = "";
        Pattern pattern1 = Pattern.compile("生日：([0-9]{4})年(.*?)月(.*?)日");
        Matcher matcher1 = pattern1.matcher(this.profileSourceCode);

        Pattern pattern2 = Pattern.compile("生日：(.*?)月(.*?)日");
        Matcher matcher2 = pattern2.matcher(this.profileSourceCode);
        if (matcher1.find()) {
            birthday = matcher1.group(1).trim() + "/" + matcher1.group(2).trim() + "/" + matcher1.group(3).trim();
        } else if (matcher2.find()) {
            birthday = matcher2.group(1).trim() + "/" + matcher2.group(2).trim();
        }
        return birthday;
    }

    /**
     * 获取时光网用户星座信息
     *
     * @param m 出生月份
     * @param d 出生日
     * @return String 时光网用户星座信息
     */
    public String getConstellation(int m, int d) {
        final String[] constellationArr = {"魔羯座", "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
        final int[] constellationEdgeDay = {20, 18, 20, 20, 20, 21, 22, 22, 22, 22, 21, 21};
        int month = m;
        int day = d;
        if (day <= constellationEdgeDay[month - 1]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        //default to return 魔羯     
        return constellationArr[11];
    }

    /**
     * 将时光网用户信息生产用户实体类
     *
     * @return MtimeUser 返回时光网用户信息类
     */
    public MtimeUser getUser() {
        Calendar a = Calendar.getInstance();
        int year = a.get(Calendar.YEAR);
        MtimeUser user = new MtimeUser();

        user.setUid(this.uid);
        user.setName(this.getName());
        user.setEducationJob(this.getEducationJob());
        user.setSource(2);
        user.setTags(this.getTag());
        user.setArea(user.getArea());
        user.setBirthday(this.getBirthday());
        if (this.getBirthday() != "") {
            System.out.println(this.uid + "的生日是：" + this.getBirthday());
            String[] birthday = this.getBirthday().split("/");
            int len = birthday.length;
            int month = Integer.parseInt(birthday[len - 2]);
            int day = Integer.parseInt(birthday[len - 1]);
            user.setAge(year - Integer.parseInt(birthday[0]));
            user.setConstellation(this.getConstellation(month, day));
        } else {
            user.setAge(-1);
            user.setConstellation("");
        }
        user.setSex(this.getSex());
        return user;
    }
}
