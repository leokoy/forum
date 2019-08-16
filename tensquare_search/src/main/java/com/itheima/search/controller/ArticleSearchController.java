package com.itheima.search.controller;

import com.itheima.search.pojo.Article;
import com.itheima.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索微服务控制层代码
 */
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 添加文章 后续搜索才会有数据
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleSearchService.save(article);
        return  new Result(true, StatusCode.OK,"保存文章成功");
    }

    /**
     * 文章搜索
     * keywords：用户输入的关键字  page:当前页码 size:每页显示的记录数
     */
    @RequestMapping(value="/search/{keywords}/{page}/{size}",method= RequestMethod.GET)
    public Result findByTitleOrContentLike(@PathVariable String keywords, @PathVariable int page, @PathVariable int size){
        //用户输入的关键字 需要根据标题或内容 搜索，满足条件则返回
        Page<Article> articlePage = articleSearchService.findByTitleOrContentLike(keywords,page,size);
        return  new Result(true, StatusCode.OK,"文章搜索成功",new PageResult<>(articlePage.getTotalElements(),articlePage.getContent()));
    }
}
