package com.renhe.api.impl;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.renhe.Config.PaymentIntent;
import com.renhe.Config.PaymentMethod;
import com.renhe.api.PaypalApi;
import com.renhe.domain.PaymentBean;
import com.renhe.service.PaypalService;
import com.renhe.util.IPUtils;
import com.renhe.util.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 支付实现层
 *
 * @author hcy
 */
@Controller
public class PaypalApiImpl implements PaypalApi {
    private Logger logger = LoggerFactory.getLogger(PaypalApiImpl.class);
    private final String DEFAULT_URL = "redirect:/";
    private final String SUCCESS_URL = "success";
    private final String INDEX_URL = "index";
    @Autowired
    PaypalService paypalService;

    @Override
    public String index() {
        return INDEX_URL;
    }

    @Override
    public String pay(HttpServletRequest request) {
        try {
            IPUtils.getIpAddress(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (request == null) {
            return INDEX_URL;
        }
        String cancelUrl = URLUtils.getBaseURl(request) + "/pay/cancel";
        String successUrl = URLUtils.getBaseURl(request) + "/pay/success";
        try {
            Double money = Double.valueOf(request.getParameter("money"));
            if (money <= 0.00) {
                return INDEX_URL;
            }
            PaymentBean paymentBean = new PaymentBean();
            paymentBean.setCancelUrl(cancelUrl);
            paymentBean.setCurrency("USD");
            paymentBean.setTotal(money);
            paymentBean.setMethod(PaymentMethod.paypal);
            paymentBean.setIntent(PaymentIntent.sale);
            paymentBean.setSuccessUrl(successUrl);
            Payment payment = paypalService.createPayment(paymentBean);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            logger.warn("ERROR PaypalApiImpl.pay", e);
        }
        return DEFAULT_URL;
    }

    @Override
    public String successPay(HttpServletRequest request) {
        if (request == null) {
            return INDEX_URL;
        }
        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return SUCCESS_URL;
            }
        } catch (PayPalRESTException e) {
            logger.warn("ERROR PaypalApiImpl.successPay", e);
        }
        return DEFAULT_URL;
    }
}
