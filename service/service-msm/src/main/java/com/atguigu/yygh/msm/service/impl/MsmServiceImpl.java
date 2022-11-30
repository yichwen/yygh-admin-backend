package com.atguigu.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.msm.utils.ConstantPropertiesUtils;
import com.atguigu.yygh.vo.msm.MsmVo;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) {

        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        // integrate with aliyun sms service
        DefaultProfile profile = DefaultProfile.getProfile(
                ConstantPropertiesUtils.REGION_ID,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.SECRET
        );
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "Atguigu");   // 签名名称
        request.putQueryParameter("TemplateCode", "SMS_45340857430");
        Map<String, String> param = new HashMap<>();
        param.put("code", code);
        request.putQueryParameter("TemplatePlatform", JSONObject.toJSONString(param));

        // invoke sms
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean send(MsmVo msmVo) {
        if (!StringUtils.isEmpty(msmVo.getPhone())) {
            return send(msmVo.getPhone(), msmVo.getParam());
        }
        return false;
    }

    private boolean send(String phone, Map<String, Object> param) {

        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        // integrate with aliyun sms service
        DefaultProfile profile = DefaultProfile.getProfile(
                ConstantPropertiesUtils.REGION_ID,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.SECRET
        );
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "Atguigu");   // 签名名称
        request.putQueryParameter("TemplateCode", "SMS_45340857430");
        request.putQueryParameter("TemplatePlatform", JSONObject.toJSONString(param));

        // invoke sms
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return false;
    }

}
