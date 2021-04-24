package com.web.blog.dao.post;

import java.util.List;

import com.web.blog.model.post.PostList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostListDao extends JpaRepository<PostList, String> {
    List<PostList> findByEmail(String email);
    List<PostList> findByEmailAndFlagOrderByCreateDateDesc(String email, int flag);
    PostList findByPid(int pid);
    List<PostList> findByFlag(int flag);
    List<PostList> findByFlag(int flag, Pageable page);
    List<PostList> findByFlagOrderByCreateDateDesc(int flag);
    List<PostList> findByFlagOrderByCreateDateDesc(int flag, Pageable page);
    List<PostList> findByFlagAndActivityLikeOrderByCreateDateDesc(int flag, String word);
    List<PostList> findByFlagAndActivityLikeOrderByCreateDateDesc(int flag, String word, Pageable page);
    List<PostList> findByFlagAndTitleLikeOrderByCreateDateDesc(int flag, String word);
    List<PostList> findByFlagAndTitleLikeOrderByCreateDateDesc(int flag, String word, Pageable page);
    List<PostList> findByFlagAndPriceLessThanEqualOrderByCreateDateDesc(int flag, int word);
    List<PostList> findByFlagAndPriceLessThanEqualOrderByCreateDateDesc(int flag, int word, Pageable page);
    List<PostList> findTop4ByFlagOrderByLikecntDesc(int flag);
    
    List<PostList> findAllByOrderByLikecntDesc();
    
    List<PostList> findBySpringAndFlagOrderByCreateDateDesc(int spring, int flag);
    List<PostList> findBySummerAndFlagOrderByCreateDateDesc(int summer, int flag);
    List<PostList> findByAutumnAndFlagOrderByCreateDateDesc(int autumn, int flag);
    List<PostList> findByWinterAndFlagOrderByCreateDateDesc(int winter, int flag);
    List<PostList> findBySpringAndFlagOrderByCreateDateDesc(int spring, int flag, Pageable page);
    List<PostList> findBySummerAndFlagOrderByCreateDateDesc(int summer, int flag, Pageable page);
    List<PostList> findByAutumnAndFlagOrderByCreateDateDesc(int autumn, int flag, Pageable page);
    List<PostList> findByWinterAndFlagOrderByCreateDateDesc(int winter, int flag, Pageable page);
    
    List<PostList> findByPlaceAndFlagOrderByCreateDateDesc(String place, int flag);
    List<PostList> findByPlaceAndFlagOrderByCreateDateDesc(String place, int flag, Pageable page);
    
    List<PostList> findByFlagAndPlace(int flag, String place);
    List<PostList> findByFlagAndPriceLessThanEqual(int flag, int price);
    List<PostList> findByFlagAndLocationLike(int flag, String location);
    List<PostList> findByEmailAndFlag(String email, int flag);
    
    List<PostList> findByFlagAndPidInOrderByCreateDateDesc(int flag, List<Integer> pid, Pageable page);
    List<PostList> findByFlagAndPidInOrderByCreateDateDesc(int flag, List<Integer> pid);
    
    List<PostList> findByEmailAndFlag(String email, int flag, Pageable page);
    List<PostList> findByEmailAndFlagOrderByCreateDateDesc(String email, int flag, Pageable page);
}