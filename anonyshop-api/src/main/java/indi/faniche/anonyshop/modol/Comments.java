package indi.faniche.anonyshop.modol;

/* File:   Comments.java
 * -------------------------
 * Author: faniche
 * Date:   5/17/20
 */

import indi.faniche.anonyshop.bean.comment.PmsComment;

import java.io.Serializable;
import java.util.List;

public class Comments implements Serializable {
    private List<PmsComment> comments;

    public List<PmsComment> getComments() {
        return comments;
    }

    public void setComments(List<PmsComment> comments) {
        this.comments = comments;
    }
}
