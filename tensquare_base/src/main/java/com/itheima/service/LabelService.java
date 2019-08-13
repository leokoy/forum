package com.itheima.service;

import com.itheima.dao.LabelDao;
import com.itheima.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.List;

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
}
