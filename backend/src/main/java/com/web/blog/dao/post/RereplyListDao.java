package com.web.blog.dao.post;

import java.util.List;

import com.web.blog.model.post.RereplyList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RereplyListDao extends JpaRepository<RereplyList, Integer> {
    public List<RereplyList> findAllByRid(int rid);
    public RereplyList findByRrid(int rid);
}