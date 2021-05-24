package com.example.crud.weixin;

import lombok.Data;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/2/8 9:33
 * @version: V1.0
 */
@Data
public class BaseResponse {

    private boolean success;
    private int errorCode;
    private String msg;
    private Object data;
    private int totalCount;

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
