package com.project.bookapp.repository.role;

import com.project.bookapp.model.Role;
import com.project.bookapp.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
