package com.tensquare.qa.controller;

import java.util.List;
import java.util.Map;

import com.tensquare.qa.client.LabelClient;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param problem
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem) {
        Claims userClaims = (Claims) request.getAttribute("user_claims");
        if (userClaims == null || !userClaims.get("roles").equals("user")) {
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.update(problem);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        problemService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /*
    最新问答列表
    */
    @RequestMapping(value = "/newlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result newlist(@PathVariable String labelId, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Problem> problemPage = problemService.newlist(labelId, page, size);
        return new Result(true, StatusCode.OK, "最新问答列表查询成功", new PageResult<>(problemPage.getTotalElements(), problemPage.getContent()));
    }


    /*
    热门问答列表
    */
    @RequestMapping(value = "/hotlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result hotlist(@PathVariable String labelId, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Problem> problemPage = problemService.hotlist(labelId, page, size);
        return new Result(true, StatusCode.OK, "热门问答列表查询成功", new PageResult<>(problemPage.getTotalElements(), problemPage.getContent()));
    }

    /*
    等待回答列表*/
    @RequestMapping(value = "/waitlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result waitlist(@PathVariable String labelId, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Problem> problemPage = problemService.waitlist(labelId, page, size);
        return new Result(true, StatusCode.OK, "等待回答列表查询成功", new PageResult<>(problemPage.getTotalElements(), problemPage.getContent()));
    }


    @Autowired
    private LabelClient labelClient;

    /**
     * 问答微服务调用基础微服务
     * 根据标签id查询标签信息
     *
     * @param id
     */
    @RequestMapping(value = "/mylabel/{id}", method = RequestMethod.GET)
    public Result mylabel(@PathVariable String id) {
        return labelClient.findById(id);
    }

}
