package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    void savePaymentInfo(OrderInfo orderInfo, Integer paymentType);
    void paySuccess(String out_trade_no, Map<String, String> resultMap);
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);
}
