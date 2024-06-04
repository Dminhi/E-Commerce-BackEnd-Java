package com.ra.repository;

import com.ra.model.entity.Category;
import com.ra.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    @Query(value = "select u.* from User u join user_role ul on u.id = ul.userId join Role r on ul.roleId = r.id where r.roleName = 'ROLE_USER'",nativeQuery = true)
    List<User> findAllByRoleUser(Pageable pageable);

    Optional<User> findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "update User set status = not status where id = :id",nativeQuery = true)
    void changeStatus(@Param("id") Long id);
    List<User> findAllByUsernameContains(String search);

    boolean existsByPhone(String phone);
}
