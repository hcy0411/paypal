package com.renhe.domain;

import com.renhe.Config.PaymentIntent;
import com.renhe.Config.PaymentMethod;
import lombok.Data;

/**
 * 创建payment，请求bean
 * @author hcy
 */
@Data
public class PaymentBean {
    private Double total;
    private String currency;
    private PaymentMethod method;
    private PaymentIntent intent;
    private String description;
    private String cancelUrl;
    private String successUrl;
}
