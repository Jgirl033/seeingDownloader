/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.collector;

import java.util.ArrayList;
import team.beatles.file.Writer;
import team.beatles.web.WebConnect;
import static team.beatles.constant.Constant.mtimeDR;

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
            String userUrl = "http://sandbox.my.mtime.com/Service/callback.mc?Ajax_CallBack=true&Ajax_CallBackType=Mtime.MemberCenter.Pages.CallbackService&Ajax_CallBackMethod=RemoteLoad&Ajax_CrossDomain=1&Ajax_RequestUrl=http://my.mtime.com/" + uid + "/&Ajax_CallBackArgument0=t&Ajax_CallBackArgument1=" + uid + "/?$2";
            WebConnect conn = new WebConnect(userUrl);
            String sourceCode = conn.downloadPage().getSourceCode();
            Writer w = new Writer("doc/mtime/user/comment/", uid + ".txt");
            w.write(sourceCode);

            String profileUrl = "http://sandbox.my.mtime.com/Service/callback.mc?Ajax_CallBack=true&Ajax_CallBackType=Mtime.MemberCenter.Pages.CallbackService&Ajax_CallBackMethod=RemoteLoad&Ajax_CrossDomain=1&Ajax_RequestUrl=http://my.mtime.com/" + uid + "/profile/&Ajax_CallBackArgument0=t&Ajax_CallBackArgument1=" + uid + "/profile/?$3";
            mtimeDR.get(profileUrl);
            String profileSourceCode = mtimeDR.getPageSource();
            System.out.println(profileSourceCode);
            Writer w2 = new Writer("doc/mtime/user/profile/", uid + ".txt");
            w2.write(profileSourceCode);

        }
    }
}
