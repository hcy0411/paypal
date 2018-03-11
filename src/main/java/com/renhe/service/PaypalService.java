package com.renhe.service;


import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.renhe.domain.PaymentBean;

/**
 * 支付服务
 *
 * @author hcy
 */
public interface PaypalService {

    /**
     * 创建支付请求
     *
     * @param payment
     * @return
     * @throws PayPalRESTException
     * @author hcy
     */
    Payment createPayment(PaymentBean payment) throws PayPalRESTException;

    /**
     * 执行支付操作
     *
     * @param paymentId
     * @param payerId
     * @return
     * @throws PayPalRESTException
     * @author hcy
     */
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}