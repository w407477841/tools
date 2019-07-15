package com.xywg.test.ceshi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CeshiApplicationTests {

    @Test
    public void contextLoads() {
        Engine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("模板.flt");

        template.render(Dict.create()
                .set("beian", "XKSLAKSLSKALAKS")
                .set("gongdi", "工地1号")
                .set("riqi", "2019-01-01")
                .set("xinghao", "XY0001")
                .set("bianhao", "YY-KKLKAAS")
                .set("changjia", "星云")
                .set("danwei", "幸运")
                .set("gongcheng", "银江国际")
                .set("sn", "XY2019010122010")
                .set("heixiazibianhao", "896523521452365212")
                .set("shouji", "18932224279")
                .set("anzhuang", "2019-01-01")
                .set("heixiazixinghao", "XY0000001")
                .set("heixiazichangjia", "星运")
                .set("peizhi", "无")
                .set("shibie", "无"),
                FileUtil.file("d://moban.doc"));

    }

}
