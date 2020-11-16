package cn.itrip.controller;

import cn.itrip.util.WXPayConfig;
import cn.itrip.util.WXPayRequest;
import cn.itrip.util.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Controller
public class weChatPayController {

    @Resource
    WXPayConfig config;

    @RequestMapping("/Pay1")
    @ResponseBody
    public Object GetPay(String orderid, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        WXPayRequest request = new WXPayRequest(config);
        Map<String, String> map = new HashMap<>();
        map.put("body", config.getTitle());
        map.put("out_trade_no", orderid);
        map.put("total_fee", config.getMoney());
        map.put("notify_url", config.getNotifyUrl());
        Map<String, String> result = request.unifiedorder(map);
        return result;
    }

    @RequestMapping(value = "/Pay2", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void GetPay_1(HttpServletRequest request, HttpServletResponse response) {
        WXPayRequest wxPayRequest = new WXPayRequest(this.config);
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> params = null;
        try {
            InputStream inputStream;
            StringBuffer sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            params = WXPayUtil.xmlToMap(sb.toString());
            boolean flag = wxPayRequest.isResponseSignatureValid(params);
            if (flag) {
                String returnCode = params.get("return_code");
                if (returnCode.equals("SUCCESS")) {
                    String transactionId = params.get("transaction_id");
                    String outTradeNo = params.get("out_trade_no");
                    System.out.println("订单号：" + outTradeNo);
                    //业务操作
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    out.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>".getBytes());
                    out.flush();
                    out.close();
                } else {
                    String transactionId = params.get("transaction_id");
                    String outTradeNo = params.get("out_trade_no");
                    result.put("return_code", "FAIL");
                    result.put("return_msg", "支付失败");
                }
            } else {
                result.put("return_code", "FAIL");
                result.put("return_msg", "签名失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}