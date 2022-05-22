package javaweb.remember.service;

import javaweb.remember.entity.User;
import javaweb.remember.vo.UserInfoVo;

public interface UserService {
    User findByEmail(String email);

    void save(User user);

    boolean changeUsername(Long id, String username);

    boolean changeBirthday(Long id, String birthday);

    boolean changePersonalSignature(Long id, String personalSignature);

    UserInfoVo findUserInfoVoById(Long id);

    boolean changePassword(String email, String newPassword);

    String findEmailById(Long id);
}
