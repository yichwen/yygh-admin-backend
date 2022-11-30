package com.atguigu.yygh.order.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantWxPropertiesUtils implements InitializingBean {

    @Value("${wx.pay.appid}")
    private String appId;
    @Value("${wx.pay.partner")
    private String partner;
    @Value("${wx.pay.partnerKey}")
    private String partnerKey;
    @Value("${wx.pay.cert}")
    private String cert;

    public static String WX_APP_ID;
    public static String WX_PARTNER;
    public static String WX_PARTNER_KEY;
    public static String WX_CERT;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_APP_ID = appId;
        WX_PARTNER = partner;
        WX_PARTNER_KEY = partnerKey;
        WX_CERT = cert;
    }

}
