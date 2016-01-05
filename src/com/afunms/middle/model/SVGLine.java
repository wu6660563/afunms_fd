/*
 * @(#)SVGLine.java     v1.01, 2013 12 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

public class SVGLine {

    /**
     * id:
     * <p>id
     *
     * @since   v1.01
     */
    private String id;

    /**
     * startSVGNode:
     * <p>开始节点
     *
     * @since   v1.01
     */
    private SVGNode startSVGNode;

    /**
     * endSVGNode:
     * <p>结束节点
     *
     * @since   v1.01
     */
    private SVGNode endSVGNode;

    /**
     * color:
     * <p>颜色
     *
     * @since   v1.01
     */
    private String color;

    /**
     * width:
     * <p>宽度
     *
     * @since   v1.01
     */
    private String width;

    /**
     * path:
     * <p>路径
     *
     * @since   v1.01
     */
    private String path;

    /**
     * getId:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getId() {
        return id;
    }

    /**
     * setId:
     * <p>
     *
     * @param   id
     *          -
     * @since   v1.01
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getStartSVGNode:
     * <p>
     *
     * @return  SVGNode
     *          -
     * @since   v1.01
     */
    public SVGNode getStartSVGNode() {
        return startSVGNode;
    }

    /**
     * setStartSVGNode:
     * <p>
     *
     * @param   startSVGNode
     *          -
     * @since   v1.01
     */
    public void setStartSVGNode(SVGNode startSVGNode) {
        this.startSVGNode = startSVGNode;
    }

    /**
     * getEndSVGNode:
     * <p>
     *
     * @return  SVGNode
     *          -
     * @since   v1.01
     */
    public SVGNode getEndSVGNode() {
        return endSVGNode;
    }

    /**
     * setEndSVGNode:
     * <p>
     *
     * @param   endSVGNode
     *          -
     * @since   v1.01
     */
    public void setEndSVGNode(SVGNode endSVGNode) {
        this.endSVGNode = endSVGNode;
    }

    /**
     * getColor:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getColor() {
        return color;
    }

    /**
     * setColor:
     * <p>
     *
     * @param   color
     *          -
     * @since   v1.01
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * getWidth:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getWidth() {
        return width;
    }

    /**
     * setWidth:
     * <p>
     *
     * @param   width
     *          -
     * @since   v1.01
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * getPath:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getPath() {
        return path;
    }

    /**
     * setPath:
     * <p>
     *
     * @param   path
     *          -
     * @since   v1.01
     */
    public void setPath(String path) {
        this.path = path;
    }

}

