package com.example.crud.controller;

import com.example.crud.common.paramCheck.DataAdd;
import com.example.crud.common.response.BaseResponse;
import com.example.crud.model.entity.Data;
import com.example.crud.model.vo.UserContext;
import com.example.crud.service.DataService;
import com.example.crud.util.CheckParamUtil;
import com.example.crud.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述：数据controller
 *
 * @author: lijie
 * @date: 2021/1/19 16:10
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private RedisUtil redisUtil;//这里需要注入一下这个工具类

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
    * 描述：查所有
    * @return java.util.List<com.example.crud.model.entity.Data>
    * @author lijie
    * @date 2021/1/19 16:34
    */
    @GetMapping("/selectAll")
    public BaseResponse selectAll() {
        List<Data> dataList = dataService.selectAll();
        //将第一条数据保存到redis
        redisUtil.set("data1", dataList.get(0));
        //将第一条数据保存到mongodb
        mongoTemplate.save(dataList.get(0));
        return BaseResponse.SuccessInstance(dataList, dataList.size());
    }

    /**
     * 描述：动态查询，带分页
     * @return java.util.List<com.example.crud.model.entity.Data>
     * @author lijie
     * @date 2021/1/19 16:34
     */
    @GetMapping("/selectByPage")
    public BaseResponse selectByPage(Data data, Integer pageNum, Integer pageSize) {
        //如果需要获取到当前登录的这个人，可以用下面的方法
        String currentUserName = UserContext.getCurrentUserName();
        log.info("当前用户登录是：{}", currentUserName);
        PageHelper.startPage(pageNum, pageSize);
        List<Data> dataList = dataService.selectByPage(data);

        Query query=new Query(Criteria.where("customerName").is("张三"));
        Data data1 =  mongoTemplate.findOne(query , Data.class);
        System.out.println(data1+"=======");
        return BaseResponse.SuccessInstance(dataList, dataList.size());
    }

    /**
     * 描述：增加数据
     * @param data data
     * @return com.example.crud.common.response.BaseResponse
     * @author lijie
     * @date 2021/1/20 16:28
     */
    @PostMapping("/insertData")
    public BaseResponse insertData(@RequestBody Data data) {
        /**
         * 描述：service层中使用spring 事务， 处理异常之后需要再次抛出 ，如果service处理了异常，那么事务就不会回滚。所以在controller处理异常
         */
        try {
            //参数校验
            String res = CheckParamUtil.checkparam(data, DataAdd.class);
            if (res != null) {
                return BaseResponse.ErrorInstance(500, res);
            }
            dataService.insertData(data);
        } catch (Exception e) {
            log.error("系统异常，异常信息是：{}", e.getMessage());
            e.printStackTrace();
            return BaseResponse.ErrorInstance(500, "系统异常,数据插入失败");
        }
        return BaseResponse.SuccessInstance(null);
    }


}
