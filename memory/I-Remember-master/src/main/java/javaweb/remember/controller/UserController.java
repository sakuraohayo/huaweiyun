package javaweb.remember.controller;

import javaweb.remember.entity.User;
import javaweb.remember.enumeration.ResultEnum;
import javaweb.remember.service.RedisService;
import javaweb.remember.service.UserService;
import javaweb.remember.service.VerifyCodeService;
import javaweb.remember.utils.RandomNumber;
import javaweb.remember.vo.ResultVo;
import javaweb.remember.vo.SignUpVo;
import javaweb.remember.vo.UserInfoVo;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Remarks  : 用户相关请求
 * File     : UserController.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 */
@RestController
@Validated
public class UserController {
    @Value("${email.subject}")
    private String subject;
    @Value("${email.content-start}")
    private String content_start;
    @Value("${email.content-end}")
    private String content_end;

    @Autowired
    UserService userService;
    @Autowired
    VerifyCodeService verifyCodeService;
    @Autowired
    private RedisService redisService;

    //设置验证码过期时间为5分钟（300秒）
    private static final Long VERIFICATION_CODE_EXPIRE_TIME = 300L;

    //设置用户token过期时间为一个小时（3600秒）
    private static final Long USER_TOKEN_EXPIRE_TIME = 3600L;

    /**
     * 通过接收email参数发送验证码
     *
     * @param email 待收取验证码的邮箱
     * @return 返回Json格式结果
     */
    @PostMapping("/verify_code")
    public ResultVo sendVerifyCode(@RequestParam("email")
                                   @NotNull(message = "邮箱不能为空")
                                   @NotBlank(message = "邮箱不能为空")
                                   @Email(message = "邮箱格式错误") String email) {
        //生成验证码
        String code = RandomNumber.createRandomCode(6);
        try {
            //先删除已有的对应的验证码，然后写进redis数据库，并设置过期时间
            redisService.delete(email);
            redisService.set(email, code);
            redisService.expire(email, VERIFICATION_CODE_EXPIRE_TIME);
            //发送邮件
            if (verifyCodeService.sendMail(email, subject, content_start + code + content_end) == 1) {
                return new ResultVo(ResultEnum.SEND_VERIFICATION_CODE_SUCCESS);
            }
        } catch (Exception e) {
            if (redisService.get(email) != null) {
                redisService.delete(email);
            }
        }
        return new ResultVo(ResultEnum.SEND_VERIFICATION_CODE_FAIL);
    }

    /**
     * 用户注册
     * @param signUpVo 注册界面VO，包含用户名，密码，邮箱，验证码
     * @return 返回封装类
     */
    @PostMapping(value = "/sign_up")
    public ResultVo userSignUp(@RequestBody SignUpVo signUpVo) {
        String code = redisService.get(signUpVo.getEmail());
        //用户名已注册
        if (userService.findByEmail(signUpVo.getEmail()) != null) {
            return new ResultVo(ResultEnum.HAVE_REGISTERED);
        }
        //验证码失效
        if (code == null) {
            return new ResultVo(ResultEnum.VERIFICATION_CODE_FAILURE);
        }
        //验证码错误
        if (!code.equals(signUpVo.getCode())) {
            return new ResultVo(ResultEnum.VERIFICATION_CODE_INCORRECT);
        }
        //注册成功
        //删除redis中的验证码
        redisService.delete(signUpVo.getEmail());
        User user = new User();
        user.setEmail(signUpVo.getEmail());
        user.setUsername(signUpVo.getUsername());
        user.setPassword(signUpVo.getPassword());
        userService.save(user);

        return new ResultVo(ResultEnum.REGISTER_SUCCESS);
    }

    /**
     * 登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return 返回封装类
     */
    @PostMapping(value = "/login")
    public ResultVo userLogin(
            @RequestParam(name = "email")
            @Email @NotBlank(message = "邮箱不能为空")
            @NotNull(message = "邮箱不能为空") String email,
            @RequestParam(name = "password")
            @NotNull(message = "密码不能为空")
            @NotBlank(message = "密码不能为空")
            @Length(min = 6, max = 20) String password
    ) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ResultVo(ResultEnum.HAVE_NOT_REGISTERED);
        }
        if (!user.getPassword().equals(password)) {
            return new ResultVo(ResultEnum.PASSWORD_INCORRECT);
        }
        Map<String,String> returnMap = new HashMap<>();
        String token = UUID.randomUUID().toString();
        //若该用户已登录，删除redis中已有的token，写入新的token，并设置过期时间
        redisService.set(token, email);
        redisService.expire(token, USER_TOKEN_EXPIRE_TIME);
        returnMap.put("username",user.getUsername());
        returnMap.put("token",token);
        ResultVo resultVo = new ResultVo(ResultEnum.LOGIN_SUCCESS);
        resultVo.setData(returnMap);
        return resultVo;
    }

//    /**
//     * @param type 图片类型
//     * @return 图片，可以在浏览器直接输入URL访问
//     */
//    @GetMapping(value = "/image/{type}", produces = MediaType.IMAGE_JPEG_VALUE)
//    @ResponseBody
//    public byte[] getImage(
//            @PathVariable("type") String type) throws Exception {
//        return photoService.getImage(type, "test");
//    }

    /**
     * 获取用户信息
     * @param request Http请求
     * @return 返回封装类
     */
    @PostMapping("/userInfo")
    public ResultVo getUserInfo(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        UserInfoVo userInfoVo = userService.findUserInfoVoById(id);
        if (userInfoVo == null) {
            return new ResultVo(ResultEnum.HAVE_NOT_REGISTERED);
        }
        ResultVo resultVo = new ResultVo(ResultEnum.GET_USER_INFO_SUCCESS);
        resultVo.setData(userInfoVo);
        return resultVo;
    }

    /**
     * 修改用户名
     * @param request Http请求
     * @param username 新用户名
     * @return 返回封装类
     */
    @PostMapping("/username")
    public ResultVo changeUsername(HttpServletRequest request,
                                   @RequestParam("username")
                                   @NotBlank @NotNull String username) {
        Long id = (Long) request.getAttribute("id");
        if (userService.changeUsername(id, username)) {
            return new ResultVo(ResultEnum.CHANGE_USERNAME_SUCCESS);
        }
        return new ResultVo(ResultEnum.CHANGE_USERNAME_FAIL);
    }

    /**
     * 修改生日
     * @param request Http请求
     * @param birthday 新的生日 格式为XXXX-XX-XX
     * @return 返回封装类
     */
    @PostMapping("/birthday")
    public ResultVo changeBirthday(HttpServletRequest request,
                                   @RequestParam("birthday")
                                   @NotBlank @NotNull String birthday) {
        Long id = (Long) request.getAttribute("id");
        if (userService.changeBirthday(id, birthday)) {
            return new ResultVo(ResultEnum.CHANGE_BIRTHDAY_SUCCESS);
        }
        return new ResultVo(ResultEnum.CHANGE_BIRTHDAY_FAIL);
    }

    /**
     * 修改个性签名
     * @param request Http请求
     * @param personalSignature 新的个性签名
     * @return 返回封装类
     */
    @PostMapping("/personalSignature")
    public ResultVo changePersonalSignature(HttpServletRequest request,
                                            @RequestParam("personalSignature")
                                            @NotNull String personalSignature) {
        Long id = (Long) request.getAttribute("id");
        if (userService.changePersonalSignature(id, personalSignature)) {
            return new ResultVo(ResultEnum.CHANGE_SIGNATURE_SUCCESS);
        }
        return new ResultVo(ResultEnum.CHANGE_SIGNATURE_FAIL);
    }

    /**
     * 修改密码
     * @param request Http请求
     * @param email 要修改密码的邮箱
     * @param newPassword 新的密码
     * @param code 验证码
     * @return 返回封装类
     */
    @PostMapping("/password")
    public ResultVo changePassword(HttpServletRequest request,
                                   @RequestParam(name = "email")
                                   @Email @NotBlank(message = "邮箱不能为空")
                                   @NotNull(message = "邮箱不能为空") String email,
                                   @RequestParam("newPassword")
                                   @Length(min = 6,max = 20)
                                   @NotNull @NotBlank String newPassword,
                                   @RequestParam("code")
                                   @NotNull @NotBlank String code) {
        User user = userService.findByEmail(email);
        if(user == null){
            return new ResultVo(ResultEnum.HAVE_NOT_REGISTERED);
        }
        String verificationCode = redisService.get(email);
        //验证码失效
        if (verificationCode == null) {
            return new ResultVo(ResultEnum.VERIFICATION_CODE_FAILURE);
        }
        //验证码错误
        if (!verificationCode.equals(code)) {
            return new ResultVo(ResultEnum.VERIFICATION_CODE_INCORRECT);
        }
        //验证码正确，删除redis中的验证码
        redisService.delete(email);
        if (userService.changePassword(email, newPassword)) {
            return new ResultVo(ResultEnum.CHANGE_PASSWORD_SUCCESS);
        }
        return new ResultVo(ResultEnum.CHANGE_PASSWORD_FAIL);
    }

}
