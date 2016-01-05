/**
 * <p>Description:host,including server and exchange device</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-27
 */

package com.afunms.polling.node;

import com.afunms.polling.node.Application;

public class URLConfigNode extends Application {

    /**
     * url:
     * <p>��ַ
     *
     * @since   v1.01
     */
    protected String url;

    /**
     * timeout:
     * <p>��ʱ
     *
     * @since   v1.01
     */
    protected int timeout;

    /**
     * getUrl:
     * <p>��ȡ��ַ
     *
     * @return  {@link String}
     *          - ��ַ
     * @since   v1.01
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl:
     * <p>���õ�ַ
     *
     * @param   url
     *          - ��ַ
     * @since   v1.01
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getTimeout:
     * <p>��ȡ��ʱ
     *
     * @return  {@link Integer}
     *          - ��ʱ
     * @since   v1.01
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setTimeout:
     * <p>���ó�ʱ
     *
     * @param   timeout
     *          - {@link Integer} ��ʱ
     * @since   v1.01
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}