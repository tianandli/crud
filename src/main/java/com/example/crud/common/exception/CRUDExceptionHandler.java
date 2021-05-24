//package com.example.crud.common.exception;
//
//import com.example.crud.common.response.BaseResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tomcat.util.ExceptionUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * 功能描述：
// *
// * @author: lijie
// * @date: 2021/1/20 10:50
// * @version: V1.0
// */
//@Slf4j
//@RestControllerAdvice
//public class CRUDExceptionHandler {
//
////    //运行时异常
////    @ExceptionHandler(Throwable.class)
////    public BaseResponse runtimeExceptionHandler(RuntimeException ex) {
////        log.error("运行时异常，异常信息是：{}", ex.getMessage());  // 记录错误信息
////        return BaseResponse.ErrorInstance(1001,"运行时异常");
////    }
////
////    //空指针异常
////    @ExceptionHandler(NullPointerException.class)
////    public BaseResponse nullPointerExceptionHandler(NullPointerException ex) {
////        log.error("空指针异常，异常信息是：{}", ex.getMessage());  // 记录错误信息
////        return BaseResponse.ErrorInstance(1002,"空指针异常");
////    }
//
//
//
//    //处理全局未考虑到的异常
//    @ExceptionHandler(CRUDException.class)
//    @ResponseBody
//    public BaseResponse crudExceptionHandler(CRUDException ex) {
//        log.error("业务异常，异常信息是：{}", ex.getMessage());  // 记录错误信息
//        return BaseResponse.ErrorInstance(500,"系统业务异常");
//    }
//
//    //处理全局未考虑到的异常
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public BaseResponse otherExceptionHandler(Exception ex) {
//        log.error("其他异常，异常信息是：{}", ex.getMessage());  // 记录错误信息
//        return BaseResponse.ErrorInstance(500,"系统内部异常");
//    }
//}
