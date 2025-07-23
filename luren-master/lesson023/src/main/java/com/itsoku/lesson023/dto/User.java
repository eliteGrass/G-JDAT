package com.itsoku.lesson023.dto;

import com.itsoku.lesson023.desensitization.Desensitization;
import com.itsoku.lesson023.desensitization.DesensitizationStrategy;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/17 15:29 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class User {
    // id
    private String id;
    // 姓名
    @Desensitization(DesensitizationStrategy.NAME)
    private String name;
    // 手机号
    @Desensitization(DesensitizationStrategy.PHONE)
    private String phone;
    // 邮箱
    @Desensitization(DesensitizationStrategy.EMAIL)
    private String email;
    // 银行卡
    @Desensitization(DesensitizationStrategy.ID_CARD)
    private String idCard;
    // 密码
    @Desensitization(DesensitizationStrategy.PASSWORD)
    private String password;

    // 地址
    @Desensitization(DesensitizationStrategy.ADDRESS)
    private String address;

    //银行卡
    @Desensitization(DesensitizationStrategy.BANK_CARD)
    private String bankCard;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String backCard) {
        this.bankCard = backCard;
    }
}
