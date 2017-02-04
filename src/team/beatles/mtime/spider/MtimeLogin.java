/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.spider;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 时光网登录类
 *
 * @author admin Jgirl
 */
public class MtimeLogin {

    /**
     * 启动chrome浏览器，用于手动登录时光网账号
     *
     * @return WebDriver
     */
    public static WebDriver getWebDriver() {
        File file = new File("exe\\chromedriver.exe"); //chromediriver的指定目录
        WebDriver dr = null;
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort().build();
        try {
            service.start();
            dr = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
            dr.get("http://movie.mtime.com/");
            Thread.sleep(30000);
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(MtimeLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dr;
    }
}
