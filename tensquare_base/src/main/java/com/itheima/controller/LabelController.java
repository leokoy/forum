package com.itheima.controller;

import com.itheima.pojo.Label;
import com.itheima.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

/**
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin //解决跨域
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 增
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK,"新建标签成功");
    }

    /**
     * 根据id删除标签
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"根据id删除标签成功");
    }


    /**
     * 根据id修改标签
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String labelId,@RequestBody Label label){
        labelService.updateById(labelId,label);
        return new Result(true, StatusCode.OK,"根据id修改标签成功");
    }


    /**
     * 根据id查询标签
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId){
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK,"根据id查询标签成功",label);
    }


    /**
     * 查询所有标签
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK,"查询所有标签成功",labelList);
    }

    /**
     *
     *条件查询标签信息
     *以后注意：可以使用Map更为灵活，当前使用Label没有问题
     *页面：开始时间  结束时间
     *实体类中只有一个 创建时间
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result search(@RequestBody Map<String,String> map){
        List<Label> labelList = labelService.search(map);
        return new Result(true, StatusCode.OK,"条件查询标签信息成功",labelList);
    }

    /**
     *
     条件查询标签分页
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findPage(@RequestBody Map<String,String> map,@PathVariable int page,@PathVariable int size){
        Page<Label> labelPage = labelService.findPage(map,page,size);
        return new Result(true, StatusCode.OK,"条件查询标签分页成功",new PageResult<>(labelPage.getTotalElements(),labelPage.getContent()));
    }

}
