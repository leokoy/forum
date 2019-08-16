package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽业务层
 */
@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发布吐槽
     * @return
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());
        spit.setVisits(0);
        spit.setThumbup(0);
        spit.setShare(0);
        spit.setComment(0);
        spit.setState("0");//状态

        //当下级吐槽发布的时候（spit.getParentid()不为空）
        // 判断不为空 则为上级吐槽的回复数+1
        if(!StringUtils.isEmpty(spit.getParentid())){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));// where _id(上级吐槽id) = Parentid
            //更新点赞数
            Update update = new Update();
            update.inc("comment",1);//每次+1
            //Query query, Update update, String collectionName
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }
    /**
     * 根据吐槽id删除吐槽信息
     * @return
     */
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }
    /**
     * 根据吐槽id修改吐槽信息
     * @return
     */
    public void updateById(String spitId, Spit spit) {
        spit.set_id(spitId);
        spitDao.save(spit);
    }
    /**
     * 根据吐槽id查询吐槽信息
     * @return
     */
    public Spit findById(String spitId) {
        return  spitDao.findById(spitId).get();
    }
    /**
     * 查询所有吐槽信息
     * @return
     */
    public List<Spit> findAll() {
        return  spitDao.findAll();
    }
    /**
     * 根据上级ID查询吐槽数据（分页）
     *
     * 方法命名查询语句
     * @return
     */
    public Page<Spit> comment(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);//自动添加分页 select * from spit where partentid = xx limit 0,10
    }

    /**
     * 吐槽点赞
     * @param spitId
     */
    public void thumbup(String spitId) {
        //基于同一个人 同一个吐槽只能点赞一次
        String userid = "123456";//后续改为权限认证后 就会有此用户id了
        //从redis中判断用户是否已经点赞
        String redisRs = (String)redisTemplate.opsForValue().get("spit_"+userid+"_"+spitId);
        if(!StringUtils.isEmpty(redisRs)){
            System.out.println("已经点赞，无需重复操作");
            throw new RuntimeException("已经点赞，无需重复操作");
        }
        //更新条件封装
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));// where _id = xxx
        //更新点赞数
        Update update = new Update();
        update.inc("thumbup",1);//每次+1
        //Query query, Update update, String collectionName
        mongoTemplate.updateFirst(query,update,"spit");

        //记录点赞记录 redis key:spit+userid+spitId value:xx
        redisTemplate.opsForValue().set("spit_"+userid+"_"+spitId,"1");
    }
}
