package com.afunms.common.util.logging;

/**
 * 日志扩展接口
 * 如果需要使用自定义的日志，则必须实现该接口，
 * 以达到让日志工厂来实例化
 * @author 聂林
 * @version 1.0 $Date 2011-05-31 23:12:19 +0100 (Tue, Match 31, 2011)$
 */
public interface Logger extends Log {

    /**
     * 通过需要使用日志的类的完全限定名，来获取一个新的日志实例。
     * @param className
     *          需要使用日志的类的完全限定名
     * @return {@link Logger}
     *          返回新的日志实例
     */
    Logger getNewInstance(String className);

}
