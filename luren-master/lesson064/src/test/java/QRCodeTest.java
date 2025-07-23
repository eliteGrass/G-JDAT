import cn.hutool.extra.qrcode.QrCodeUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * 第64节 使用hutool，生成&解析，二维码
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/17 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class QRCodeTest {

    @Test
    public void qrCodeDecode() {
        //二维码文件
        File qrCodeFile = new File("D:\\code\\likun_557\\luren\\lesson064\\src\\test\\resources\\二维码.png");
        //使用hutool中的工具类解析二维码，得到二维码中的内容
        String content = QrCodeUtil.decode(qrCodeFile);
        System.out.println(content);
    }
}
