/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.collector;

import java.util.ArrayList;
import org.openqa.selenium.TimeoutException;
import team.beatles.downloader.file.Writer;
import team.beatles.downloader.web.WebConnect;
import static team.beatles.constant.Constant.mtimeDR;
import team.beatles.downloader.web.WebPage;

/**
 * 时光网用户主页源代码下载类
 *
 * @author admin Jgirl
 */
public class MtimeUserPageCollector {

    /**
     * 构造一个新的MtimeUserPageCollector()
     */
    public MtimeUserPageCollector() {

    }

    /**
     * 启动时光网用户主页源代码下载程序，下载内容包括用户基本信息以及短评信息
     *
     * @param uidList 用户ID列表
     */
    public void start(ArrayList<String> uidList) {

        for (String uid : uidList) {
            System.out.println(MtimeUserPageCollector.class.getName() + "开始爬取用户" + uid + "的comment源代码");
            String userUrl = "http://sandbox.my.mtime.com/Service/callback.mc?Ajax_CallBack=true&Ajax_CallBackType=Mtime.MemberCenter.Pages.CallbackService&Ajax_CallBackMethod=RemoteLoad&Ajax_CrossDomain=1&Ajax_RequestUrl=http://my.mtime.com/" + uid + "/&Ajax_CallBackArgument0=t&Ajax_CallBackArgument1=" + uid + "/?$2";
            WebConnect conn = new WebConnect(userUrl);
            WebPage p = conn.downloadPage();
            while (true) {
                if (p.isSuccess()) {
                    break;
                } else {
                    p = conn.downloadPage();
                }
            }
            String sourceCode = p.getSourceCode();
            Writer w = new Writer("doc/client/mtime/user/comment/", uid + ".txt");
            w.write(sourceCode);
            System.out.println(MtimeUserPageCollector.class.getName() + "开始写入用户" + uid + "的comment源代码");

            System.out.println(MtimeUserPageCollector.class.getName() + "开始爬取用户" + uid + "的profile源代码");
            String profileUrl = "http://sandbox.my.mtime.com/Service/callback.mc?Ajax_CallBack=true&Ajax_CallBackType=Mtime.MemberCenter.Pages.CallbackService&Ajax_CallBackMethod=RemoteLoad&Ajax_CrossDomain=1&Ajax_RequestUrl=http://my.mtime.com/" + uid + "/profile/&Ajax_CallBackArgument0=t&Ajax_CallBackArgument1=" + uid + "/profile/?$3";
            while (true) {
                try {
                    mtimeDR.get(profileUrl);
                    break;
                } catch (TimeoutException e) {
                    System.out.println(e.toString());
                }
            }

            String profileSourceCode = mtimeDR.getPageSource();
            while (true) {
                if (profileSourceCode.contains("无法访问此网站".subSequence(0, 6))) {
                    profileSourceCode = mtimeDR.getPageSource();
                } else {
                    break;
                }
            }
            Writer w2 = new Writer("doc/client/mtime/user/profile/", uid + ".txt");
            w2.write(profileSourceCode);
            System.out.println(MtimeUserPageCollector.class.getName() + "开始写入用户" + uid + "的profile源代码");

        }
    }
}
