package com.example.crud.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.crud.common.paramCheck.DataAdd;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@lombok.Data
public class Data implements Serializable {
    /**
     * 注意：
     * 1、参数校验的时候，String类型的使用@NotBlank，其他类型的使用@NotNull
     * 2、带正则的必须加上message，否则提示信息会有误。
     * 3、每一条都要有groups，否者这条失效。
     * 4、只写max表示是0-max。 min和max表示min-max。
     */

    private Long id;

    @NotBlank(groups = {DataAdd.class})
    @Size(min = 1, max = 4, groups = {DataAdd.class})
    private String customerName;

    @NotBlank(groups = {DataAdd.class})
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机格式不正确", groups = {DataAdd.class})
    private String customerPhone;

    @NotNull(groups = {DataAdd.class})
    private BigDecimal price;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull(groups = {DataAdd.class})
    private Long companyId;

    @NotBlank(groups = {DataAdd.class})
    @Size(min = 1, max = 25, groups = {DataAdd.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "必须是文字、字母或数字", groups = {DataAdd.class})
    private String companyName;

    public void init() {
        this.createTime = new Date();
    }
}