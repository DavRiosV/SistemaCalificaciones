package com.drvservicios.api.reposotories;

import com.drvservicios.api.models.Role;
import com.drvservicios.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

    @Query("SELECT u.roles FROM User u WHERE u.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);

    boolean existsByUsername(String username); 
}
