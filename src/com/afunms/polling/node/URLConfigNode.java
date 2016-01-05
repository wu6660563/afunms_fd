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
     * <p>地址
     *
     * @since   v1.01
     */
    protected String url;

    /**
     * timeout:
     * <p>超时
     *
     * @since   v1.01
     */
    protected int timeout;

    /**
     * getUrl:
     * <p>获取地址
     *
     * @return  {@link String}
     *          - 地址
     * @since   v1.01
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl:
     * <p>设置地址
     *
     * @param   url
     *          - 地址
     * @since   v1.01
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getTimeout:
     * <p>获取超时
     *
     * @return  {@link Integer}
     *          - 超时
     * @since   v1.01
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setTimeout:
     * <p>设置超时
     *
     * @param   timeout
     *          - {@link Integer} 超时
     * @since   v1.01
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}