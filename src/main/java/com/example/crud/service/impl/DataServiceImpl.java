package com.example.crud.service.impl;

import com.example.crud.common.response.BaseResponse;
import com.example.crud.mapper.DataMapper;
import com.example.crud.model.entity.Data;
import com.example.crud.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/1/19 16:25
 * @version: V1.0
 */
@Slf4j
@Service
@Transactional
public class DataServiceImpl implements DataService {

    @Autowired
    private DataMapper dataMapper;

    @Override
    public int insertData(Data data) {
        data.init();
        return dataMapper.insert(data);
    }

    @Override
    public List<Data> selectByPage(Data data) {
        return dataMapper.selectByPage(data);
    }

    @Override
    public List<Data> selectAll() {
        return dataMapper.selectAll();
    }
}
