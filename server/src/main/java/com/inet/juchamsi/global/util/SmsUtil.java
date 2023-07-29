package com.inet.juchamsi.global.util;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSercretKey;
    @Value("${coolsms.phone.number}")
    private String phoneNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSercretKey, "https://api.coolsms.co.kr");
    }

    //  메세지 발송
    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
        Message message = new Message();
        //  발신번호 및 수신번호는 01012345678형태
        message.setFrom(phoneNumber);
        message.setTo(to);
        message.setText("[주참시 인증번호] 아래의 인증번호를 입력해주세요\n" + "[" + verificationCode + "]");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }
}
