package indi.faniche.anonyshop.service;

/* File:   CommentService.java
 * -------------------------
 * Author: faniche
 * Date:   5/15/20
 */

import indi.faniche.anonyshop.bean.comment.PmsComment;
import indi.faniche.anonyshop.modol.Comments;

import java.util.List;

public interface CommentService {
    void addCommment(Comments comments);

    List<PmsComment> getComments(String productId, String storeId);
}
