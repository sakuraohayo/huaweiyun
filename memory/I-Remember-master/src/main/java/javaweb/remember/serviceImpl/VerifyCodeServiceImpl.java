package javaweb.remember.serviceImpl;

import javaweb.remember.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public int sendMail(String receiver, String subject, String content) {
        if(receiver.equals("")){
            return 0;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 发件人信息
            message.setFrom("www.1964085132@qq.com");
            message.setTo(receiver);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
