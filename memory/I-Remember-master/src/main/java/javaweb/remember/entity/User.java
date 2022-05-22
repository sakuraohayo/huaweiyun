package javaweb.remember.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Data
@Entity
public class User {
    /**
     * 用户id，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 生日（默认值为注册时间）
     */
    private Date birthDay = new Date(new java.util.Date().getTime());
    /**
     * 个性签名
     */
    private String personalSignature = "";
}
