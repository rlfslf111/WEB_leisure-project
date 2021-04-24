package com.web.blog.dao.packaging;

import java.util.List;

import com.web.blog.model.packaging.Pack;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackDao extends JpaRepository<Pack, String> {
    List<Pack> findByEmail(String email);
    List<Pack> findByEmail(String email, Pageable page);
}