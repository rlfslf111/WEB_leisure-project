
package com.web.blog.dao.user;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.user.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {
    User getUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNickname(String nickname);

    User findUserByEmailAndName(String email, String name);
    
    User findByUid(int uid);

    List<User> findByUidNot(int uid);
    List<User> findByUidNot(int uid, Pageable page);

    User findByNickname(String nickname);
}
