package com.itsoku.lesson037.controller;

import com.itsoku.lesson037.common.Result;
import com.itsoku.lesson037.common.ResultUtils;
import com.itsoku.lesson037.dto.CashOutRequest;
import com.itsoku.lesson037.po.AccountPO;
import com.itsoku.lesson037.service.IAccountService;
import com.itsoku.lesson037.service.ICashOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/8 6:50 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ICashOutService cashOutService;

    @RequestMapping("/cashOut")
    public Result<Boolean> cashOut(@RequestBody CashOutRequest request) {
        //这里可以先拿到用户ID
        String accountId = "1";
        this.cashOutService.cashOut(accountId, request.getPrice());
        return ResultUtils.success(Boolean.TRUE);
    }

    public void m1(AccountPO accountPO){
        System.out.println();
    }

    private static ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private static ExpressionParser expressionParser = new SpelExpressionParser();
    public static void main(String[] args1) {
        Method method = ReflectionUtils.findMethod(AccountController.class, "m1",AccountPO.class);
        AccountPO accountPO = new AccountPO();
        accountPO.setId("123");
        Object [] args = new Object[]{accountPO};
        EvaluationContext context = new MethodBasedEvaluationContext(null,method,args,parameterNameDiscoverer);
        String expressionString = "'abc'+#accountPO.id";
        expressionString = "'abc'+(#accountPO?.id?:'xdf')";
        System.out.println(expressionParser.parseExpression(expressionString).getValue(context,String.class));
    }
}
