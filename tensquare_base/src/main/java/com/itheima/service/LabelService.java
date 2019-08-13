package com.itheima.service;

import com.itheima.dao.LabelDao;
import com.itheima.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 只有类，没有接口，业务比较简单，
 * 微服务之间调用 不用借助注入接口，借助spring cloud
 *
 * 标签业务层
 */
@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增
     */
    public void save(Label label) {
        label.setId(idWorker.nextId()+"");///设置id
        labelDao.save(label);
    }
    /**
     * 根据id删除标签
     */
    public void deleteById(String labelId) {

        labelDao.deleteById(labelId);
    }
    /**
     * 根据id修改标签
     */
    public void updateById(String labelId, Label label) {
        label.setId(labelId);
        labelDao.save(label);//在设置对象之前
    }
    /**
     * 根据id查询标签
     */
    public Label findById(String labelId) {
        return labelDao.findById(labelId).get();
    }
    /**
     * 查询所有标签
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 条件查询标签信息
     * @param map
     * @return
     */
    public List<Label> search(Map<String, String> map) {
        //通过匿名内部类拼接查询条件
        /*Specification<Label> spec = new Specification<Label>() {
            *//**
             * @param root 操作根对象
             * @param criteriaQuery 了解 顶级查询对象
             * @param criteriaBuilder 条件构建对象
             * @return
             *//*
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // "labelname": "string",  "state": "string",

                String labelname = map.get("labelname");//标签名称  模糊查询
                String state = map.get("state");//精确匹配
                //定一个list存放条件
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isEmpty(labelname)){
                    list.add(criteriaBuilder.like(root.get("labelname").as(String.class),"%"+labelname+"%")); // labelname like '%%'

                }
                if(!StringUtils.isEmpty(state)){
                    list.add(criteriaBuilder.equal(root.get("state").as(String.class),state)); // labelname ='xxx'
                }
                if(list == null || list.size() == 0){
                    return null;//没有条件
                }
                //将list转predicate数组
               *//* Predicate[] predicates = new Predicate[list.size()];
                Predicate[] newPredicates = list.toArray(predicates);
                return criteriaBuilder.and(newPredicates);*//*
               return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };*/
        return labelDao.findAll(getSpecification(map));
    }
    /**
     *
     条件查询标签分页
     */
    public Page<Label> findPage(Map<String, String> map, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(getSpecification(map),pageable);
    }

    /**
     * 公共的条件方法抽取
     * @return
     */
    public Specification getSpecification(Map<String, String> map){
       return new Specification<Label>() {
            /**
             * @param root 操作根对象
             * @param criteriaQuery 了解 顶级查询对象
             * @param criteriaBuilder 条件构建对象
             * @return
             */
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // "labelname": "string",  "state": "string",

                String labelname = map.get("labelname");//标签名称  模糊查询
                String state = map.get("state");//精确匹配
                //定一个list存放条件
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isEmpty(labelname)){
                    list.add(criteriaBuilder.like(root.get("labelname").as(String.class),"%"+labelname+"%")); // labelname like '%%'

                }
                if(!StringUtils.isEmpty(state)){
                    list.add(criteriaBuilder.equal(root.get("state").as(String.class),state)); // labelname ='xxx'
                }
                if(list == null || list.size() == 0){
                    return null;//没有条件
                }
                //将list转predicate数组
               /* Predicate[] predicates = new Predicate[list.size()];
                Predicate[] newPredicates = list.toArray(predicates);
                return criteriaBuilder.and(newPredicates);*/
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
    }
}
