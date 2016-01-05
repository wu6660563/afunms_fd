/**
 * <p>Description:system initialize,loads system resources when server starting</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.initialize;

import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.afunms.common.util.SysLogger;

/**
 * 系统初始化，当系统启动时进行初始化的配置
 * @author Administrator
 *
 */
public class SysInitialize {

    /**
     * builder:
     * <p>用于解析 XML 文件的 {@link SAXBuilder}
     *
     * @since   v1.01
     */
    private SAXBuilder builder;

    /**
     * INITIALIZE_CONFIG_FILE:
     * <p>初始化配置
     *
     * @since   v1.01
     */
    private final static String INITIALIZE_CONFIG_FILE = "WEB-INF/classes/initialize-config.xml";

    /**
     * AbstractInitializeListener.java:
     * 构造方法
     *
     * @since   v1.01
     */
    public SysInitialize() {
        setBuilder(new SAXBuilder());
    }
    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(SysInitialize.class.getName());

    @SuppressWarnings({ "static-access", "unchecked" })
    public void init() {
        logger.info("系统正在启动，请稍候...........");
        try {           
            Document doc = getDocument(getSysPath() + INITIALIZE_CONFIG_FILE);
            List<Element> list = (List<Element>) doc.getRootElement().getChildren("listener");
            for (Element element : list) {
                String name = element.getChildText("listener-name");
                boolean enable = Boolean.valueOf(element.getChildText("listener-enable"));
                String classPath = element.getChildText("listener-class");
                String config = element.getChildText("listener-config");
                String description = element.getChildText("listener-description");
                if (enable) {
                    logger.info("初始化 " + name + ":" + description + "，配置文件位置：" + getSysPath() + config);
                    SysInitializeListener initListener = (SysInitializeListener) Class.forName(classPath).newInstance();
                    initListener.init(getSysPath() + config);
                }
            }    
        } catch(Exception e) {
            logger.error("初始化 " + INITIALIZE_CONFIG_FILE + " 配置信息出错！！！", e);
        }
        logger.info("系统启动完成..............");
    }

    /**
     * getSysPath:
     * <p>获取系统路径
     *
     * @return
     *
     * @since   v1.01
     */
    public String getSysPath() {
        return ResourceCenter.getInstance().getSysPath();
    }

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
