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
 * ϵͳ��ʼ������ϵͳ����ʱ���г�ʼ��������
 * @author Administrator
 *
 */
public class SysInitialize {

    /**
     * builder:
     * <p>���ڽ��� XML �ļ��� {@link SAXBuilder}
     *
     * @since   v1.01
     */
    private SAXBuilder builder;

    /**
     * INITIALIZE_CONFIG_FILE:
     * <p>��ʼ������
     *
     * @since   v1.01
     */
    private final static String INITIALIZE_CONFIG_FILE = "WEB-INF/classes/initialize-config.xml";

    /**
     * AbstractInitializeListener.java:
     * ���췽��
     *
     * @since   v1.01
     */
    public SysInitialize() {
        setBuilder(new SAXBuilder());
    }
    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(SysInitialize.class.getName());

    @SuppressWarnings({ "static-access", "unchecked" })
    public void init() {
        logger.info("ϵͳ�������������Ժ�...........");
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
                    logger.info("��ʼ�� " + name + ":" + description + "�������ļ�λ�ã�" + getSysPath() + config);
                    SysInitializeListener initListener = (SysInitializeListener) Class.forName(classPath).newInstance();
                    initListener.init(getSysPath() + config);
                }
            }    
        } catch(Exception e) {
            logger.error("��ʼ�� " + INITIALIZE_CONFIG_FILE + " ������Ϣ��������", e);
        }
        logger.info("ϵͳ�������..............");
    }

    /**
     * getSysPath:
     * <p>��ȡϵͳ·��
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
     * <p>��ȡ�ĵ�
     *
     * @param   configFile
     *          - �����ļ�
     * @return  {@link Document}
     *          - �ĵ�
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
