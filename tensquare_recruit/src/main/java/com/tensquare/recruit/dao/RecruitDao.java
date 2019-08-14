package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{
    /**
     * 推荐职位  state=2 + 时间排序=createtime desc  + top4
     * <p>
     * 0：关闭 1:开启  2：推荐
     */
    List<Recruit> findTop4ByStateOrderByCreatetimeDesc(String s);
    /**
     * 最新职位 state!=0 + 时间排序=createtime desc  + top12
     */
    List<Recruit> findTop12ByStateNotOrderByCreatetimeDesc(String s);
}
