import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.lesson067.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/22 20:28 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SignTest {
    @Test
    public void transfer() {
        RestTemplate restTemplate = new RestTemplate();
        TransferRequest transferRequest = TransferRequest.builder().fromAccountId("张三").toAccountId("李四").transferPrice(new BigDecimal("100.00")).build();
        String body = JSONUtil.toJsonStr(transferRequest);

        //秘钥
        String secretKey = "b0e8668b-bcf2-4d73-abd4-893bbc1c6079";
        //随机数
        String nonce = UUID.randomUUID().toString();
        //时间戳
        String timestampStr = String.valueOf(System.currentTimeMillis() / 1000);
        // 签名：md5(secretKey+nonce+timestampStr+body)
        String sign = DigestUtil.md5Hex(String.format("%s%s%s%s", secretKey, nonce, timestampStr, body));
        //将时间戳、随机数、签名放到请求头中
        RequestEntity<String> request = RequestEntity
                .post("http://localhost:8080/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Timestamp", timestampStr)
                .header("X-Nonce", nonce)
                .header("X-Sign", sign)
                .body(body);

        System.out.println("X-Timestamp: " + timestampStr);
        System.out.println("X-Nonce: " + nonce);
        System.out.println("X-Sign: " + sign);
        System.out.println("请求体：" + body);

        ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("\n响应体：" + responseBody);
    }

}
