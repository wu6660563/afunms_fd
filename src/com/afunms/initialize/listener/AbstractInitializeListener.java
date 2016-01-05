/*
 * @(#)AbstractInitializeListener.java     v1.01, 2014 1 8
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.afunms.initialize.ResourceCenter;
import com.afunms.initialize.SysInitializeListener;

/**
 * ClassName:   AbstractInitializeListener.java
 * <p>{@link AbstractInitializeListener} 抽象的初始化监听类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 8 17:00:59
 */
public abstract class AbstractInitializeListener implements SysInitializeListener {

    /**
     * resourceCenter:
     * <p>资源中心
     *
     * @since   v1.01
     */
    private ResourceCenter resourceCenter;
    
    /**
     * builder:
     * <p>用于解析 XML 文件的 {@link SAXBuilder}
     *
     * @since   v1.01
     */
    private SAXBuilder builder;

    /**
     * AbstractInitializeListener.java:
     * 构造方法
     *
     * @since   v1.01
     */
    public AbstractInitializeListener() {
        setResourceCenter(ResourceCenter.getInstance());
        setBuilder(new SAXBuilder());
    }

//    /**
//     * getSysPath:
//     * <p>获取系统路径
//     *
//     * @return  {@link String}
//     *          - 系统路径
//     *
//     * @since   v1.01
//     */
//    public String getSysPath() {
//        return getResourceCenter().getSysPath();
//    }

    /**
     * getDocument:
     * <p>获取文档
     *
     * @param   configFile
     *          - 配置文件
     * @return  {@link Document}
     *          - 文档
     * @throws  JDOMException
     * @throws  IOException
     *
     * @since   v1.01
     */
    public Document getDocument(String configFile) throws JDOMException, IOException {
        return getBuilder().build(configFile);
    }

    /**
     * getResourceCenter:
     * <p>
     *
     * @return  ResourceCenter
     *          -
     * @since   v1.01
     */
    public ResourceCenter getResourceCenter() {
        return resourceCenter;
    }

    /**
     * setResourceCenter:
     * <p>
     *
     * @param   resourceCenter
     *          -
     * @since   v1.01
     */
    public void setResourceCenter(ResourceCenter resourceCenter) {
        this.resourceCenter = resourceCenter;
    }

    /**
     * getBuilder:
     * <p>
     *
     * @return  SAXBuilder
     *          -
     * @since   v1.01
     */
    public SAXBuilder getBuilder() {
        return builder;
    }

    /**
     * setBuilder:
     * <p>
     *
     * @param   builder
     *          -
     * @since   v1.01
     */
    public void setBuilder(SAXBuilder builder) {
        this.builder = builder;
    }

}

