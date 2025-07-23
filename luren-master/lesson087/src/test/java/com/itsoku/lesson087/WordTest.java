package com.itsoku.lesson087;

import com.deepoove.poi.XWPFTemplate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 87.Java动态生成word，太强大了
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/7 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class WordTest {

    @Test
    public void test1() throws IOException {
        //模板中需要填充的数据
        Map<String, Object> model = new HashMap<>();
        model.put("userName", "路人");
        model.put("age", 30);
        model.put("description", "正在连载《java高并发 & 微服务 & 性能调优实战案例100讲》，涵盖：高并发、接口性能优化、幂等、超卖、MQ专题、分布式事务、分库分表、常见问题排查、接口签名、接口加解密等各种实战案例；需要的朋友加我微信：itsoku");

        //模板文件
        String templatePath = "..\\luren\\lesson087\\src\\main\\resources\\template.docx";
        //模板 + 数据，合并后产生的目标文件的存放的地址
        String outFile = "..\\luren\\lesson087\\src\\main\\resources\\1.docx";
        //通过word模板引擎，将模板和数据进行组合，生成新的文件
        XWPFTemplate.compile(templatePath).render(model).writeToFile(outFile);
    }

}
