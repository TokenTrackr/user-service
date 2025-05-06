package com.tokentrackr.user_service.repository;

import com.tokentrackr.user_service.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByKeycloakId(String keycloakId);

    boolean existsByUsername(String username);

}
