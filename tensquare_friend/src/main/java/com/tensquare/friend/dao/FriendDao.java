package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友微服务接口
 */
public interface FriendDao extends JpaRepository<Friend,String>{
    /**
     * 根据登录用户id和好友id查询记录是否存在
     * @param userId
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid=?1 and f.friendid=?2")
    int selectCount(String userId, String friendid);

    /**
     * 根据登录用户id和好友id更新islike
     * @param userId
     * @param friendid
     */
    @Query("update Friend set islike = 1 where userid = ?1 and friendid = ?2")
    @Modifying
    void updateLike(String userId, String friendid);
}
