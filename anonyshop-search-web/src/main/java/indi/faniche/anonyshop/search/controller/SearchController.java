package indi.faniche.anonyshop.search.controller;

/* File:   ClassSearchController.java
 * -------------------------
 * Author: faniche
 * Date:   4/27/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.modol.ClassSearch;
import indi.faniche.anonyshop.service.SearchService;
import indi.faniche.anonyshop.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@CrossOrigin
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    SkuService skuService;

    /* 跳转分类搜索 */
    @RequestMapping(value = {"", "classSearch", "/"})
    @LoginRequired(loginSuccess = false)
    public String toClassSearch(Model model, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        PageInfo<PmsSkuInfo> skuInfoList = skuService.getClassSearchPageRandomList(1);
        model.addAttribute("skuInfoList", skuInfoList);
        return "search/catalog-page";
    }


    @RequestMapping("randomRet")
    public String randomRet(Integer pageNum, Model model) {
        PageInfo<PmsSkuInfo> skuInfoList = skuService.getClassSearchPageRandomList(pageNum);
        model.addAttribute("skuInfoList", skuInfoList);
        return "search/catalog-page::class-search-result";
    }

    /* 分类搜索结果 */
    @RequestMapping("class_search")
    public String classSearch(ClassSearch classSearch, HttpSession session, Model model) {
        List<PmsSkuInfo> skuInfos = searchService.classSearch(classSearch);
        sortSearchRetById(skuInfos);
        PageInfo<PmsSkuInfo> skuInfoList = getPage(skuInfos, 1);
        session.setAttribute("skuInfoList", skuInfoList);
        model.addAttribute("skuInfoList", skuInfoList);
        return "search/catalog-page::class-search-result";
    }

    @RequestMapping("classSearchList")
    public String classSearchList(HttpSession session, Integer pageNum, Model model) {
        List<PmsSkuInfo> skuInfos = (List<PmsSkuInfo>) session.getAttribute("skuInfoList");
        PageInfo<PmsSkuInfo> skuInfoList = getPage(skuInfos, pageNum);
        model.addAttribute("skuInfoList", skuInfoList);
        return "search/catalog-page::class-search-result";
    }

    /* 模糊搜索 */
    @RequestMapping("list")
    @LoginRequired(loginSuccess = false)
    public String ambSearch(String keyword, Model model) {
        List<PmsSkuInfo> skuInfoList = searchService.ambSearch(keyword);
        model.addAttribute("skuInfoList", skuInfoList);
        return "search/search-result";
    }

    private void sortSearchRetById(List<PmsSkuInfo> skuInfoList) {
        Collections.sort(skuInfoList, new Comparator<PmsSkuInfo>() {
            public int compare(PmsSkuInfo o1, PmsSkuInfo o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
    }

    private PageInfo<PmsSkuInfo> getPage(List<PmsSkuInfo> skuInfos, int pageNum) {
        int pageSize = 12;
        PageInfo<PmsSkuInfo> skuInfoList = new PageInfo<>(skuInfos);
        skuInfoList.setPageNum(pageNum);
        skuInfoList.setPageSize(pageSize);
        skuInfoList.setSize(pageSize);
        skuInfoList.setStartRow(1);
        skuInfoList.setEndRow(pageSize);
        int pages = skuInfos.size() / pageSize + ((skuInfos.size() % pageSize == 0) ? 0 : 1);
        skuInfoList.setPages(pages);
        skuInfoList.setNextPage(pages);
        skuInfoList.setIsFirstPage(true);
        skuInfoList.setIsLastPage(false);
        skuInfoList.setHasPreviousPage(false);
        skuInfoList.setHasNextPage(true);
        skuInfoList.setNavigatePages(8);
        int[] navPageNums = new int[pages];
        for (int i = 1; i <= pages; i++) navPageNums[i-1] = i;
        skuInfoList.setNavigatepageNums(navPageNums);
        skuInfoList.setNavigateFirstPage(1);
        skuInfoList.setNavigateLastPage(pages);
        skuInfoList.setTotal(skuInfos.size());

        int startIndex = (pageNum-1) * skuInfoList.getPageSize();
        int endIndex = ((pageNum == pages)?skuInfos.size():(startIndex + skuInfoList.getPageSize()));
        List<PmsSkuInfo> tmp = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++){
            tmp.add(skuInfos.get(i));
        }
        sortSearchRetById(tmp);
        skuInfoList.setList(tmp);
        return skuInfoList;
    }
}
