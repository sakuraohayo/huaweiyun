package javaweb.remember.enumeration;

public enum ResultEnum {
    /**
     * 出现格式错误时，状态码统一为-100
     */
    SEND_VERIFICATION_CODE_SUCCESS(1,"验证码发送成功，5分钟内有效"),
    REGISTER_SUCCESS(2,"注册成功"),
    LOGIN_SUCCESS(3,"登录成功"),
    CHANGE_USERNAME_SUCCESS(4,"修改用户名成功"),
    CHANGE_BIRTHDAY_SUCCESS(5,"修改生日成功"),
    CHANGE_SIGNATURE_SUCCESS(6,"修改个性签名成功"),
    GET_USER_INFO_SUCCESS(7,"获取用户信息成功"),
    CHANGE_PASSWORD_SUCCESS(8,"修改密码成功"),
    REMEMBER_PUBLISH_SUCCESS(11,"记忆发布成功"),
    MEMORY_SHOW_SUCCESS(12,"记忆显示成功"),
    TAGS_GET_SUCCESS(15,"标签集合获取成功"),
    RANDOM_MEMORY_SUCCESS(20,"随机获取记忆成功"),
    GET_ALL_MEMORY_SUCCESS(21,"获取所有记忆成功"),

    SEND_VERIFICATION_CODE_FAIL(-1,"验证码发送失败"),
    NO_LOGIN(-2,"请先登录"),
    VERIFICATION_CODE_FAILURE(-3,"验证码已失效，请重新获取"),
    VERIFICATION_CODE_INCORRECT(-4,"验证码错误，请核对后重新输入"),
    HAVE_REGISTERED(-5,"您已注册过I-Remember，请直接登录"),
    HAVE_NOT_REGISTERED(-6,"您还没有注册，请先注册"),
    PASSWORD_INCORRECT(-7,"密码错误"),
    CHANGE_USERNAME_FAIL(-8,"修改用户名失败"),
    CHANGE_BIRTHDAY_FAIL(-9,"修改生日失败"),
    CHANGE_SIGNATURE_FAIL(-10,"修改个性签名失败"),
    CHANGE_PASSWORD_FAIL(-11,"修改密码失败"),
    REMEMBER_PUBLISH_FAIL(-20,"记忆发布失败"),
    MEMORY_SHOW_FAIL(-21,"记忆显示失败"),
    PICTURE_TYPE_ERROR(-22,"非图片类型"),
    PICTURE_FILEFOLDER_NOT_EXIST(-23,"图片保存失败"),
    TAGS_GET_FAIL(-25,"标签集合获取失败"),
    ;
    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
