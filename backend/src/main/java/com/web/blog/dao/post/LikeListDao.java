package com.web.blog.dao.post;

import com.web.blog.model.post.LikeList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeListDao extends JpaRepository<LikeList, String> {
    public LikeList findByNo(int no);
    public List<LikeList> findByEmailAndCart(String email, int cart);
    public LikeList findByEmailAndPidAndCart(String email, int pid, int cart);
    
    public List<LikeList> findByPid(int pid);
    
    public List<LikeList> findByNoIn(List<Integer> no);
    public List<LikeList> findByEmailAndCart(String email, int cart, Pageable page);
}