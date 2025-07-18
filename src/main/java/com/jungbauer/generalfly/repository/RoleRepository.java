package com.jungbauer.generalfly.repository;

import com.jungbauer.generalfly.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
