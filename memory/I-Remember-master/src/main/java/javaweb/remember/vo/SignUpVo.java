package javaweb.remember.vo;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class SignUpVo {

    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 14)
    private String username;

    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6,max = 20)
    private String password;

    @NotNull(message = "验证码不能为空")
    @NotBlank(message = "验证码不能为空")
    private String code;

}
