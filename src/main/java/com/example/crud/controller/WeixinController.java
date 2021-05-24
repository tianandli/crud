package com.example.crud.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.crud.common.paramCheck.DataAdd;
import com.example.crud.common.response.BaseResponse;
import com.example.crud.model.entity.Data;
import com.example.crud.model.vo.UserContext;
import com.example.crud.service.DataService;
import com.example.crud.util.CheckParamUtil;
import com.example.crud.util.RedisUtil;
import com.example.crud.weixin.Global;
import com.example.crud.weixin.HttpUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：数据controller
 *
 * @author: lijie
 * @date: 2021/1/19 16:10
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/weixin")
public class WeixinController {


    /**
     * 客户端 发起微信扫码登录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/loginwx")
    public BaseResponse loginWx(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long l = System.currentTimeMillis();
        String TIMESTAMP = String.valueOf(l);
        String notify = Global.YUMING + "/api/busaccount/notify_login?timestamp=TIMESTAMP";//配置的回调地址，这个就是项目中的另一个接口
        String timestamp = notify.replaceAll("TIMESTAMP", TIMESTAMP);
        String urlEncode = URLEncoder.encode(timestamp, "utf-8");

        String uQR = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx434cdf890d37c36f&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
        String realUrl = uQR.replaceAll("REDIRECT_URI", urlEncode);

        HashMap<String, Object> map = new HashMap<>();
        map.put("qrImg", realUrl);
        map.put("TIMESTAMP", TIMESTAMP);
        return BaseResponse.SuccessInstance(map);
    }


    /**
     * 微信回调 pc版 微信扫码登录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notify_login")
    public void notify_login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-type", "text/html;charset=UTF-8");

        response.setCharacterEncoding("UTF-8");
        String timestamp = request.getParameter("timestamp");
        String code = request.getParameter("code");
        String openId = request.getParameter("openid");
        String loginName = request.getParameter("loginName");
        log.info("WeiXinLoginController ==> pcLoginByWeiXin(){} code: " + code);

        /** 错误信息 **/
        if (StringUtils.isBlank(code) && loginName == null) {
            return;
        }
        if (openId == null && code != null) {
            // 第一次进入界面，code不空，openid为空，根据code获取openid，然后查询是否存在用户信息。
            Map<String, String> accessTokenMap = getPcWXAccessToken(code); // 获取getWXAccessToken（微信网站PC扫码登录）
            /** 请求微信服务器错误 **/
            if (accessTokenMap.get("errcode") != null) {
                return;
            }
            openId = accessTokenMap.get("openid");

//            //根据openid查询用户表
//            TbBusAccount tbBusAccount = tbBusAccountService.queryByWechatopenid(openId);
//            if (tbBusAccount != null) {
//                //获取用户主键
//                Integer id = tbBusAccount.getId();
//                //根据时间戳查询中间表
//                TbWechatLogin tbWechatLogin = wechatLoginService.queryByTimestamp(timestamp);
//                if (tbWechatLogin != null) {
//                    // 中间表 更新这个时间戳下的用户id为 openid关联的 用户Id
//                    tbWechatLogin.setAccountid(id);
//                    wechatLoginService.saveOrUpdate(tbWechatLogin);
//                }
//            } else {
//                TbWechatLogin tbWechatLogin = wechatLoginService.queryByTimestamp(timestamp);
//                tbWechatLogin.setAccountid(-99);
//                wechatLoginService.saveOrUpdate(tbWechatLogin);
//            }

        }
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594380061047&di=486dd12bc58ab503e28ed6688c19b366&imgtype=0&src=http%3A%2F%2Fc.anywifi.com%2Ftheme%2Fzh_cn%2Fadmin%2Fwiki%2Fimages%2Fweixin_1.jpg";
        response.sendRedirect(url);
        return;
    }

    /**
     * 获取getPcWXAccessToken（微信网站PC扫码）
     */
    private Map<String, String> getPcWXAccessToken(String code) throws Exception {
        log.info("WeiXinLoginController ==> getPcWXAccessToken(){}");
        Map<String, String> resMap = new HashMap<String, String>();
        StringBuffer target = new StringBuffer();
        target.append("https://api.weixin.qq.com/sns/oauth2/access_token?").append("appid=").append("wx434cdf890d37c36f").append("&secret=").append("4e6f3e4b47e2c8f5bc89c86c36e88b7c")
                .append("&code=").append(code).append("&grant_type=authorization_code");
        log.info("WeiXinLoginController ==> getPcWXAccessToken(){} target: " + target);
        JSONObject jSONObject = HttpUtil.httpsRequest(target.toString(), "GET", null);
        log.info("WeiXinLoginController ==> getPcWXAccessToken(){} resMessageString: " + jSONObject);

        if (jSONObject != null && jSONObject.get("errcode") != null) { // 有错误码
            String errcode = String.valueOf(jSONObject.get("errcode"));
            String errmsg = String.valueOf(jSONObject.get("errmsg"));
            resMap.put("errmsg", errmsg);
            resMap.put("errcode", errcode);
        } else {
            String accessToken = String.valueOf(jSONObject.get("access_token"));
            String refreshToken = String.valueOf(jSONObject.get("refresh_token"));
            String openid = String.valueOf(jSONObject.get("openid"));
            String expiresIn = String.valueOf(jSONObject.get("expires_in"));
            String unionid = String.valueOf(jSONObject.get("unionid"));

            resMap.put("access_token", accessToken);
            resMap.put("refresh_token", refreshToken);
            resMap.put("openid", openid);
            resMap.put("expires_in", expiresIn);
            resMap.put("unionid", unionid);
        }
        return resMap;
    }
}
