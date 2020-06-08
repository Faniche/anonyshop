package indi.faniche.anonyshop.modol;

/* File:   Msg.java
 * -------------------------
 * Author: faniche
 * Date:   5/8/20
 */

import java.io.Serializable;

public class Msg implements Serializable {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
