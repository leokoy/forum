package com.tensquare.recruit.dao;

import com.tensquare.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author
 * @ClassName: EnterpriseDao
 * @Description(描叙): 数据库访问层接口
 * @date 2019/8/14 11:28
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>, JpaSpecificationExecutor<Enterprise> {

    /**
     * 热门企业列表查询
     * @param
     * @return
     */
    List<Enterprise> findByIshot(String s);

}
