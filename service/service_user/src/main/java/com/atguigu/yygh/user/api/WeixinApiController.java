package com.atguigu.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.ConstantWxPropertiesUtils;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;

    // generate qrcode
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            map.put("redirect_url", URLEncoder.encode(ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL, "utf-8"));
            map.put("state", System.currentTimeMillis() + "");
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 微信扫码后回调的方法
    @GetMapping("callback")
    public String callback(String code, String state) {
        // 拿着code和微信id和密钥，请求微信固定地址，的两个值
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                code);

        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String accessToken = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");

            UserInfo userInfoExist = userInfoService.selectWxInfoOpenId(openId);
            if (userInfoExist == null) {
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%S";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);
                String userInfo = HttpClientUtils.get(userInfoUrl);
                jsonObject = JSONObject.parseObject(userInfo);

                // 用户昵称
                String nickname = jsonObject.getString("nickname");
                // 用户头像
                String headImgUrl = jsonObject.getString("headimgurl");

                // 获取扫码人信息添加数据库
                userInfoExist = new UserInfo();
                userInfoExist.setNickName(nickname);
                userInfoExist.setOpenid(openId);
                userInfoExist.setStatus(1);
                userInfoService.save(userInfoExist);
            }

            // 返回name和token
            Map<String, String> map = new HashMap<>();
            String name = userInfoExist.getName();
            if (StringUtils.isEmpty(name)) {
                name = userInfoExist.getNickName();
            }
            if (StringUtils.isEmpty(name)) {
                name = userInfoExist.getPhone();
            }
            map.put("name", name);

            // 前端做判断，如果openid不为空，需要绑定手机
            if (StringUtils.isEmpty(userInfoExist.getPhone())) {
                map.put("openid", userInfoExist.getOpenid());
            } else {
                map.put("openid", "");
            }

            String token = JwtHelper.createToken(userInfoExist.getId(), name);
            map.put("token", token);

            return "redirect:" + ConstantWxPropertiesUtils.YYGH_BASE_URL +
                    "/weixin/callback?token=" + map.get("token") + "&openid=" + map.get("openid")
                    + "&name" + URLEncoder.encode(map.get("name"), "utf-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
