package com.web.blog.dao.user;

import java.util.List;

import com.web.blog.model.user.Review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDao extends JpaRepository<Review, Integer>{
    List<Review> findByPid(int pid);
    Review findByRvid(int rvid);
    List<Review> findTop6ByOrderByCreateDateDesc();
    Review findByPidAndRvid(int pid, int rvid);
    List<Review> findByEmail(String email);
    List<Review> findByEmail(String email, Pageable page);
}