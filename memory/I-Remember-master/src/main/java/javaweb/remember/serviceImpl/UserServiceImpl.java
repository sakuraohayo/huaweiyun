package javaweb.remember.serviceImpl;

import javaweb.remember.entity.User;
import javaweb.remember.repository.UserRepository;
import javaweb.remember.service.UserService;
import javaweb.remember.utils.DataBaseDateUtils;
import javaweb.remember.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.text.ParseException;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean changeUsername(Long id, String username) {
        int updated = userRepository.updateUsername(id,username);
        if(updated!=1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean changeBirthday(Long id, String birthday) {
        Date date = null;
        try {
            date = DataBaseDateUtils.String2Date(birthday);
        } catch (ParseException e) {
            return false;
        }
        int updated = userRepository.updateBirthday(id,date);
        if(updated!=1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean changePersonalSignature(Long id, String personalSignature) {
        int updated = userRepository.updatePersonalSignature(id,personalSignature);
        if(updated!=1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public UserInfoVo findUserInfoVoById(Long id) {
        UserInfoVo userInfoVo = new UserInfoVo();
        Optional<User> _user = userRepository.findById(id);
        if(_user.isPresent()){
            User user = _user.get();
            userInfoVo.setUsername(user.getUsername());
            userInfoVo.setEmail(user.getEmail());
            userInfoVo.setBirthday(DataBaseDateUtils.Date2String(user.getBirthDay()));
            userInfoVo.setPersonalSignature(user.getPersonalSignature());
            return userInfoVo;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean changePassword(String email, String newPassword) {
        int updated = userRepository.updatePassword(email,newPassword);
        if(updated!=1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public String findEmailById(Long id) {
        Optional<User> _user= userRepository.findById(id);
        if(_user.isPresent()){
           User user = _user.get();
           return user.getEmail();
        }
        return null;
    }
}
