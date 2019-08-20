package com.itheima.sms;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;
/**
 * 短信监听类
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private  SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;//模板编号

    @Value("${aliyun.sms.sign_name}")
    private String signName;//签名


    /**
     * 处理消息
     */
    @RabbitHandler
    public void sendMessage(Map<String, String> messaageMap){
        System.out.println("验证码"+messaageMap.get("code")+"::::手机号码：：："+messaageMap.get("mobile"));
        try {
            smsUtil.sendSms((String)messaageMap.get("mobile"),templateCode,signName,"{\"code\":"+ messaageMap.get("code") +"}");
            System.out.println("发送短信成功了。。。。");
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("发送短信失败了。。。。");
        }

    }
}
