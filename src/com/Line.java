package com;

public class Line {

    private int length;

    private Node from;

    private Node to;

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the from
     */
    public Node getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Node from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public Node getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(Node to) {
        this.to = to;
    }
}
