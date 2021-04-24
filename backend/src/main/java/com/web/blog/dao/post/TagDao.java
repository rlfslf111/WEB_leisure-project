package com.web.blog.dao.post;

import java.util.List;

import com.web.blog.model.post.Tag;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TagDao extends JpaRepository<Tag, String> {
    List<Tag> findByPid(int pid);
    List<Tag> findByTagnameIn(List<String> tags);
}