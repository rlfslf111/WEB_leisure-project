package com.web.blog.dao.post;

import java.util.List;

import com.web.blog.model.post.ReplyList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyListDao extends JpaRepository<ReplyList, String> {
    List <ReplyList> findByPid(int pid);
    ReplyList findByRid(int rid);
    List<ReplyList> findByPidAndRid(int pid, int rid);
}