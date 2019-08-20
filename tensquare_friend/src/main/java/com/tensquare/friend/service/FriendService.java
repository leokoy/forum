package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交友微服务业务层
 */
@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    /**
     * 添加好友
     * @param userId
     * @param friendid
     */
    @Transactional
    public void like(String userId, String friendid) {
        //1.判断当前登录用户是否已经关注此好友
        int count = friendDao.selectCount(userId,friendid);
        //2.已经关注，抛出已经关注此好友的异常
        if(count > 0 ){
            throw new RuntimeException("已经关注，无需重复关注");
        }
        //3.未关注，直接往好友表插入一条记录
        Friend friend=new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //4.如果对方已经关注登录的用户
        int count2 = friendDao.selectCount(friendid,userId);
        if(count2>0){
            //5.更新islike=1
            friendDao.updateLike(userId,friendid);
            friendDao.updateLike(friendid,userId);
        }

    }

    /**
     * 添加非好友
     * @param userId
     * @param friendid
     */
    public void nolike(String userId, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setFriendid(friendid);
        noFriend.setUserid(userId);
        noFriendDao.save(noFriend);
    }
}
