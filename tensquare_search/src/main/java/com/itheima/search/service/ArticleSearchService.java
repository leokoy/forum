package com.itheima.search.service;

import com.itheima.search.dao.ArticleSearchDao;
import com.itheima.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

/**
 * 文章搜微服务
 */
@Service
@Transactional
public class ArticleSearchService {
    @Autowired
    private ArticleSearchDao articleSearchDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Article article) {
        article.setId(idWorker.nextId()+"");
        articleSearchDao.save(article);
    }

    /**
     * 文章搜索
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String keywords, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        //dao使用方式 操作es?
        return articleSearchDao.findByTitleOrContentLike(keywords,keywords,pageable);
    }
}
