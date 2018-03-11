package com.renhe.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.renhe.Config.PaymentIntent;
import com.renhe.Config.PaymentMethod;
import com.renhe.domain.PaymentBean;
import com.renhe.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * service实现
 *
 * @author hcy
 */
@Service
public class PaypalServiceImpl implements PaypalService {

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(PaymentBean bean) throws PayPalRESTException {
        /**创建账目**/
        Amount amount = new Amount();
        amount.setCurrency(bean.getCurrency());
        amount.setTotal(String.format("%.2f", bean.getTotal()));
        /**创建交易**/
        Transaction transaction = new Transaction();
        transaction.setDescription(bean.getDescription());
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod(bean.getMethod().toString());
        Payment payment = new Payment();
        payment.setIntent(bean.getIntent().toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        /**创建重定向url**/
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(bean.getCancelUrl());
        redirectUrls.setReturnUrl(bean.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        /**设置支付项目和支付人**/
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}