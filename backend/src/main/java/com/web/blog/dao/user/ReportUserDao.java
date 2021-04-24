package com.web.blog.dao.user;

import java.util.List;

import com.web.blog.model.user.ReportUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportUserDao extends JpaRepository<ReportUser, String>{
    ReportUser findByEmail(String email);
}