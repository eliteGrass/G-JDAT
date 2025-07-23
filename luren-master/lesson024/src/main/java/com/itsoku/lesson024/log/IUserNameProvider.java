package com.itsoku.lesson024.log;

/**
 * <b>description</b>： 用户名提供者接口，默认实现 ThreadLocalUserNameProvider(放在ThreadLocal中)，可以自己实现一个<br>
 * <b>time</b>：2024/4/22 14:38 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface IUserNameProvider {
    /**
     * 获取用户名
     *
     * @return
     */
    String getUserName();

    /**
     * 设置用户名
     *
     * @param userName
     */
    void setUserName(String userName);
}
