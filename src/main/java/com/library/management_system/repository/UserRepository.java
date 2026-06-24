package com.library.management_system.repository;

import com.library.management_system.model.type.Role;
import com.library.management_system.model.User;
import com.library.management_system.model.embeddable.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(Username username);
    List<User> findByRole(Role role);
    boolean existsByUsername(Username username);
}
