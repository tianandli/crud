package com.example.crud.common.response;

import lombok.Data;

/**
 * 功能描述：数据返回的公共类
 *
 * @author: lijie
 * @date: 2021/1/20 10:54
 * @version: V1.0
 */
@Data
public class BaseResponse {
    private boolean success;
    private Integer errorCode;
    private String msg;
    private Object data;
    private Integer totalCount;

    public static BaseResponse ErrorInstance(int errorCode, String msg) {
        BaseResponse resp = new BaseResponse();
        resp.success = false;
        resp.errorCode = errorCode;
        resp.msg = msg;
        return resp;
    }

    public static BaseResponse SuccessInstance(Object data) {
        BaseResponse resp = new BaseResponse();
        resp.success = true;
        resp.errorCode = 0;
        resp.msg = "请求成功！";
        resp.setData(data);
        return resp;
    }


    public static BaseResponse SuccessInstance(Object data, Integer totalCount) {
        BaseResponse resp = new BaseResponse();
        resp.success = true;
        resp.errorCode = 0;
        resp.msg = "请求成功！";
        resp.setTotalCount(totalCount);
        resp.setData(data);
        return resp;
    }
}
