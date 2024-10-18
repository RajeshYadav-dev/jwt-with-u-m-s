package com.dev.repository;

import com.dev.entity.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  OurUserRepository extends JpaRepository<OurUser,Long> {
    Optional<OurUser> findByUsername(String username);
}
