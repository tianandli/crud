package com.example.crud.mapper;

import com.example.crud.model.entity.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Data record);

    Data selectByPrimaryKey(Long id);

    List<Data> selectAll();

    int updateByPrimaryKey(Data record);

    List<Data> selectByPage(Data data);

}