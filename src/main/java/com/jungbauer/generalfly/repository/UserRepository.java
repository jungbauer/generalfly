package com.jungbauer.generalfly.repository;

import com.jungbauer.generalfly.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
