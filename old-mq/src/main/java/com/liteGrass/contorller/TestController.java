package com.liteGrass.contorller;


/**
 * Copyright (c) 2024 Huahui Information Technology Co., LTD.
 * and China Nuclear Engineering & Construction Corporation Limited (Loongxin Authors).
 * All Rights Reserved.
 * <p>
 * This software is part of the Zhonghe Loongxin Development Platform (the "Platform").
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <p>
 * For more information about the Platform, terms and conditions, and user licenses,
 * please visit our official website at www.icnecc.com.cn or contact us directly.
 */

import com.liteGrass.service.UserService;
import com.liteGrass.trans.anno.ZhlxTransactionalMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName controller
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 23:38
 * @Description
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    private final UserService userService;

    @ZhlxTransactionalMessage(messageBuilderFunName = "TestFuns")
    @GetMapping("/send/{username}")
    public String sendNormalMsg(@PathVariable("username") String username) throws MQClientException {
        userService.saveUser(username);
        return "msg";
    }
}
