package com.example.crud.service;

import com.example.crud.common.response.BaseResponse;
import com.example.crud.model.entity.Data;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/1/19 16:23
 * @version: V1.0
 */
public interface DataService {

    public int insertData(Data data);

    public List<Data> selectByPage(Data data);

    public List<Data> selectAll();
}
