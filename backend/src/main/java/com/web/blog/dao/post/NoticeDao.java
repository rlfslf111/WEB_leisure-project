package com.web.blog.dao.post;

import java.util.List;

import com.web.blog.model.post.Notice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeDao extends JpaRepository<Notice, Integer>{
    Notice findByNid(int nid);

	List<Notice> findAllByOrderByCreateDateDesc();
}