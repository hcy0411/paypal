package com.renhe.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付api
 *
 * @author hcy
 */
@RequestMapping("/")
public interface PaypalApi {

    /**
     * 支付首页
     * @author hcy
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    String index();

    /**
     * 创建支付链接
     * @author hcy
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "pay")
    String pay(HttpServletRequest request);

    /**
     * 支付操作
     * @author hcy
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "pay/success")
    String successPay(HttpServletRequest request);

}
