package javaweb.remember.repository;

import javaweb.remember.entity.User;
import javaweb.remember.vo.UserInfoVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    @Modifying
    @Query(value = "update User user set user.username = ?2 where user.id = ?1")
    int updateUsername(Long id,String username);

    @Modifying
    @Query(value = "update User user set user.birthDay = ?2 where user.id = ?1")
    int updateBirthday(Long id,Date date);

    @Modifying
    @Query(value = "update User user set user.personalSignature = ?2 where user.id = ?1")
    int updatePersonalSignature(Long id, String personalSignature);

    @Modifying
    @Query(value = "update User user set user.password = ?2 where user.email = ?1")
    int updatePassword(String email, String newPassword);
}
