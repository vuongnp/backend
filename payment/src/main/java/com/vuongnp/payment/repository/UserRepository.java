package com.vuongnp.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.vuongnp.payment.model.User;

import jakarta.persistence.LockModeType;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User>findOneWithLockingById(int id);
}
