package javaweb.remember.service;

/**
 * Remarks  : 验证码相关服务
 * File     : VerifyCodeService.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 */
public interface VerifyCodeService {

    /**
     * @param receiver 邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 1，邮件发送成功；0，邮件发送失败
     * */
    public int sendMail(String receiver, String subject, String content);
}
