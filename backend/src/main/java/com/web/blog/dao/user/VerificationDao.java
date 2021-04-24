package com.web.blog.dao.user;

import java.util.List;

import com.web.blog.model.user.Verification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationDao extends JpaRepository<Verification, String>{
    Verification findByEmail(String email);
}