package com.acimage.user.service.mail.impl;

import com.acimage.user.service.mail.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;

@Service
@Slf4j
public class MailServiceImpl implements MainService {
    /**
     * 注入邮件工具类
     */
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String sendMailer;

    /**
     * 发送纯文本邮件
     */
    @Override
    public void sendTextMailMessage(String to, String subject, String text) {

        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人
            mimeMessageHelper.setTo(to);
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容
            mimeMessageHelper.setText(text);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            log.info("发送邮件成功：{}->{} subject；{} text；{}", sendMailer, to, subject, text);

        } catch (MessagingException e) {
            log.error("发送邮件失败：{}->{} subject；{} text；{} error:{}", sendMailer, to, subject, text, e.getMessage());
        }
    }


    /**
     * 发送html邮件
     */
    @Override
    public void sendVerifyCodeMailMessage(String to, String code,int timeoutMinute) {

        String content = "acimage网站正在验证邮箱<br/>" +
                "验证码：" + code+"<br/>"+
                "有效时间"+timeoutMinute+"分钟";

        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人  1或多个
            mimeMessageHelper.setTo(to);
            //邮件主题
            mimeMessageHelper.setSubject("acimage网站验证邮箱");
            //邮件内容   true 代表支持html
            mimeMessageHelper.setText(content, true);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            log.info("发送验证码邮件成功：to:{} code；{} ",  to, code);

        } catch (MessagingException e) {
            log.error("发送验证码邮件失败：to:{} code；{} error:{}",  to, code,e.getMessage());
        }
    }

}
