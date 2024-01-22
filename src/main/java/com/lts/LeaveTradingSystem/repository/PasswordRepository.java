package com.lts.LeaveTradingSystem.repository;

import com.lts.LeaveTradingSystem.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password,String> {
}
