package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 最新问答列表
     * @param labelId
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) order by replytime desc")
    Page<Problem> newlist(String labelId, Pageable pageable);
    /**
     *  热门问答列表
     * @param labelId
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) order by reply desc")
    Page<Problem> hotlist(String labelId, Pageable pageable);
    /**
     * 等待回答列表 reply=0
     * @param labelId
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) and reply = 0 order by createtime desc")
    Page<Problem> waitlist(String labelId, Pageable pageable);
}
