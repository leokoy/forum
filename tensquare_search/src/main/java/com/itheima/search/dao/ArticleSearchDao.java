package com.itheima.search.dao;

import com.itheima.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {
    /**
     * 文章搜索
     * @param keywords
     * @param keywords1
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String keywords, String keywords1, Pageable pageable);
}
