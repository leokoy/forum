package com.tensquare.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.tensquare.user.pojo.Admin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加（后台管理系统 - 管理员在后台添加的用户）
     *
     * @param user
     */
    public void add(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 发送验证码
     *
     * @param mobile
     */
    public void sendsms(String mobile) {
        int min = 100000;
        int max = 999999;
        //1.生成验证码 6位验证码
        Random random = new Random();
        int randomNum = random.nextInt(max);//不一定是6
        if (randomNum < min) {
            randomNum = randomNum + min;//一定是6位
        }

        //创建消息对象 短信接口？手机号码+验证码
        Map<String, String> map = new HashMap<>();
        map.put("code", randomNum + "");
        map.put("mobile", mobile);
        //2.将发送短信请求消息写入消息队列
        rabbitTemplate.convertAndSend("sms", map);
        //3.将验证码记录redis 10分钟过期
        redisTemplate.opsForValue().set("code_"+mobile,randomNum+"",10, TimeUnit.MINUTES);
        System.out.println("验证码：：：："+randomNum+":::::手机号码：：：：：："+mobile);
    }


    /**
     * 注册用户
     * @param map
     * @param code
     */
    public void register(Map<String, Object> map, String code) {
        //1.验证验证码是否正确
        String redisCode = (String)redisTemplate.opsForValue().get("code_" + map.get("mobile"));
        if(StringUtils.isEmpty(redisCode)|| StringUtils.isEmpty(code) || !redisCode.equals(code)){
            //抛出异常
            throw new RuntimeException("验证码错误");
        }
        //2.将注册信息写入数据
        User user = new User();
        user.setId(idWorker.nextId()+"");
        user.setNickname((String)map.get("nickname"));
        user.setPassword(encoder.encode((String)map.get("password")));
        user.setMobile((String)map.get("mobile"));
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
        userDao.save(user);

        //1.注册成功后删除验证码  2.手机号码要保证唯一
    }

    /**
     * 用户登录
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        //根据用户名查询用户对象
        User user = userDao.findByMobile(mobile);
        if(user == null){
            return null;
        }
        //第一个参数 用户输入的密码  第二个参数是数据库中加密的密码
        if(encoder.matches(password,user.getPassword())){
            return user;
        }
        return null;
    }
}
