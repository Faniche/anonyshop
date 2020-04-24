package indi.faniche.anonyshop.manage.controller;

/* File:   ImgController.java
 * -------------------------
 * Author: faniche
 * Date:   4/23/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.spu.PmsProductImage;
import indi.faniche.anonyshop.manage.util.PmsUploadUtil;
import indi.faniche.anonyshop.service.ProductImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("img")
public class ImgController {

    @Reference
    ProductImageService productImageService;

    @RequestMapping("getImgListBySpuId")
    @ResponseBody
    public List<PmsProductImage> getImgListBySpuId(String productId) {
        return productImageService.getImgListBySpuId(productId);
    }

    @RequestMapping("imgUpload")
    @ResponseBody
    public String imgUpload(@RequestParam("imgName") MultipartFile multipartFile, HttpSession session){
        // 将图片或者音视频上传到分布式的文件存储系统
        // 将图片的存储路径返回给页面
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        List<PmsProductImage> pmsProductImageList = (List<PmsProductImage>) session.getAttribute("pmsProductImageList");
        if (pmsProductImageList == null) {
            pmsProductImageList = new ArrayList<>();
        }
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setImgUrl(imgUrl);
        pmsProductImage.setImgName(multipartFile.getOriginalFilename());

        pmsProductImageList.add(pmsProductImage);
        session.setAttribute("pmsProductImageList", pmsProductImageList);
        return imgUrl;
    }

    /*====================================================================================*/
    /* sku管理 显示spu图片 */
    @RequestMapping("skuGetSpuImgs")
    public String skuGetSpuImgs(Model model, String productId) {
        List<PmsProductImage> pmsProductImages = getImgListBySpuId(productId);
        model.addAttribute("pmsProductImages", pmsProductImages);
        return "skumanage::showSpuImgArea";
    }

}
