package com.xywg.pachong.lihangtadiao.core;


import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xywg.pachong.lihangtadiao.core.model.CraneDetailDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017-9-4.
 */
public class WbHandler{

    public static String timer=null;
    public static String uuid = "";
    public static final String deviceNo = "00118052902";

public static void satrt(){
    System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
    ChromeDriver driver = new ChromeDriver();
    driver.get("https://www.safetypm.com/Login.aspx");

     driver.findElementById("username").sendKeys("溧阳南大街");
     driver.findElementById("password").sendKeys("123456");
     driver.findElementById("Login").click();
    // 先进 历史
    histroypage(driver);
    // 切换项目
    changeProject(driver);
    try {
        Thread.sleep(3000);
    } catch (Exception e) {
        e.printStackTrace();
    }

    query(driver);
}

    public static void histroypage(ChromeDriver driver){
        try {
            System.out.println("历史记录页面");
            // 历史页面
        Thread.sleep(1000);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElementById("CD_400")).build().perform();
            Thread.sleep(100);
        // 先到 历史数据页面再说
        driver.findElementById("CD_402").click();
        } catch (Exception e) {
            histroypage(driver);
        }


    }


    /**
     * 切换项目
     * @param driver
     */
    public static void changeProject( ChromeDriver driver){
    // 切换项目
    try {
        System.out.println("切换项目");
        Thread.sleep(1000);
        driver.findElementById("ztree2_2_switch").click();
    }catch (Exception e){
        changeProject(driver);
    }

}

    /**
     * 切换到 下线的的那台
     * @param driver
     */
    public static void changeoffline(ChromeDriver driver){
        // 选设备
        System.out.println("切到离线设备开始");
        try {
            Thread.sleep(1000);
        driver.findElementById("ztree2_3_span").click();

        } catch (Exception e) {
            changeoffline(driver);
        }
        System.out.println("切到离线设备结束");
}

    public  static void changeonline(ChromeDriver driver){
        System.out.println("切到在线设备开始");
        try {
            Thread.sleep(1000);
            driver.findElementById("ztree2_4_span").click();
        } catch (Exception e) {
            changeonline(driver);
        }
        System.out.println("切到在线设备结束");
    }

    public static  void  loaddata(ChromeDriver driver){
        System.out.println("在搞数据 开始");
        List<WebElement> trList = null;
        try {
            Thread.sleep(1000);
            trList =    driver.findElementsByClassName("gridTbody").get(0).findElements(By.tagName("tr"));
            if(!CollectionUtils.isEmpty(trList)){
                WebElement we = trList.get(0);
                List<WebElement>  devList =  we.findElements(By.tagName("div"));
                // System.out.println(devList.size());
                CraneDetailDTO craneDetailDTO =new CraneDetailDTO();
                if(!devList.get(1).getText().equals(timer)){
                    craneDetailDTO.setDeviceNo(deviceNo);
                    craneDetailDTO.setTime(DateUtil.parse(devList.get(1).getText()));
                    craneDetailDTO.setWeight(new BigDecimal(devList.get(2).getText()));
                    craneDetailDTO.setHeight(new BigDecimal(devList.get(3).getText()));
                    craneDetailDTO.setRange(new BigDecimal(devList.get(4).getText()));
                    craneDetailDTO.setRotaryAngle(new BigDecimal(devList.get(5).getText()));
                    craneDetailDTO.setMoment(new BigDecimal(devList.get(6).getText()));
                    craneDetailDTO.setTiltAngle(new BigDecimal(devList.get(7).getText()));
                    craneDetailDTO.setWindSpeed(new BigDecimal(devList.get(8).getText()));
                    craneDetailDTO.setMomentPercentage(new BigDecimal(devList.get(9).getText()));
                    craneDetailDTO.setStatus(0);
                    timer = devList.get(1).getText();
                    System.out.println(JSONUtil.toJsonStr(craneDetailDTO));
                    System.out.println(HttpUtil.post("http://device.jsxywg.cn/ssdevice/device/crane/insertData?uuid=6f7fcd825a8143d8a1c5d86929264de9",JSONUtil.toJsonStr(craneDetailDTO)));
                }
            }
        } catch (Exception e) {
            loaddata(driver);
        }

        System.out.println("在搞数据 结束");
    }

    /**
     * 循环操作
     * @param driver
     */
    public static void query(ChromeDriver driver){

    // 切设备
        try {
            changeoffline(driver);
            changeonline(driver);
            loaddata(driver);
            query(driver);
        } catch (Exception e) {
            query(driver);
        }





}


}















