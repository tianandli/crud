package com.example.crud.controller;

import com.example.crud.common.response.BaseResponse;
import com.example.crud.model.entity.User;
import com.example.crud.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述：这个congtoller就相当于是登录接口了，主要的作用就是生成了一个token，token如下所示:
 * "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYxMTYyNjk5OSwiZXhwIjoxNjExNjM0MTk5fQ.shRYPQo8CvLNliTeKiEfJsctsYYfhA7xnm0wK03_3kr0D6JufvNfcnMZH0LbyTcTo2-A9RmiIGPmpDkkbZDuHA"
 * 其他接口登录的时候，需要携带这个token，前端需要把这个token放到headler里面
 *
 * @author: lijie
 * @date: 2021/1/26 10:02
 * @version: V1.0
 */
@RestController
public class JwtController {
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user) {
        // 判断账号密码是否正确，这一步肯定是要读取
        // 数据库中的数据来进行校验的，这里为了模拟就省去了
        if ("admin".equals(user.getUsername()) && "admin".equals(user.getPassword())) {
            // 如果正确的话就返回生成的token（注意哦，这里服务端是没有存储任何东西的）
            String generate = JwtUtil.generate(user.getUsername());//实际代码中，这里应该放userid
            return BaseResponse.SuccessInstance(generate);

        }
        return BaseResponse.ErrorInstance(400, "账号或密码错误");
    }
}
