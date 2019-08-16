package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 吐槽控制层
 */
@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {

    //日志对象
    private Logger logger = LoggerFactory.getLogger(SpitController.class);

    @Autowired
    private SpitService spitService;

    /**
     * 增
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit) {
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "发布吐槽成功");
    }

    /**
     * 根据吐槽id删除吐槽信息
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId) {
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "删除吐槽成功");
    }


    /**
     * 根据吐槽id修改吐槽信息
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String spitId,@RequestBody Spit spit) {
        spitService.updateById(spitId,spit);
        return new Result(true, StatusCode.OK, "修改吐槽成功");
    }


    /**
     * 根据吐槽id查询吐槽信息
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        //日志级别 trace -->debug  -->info  -->warn -->error(最高级别)
        //开发使用debug  上线后info以上
        logger.info("根据吐槽id删除吐槽信息,id：：："+spitId);
        logger.error("根据吐槽id删除吐槽信息,id：：："+spitId);
        logger.warn("根据吐槽id删除吐槽信息,id：：："+spitId);
        logger.debug("根据吐槽id删除吐槽信息,id：：："+spitId);
        logger.trace("根据吐槽id删除吐槽信息,id：：："+spitId);
        Spit spit = null;
        try {
            spit = spitService.findById(spitId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        //输出 操作成功失败
        logger.info("删除吐槽成功，id:::"+spitId);
        return new Result(true, StatusCode.OK, "根据id查询吐槽成功",spit);
    }


    /**
     * 查询所有吐槽信息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Spit> spitList = spitService.findAll();
        return new Result(true, StatusCode.OK, "查询吐槽列表成功",spitList);
    }




    /**
     * 根据上级ID查询吐槽数据（分页）
     * @return
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result comment(@PathVariable String parentid,@PathVariable int page,@PathVariable int size) {
        Page<Spit> spitPage = spitService.comment(parentid,page,size);
        return new Result(true, StatusCode.OK, "根据上级ID查询吐槽列表成功",new PageResult<>(spitPage.getTotalElements(),spitPage.getContent()));
    }

    /***
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId) {
        spitService.thumbup(spitId);
        return new Result(true, StatusCode.OK, "吐槽点赞成功");
    }

}
