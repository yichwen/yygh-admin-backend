package com.atguigu.yygh.order.service;

import java.util.Map;

public interface WeixinService {
    Map createNative(Long orderId);
    Map<String, String> queryPayStatus(Long orderId);
    boolean refund(Long orderId);
}
