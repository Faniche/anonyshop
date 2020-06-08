package indi.faniche.anonyshop.manage.controller;

/* File:   CommentController.java
 * -------------------------
 * Author: faniche
 * Date:   5/15/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.comment.PmsComment;
import indi.faniche.anonyshop.modol.Comments;
import indi.faniche.anonyshop.service.CommentService;
import indi.faniche.anonyshop.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("comment")
public class CommentController {
    @Reference
    CommentService commentService;

    @Reference
    StoreService storeService;

    @RequestMapping("write")
    @LoginRequired
    @ResponseBody
    public String writeComment(HttpServletRequest request, Comments comments, Model model) {
        String username = (String) request.getAttribute("username");
        setUsernameAndcopyScore(comments, username);
        commentService.addCommment(comments);
        return JSON.toJSONString("感谢您的评论");
    }

    private void setUsernameAndcopyScore(Comments comments, String username) {
        List<PmsComment> pmsComments = comments.getComments();
        pmsComments.get(0).setMemberNickName(username);
        pmsComments.get(0).setCreateTime(new Date());
        pmsComments.get(0).setStar(getAverageScore(pmsComments.get(0)));
        if (pmsComments.size() > 1) {
            for (int i = 1; i < pmsComments.size(); i++) {
                pmsComments.get(i).setMemberNickName(username);
                pmsComments.get(i).setCreateTime(new Date());
                pmsComments.get(i).setServiceScore(pmsComments.get(0).getServiceScore());
                pmsComments.get(i).setDeliveryScore(pmsComments.get(0).getDeliveryScore());
                pmsComments.get(i).setStar(getAverageScore(pmsComments.get(i)));
            }
        }
        comments.setComments(pmsComments);
    }

    private String getAverageScore(PmsComment comment){
        int average = (comment.getProductScore() + comment.getProductScore() + comment.getDeliveryScore()) / 3;
        return Integer.toString(average);
    }
}
