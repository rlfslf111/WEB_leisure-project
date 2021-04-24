package com.web.blog.controller.post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.post.LikeListDao;
import com.web.blog.dao.post.PostListDao;
import com.web.blog.model.post.LikeList;
import com.web.blog.model.post.PostList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/like")
public class LikeListController {

    @Autowired
    LikeListDao likeListDao;
    @Autowired
    PostListDao postListDao;

    @GetMapping("/list/{email}/{page}")
    @ApiOperation("좋아요 리스트")
    public List<PostList> selectAll(@PathVariable String email, @PathVariable int page) throws SQLException, IOException {
        List<LikeList> plist = new LinkedList<>();
        plist = likeListDao.findByEmailAndCart(email,0);

        int start = (page - 1) * 8;
        int end = start + 8;

        if(end > plist.size()) {
            end = plist.size();
        }

        List<PostList> list = new LinkedList<>();
        for (LikeList likeList : plist) {
            list.add(postListDao.findByPid(likeList.getPid()));
        }

        List<PostList> tlist = new LinkedList<>();
        for (int i = start; i < end; i++) {
            tlist.add(list.get(i));
        }

        return tlist;
    }

    @GetMapping("/count/{email}")
    @ApiOperation("좋아요 리스트 카운트")
    public int count(@PathVariable String email) throws SQLException, IOException {
        List<LikeList> plist = new LinkedList<>();
        plist = likeListDao.findByEmailAndCart(email,0);

        int cnt = plist.size();

        return cnt;
    }

    @GetMapping("/regist/{email}/{pid}")
    @ApiOperation("좋아요 등록")
    public Object regist(@PathVariable String email, @PathVariable String pid) throws SQLException, IOException {
        try {
            LikeList list = new LikeList();
            list.setPid(Integer.parseInt(pid));
            list.setEmail(email);
            list.setCart(0);
            likeListDao.save(list);

            return list;
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/registDelete/{email}/{pid}")
    @ApiOperation("좋아요 등록 및 삭제")
    public Object registDelete(@PathVariable String email, @PathVariable String pid) throws SQLException, IOException {
        LikeList like = likeListDao.findByEmailAndPidAndCart(email, Integer.parseInt(pid),0);
        if (like == null) { // 등록
            try {
                LikeList list = new LikeList();
                list.setPid(Integer.parseInt(pid));
                list.setEmail(email);
                list.setCart(0);
                likeListDao.save(list);
                PostList post = postListDao.findByPid(Integer.parseInt(pid));
                post.setLikecnt(post.getLikecnt() + 1);
                post.setSdate(post.getSdate().plusDays(1));
                post.setEdate(post.getEdate().plusDays(1));

                postListDao.save(post);
                // return list;
                return "regist";
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else { // 삭제
            try {
                likeListDao.delete(likeListDao.findByNo(like.getNo()));
                PostList post = postListDao.findByPid(Integer.parseInt(pid));
                post.setLikecnt(post.getLikecnt() - 1);
                post.setSdate(post.getSdate().plusDays(1));
                post.setEdate(post.getEdate().plusDays(1));
                postListDao.save(post);
                // return like;
                return "del";
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

    @GetMapping("/check/{email}")
    @ApiOperation("좋아요 확인")
    public Object check(@PathVariable String email) throws SQLException, IOException {
        List<LikeList> like = likeListDao.findByEmailAndCart(email,0);
        List<Integer> likelist = new LinkedList<>();
        for(LikeList ll : like){
            likelist.add(ll.getPid());
        }
        return likelist;
    }

    @GetMapping("/checkpidlike/{email}/{pid}")
    @ApiOperation("포스트 좋아요 확인")
    public Object checkpidlike(@PathVariable String email, @PathVariable int pid) throws SQLException, IOException {
        LikeList like = likeListDao.findByEmailAndPidAndCart(email, pid, 0);
        if(like!=null){
            return new ResponseEntity<>(1, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }

    // @GetMapping("/cnt")
    // @ApiOperation("좋아요 개수")
    // public Object cnt() throws SQLException, IOException {
    //     List<PostList> postlist = postListDao.findByFlagOrderByCreateDateDesc(1);
    //     List<Integer> cntlist = new LinkedList<>();
    //     for(PostList post : postlist){
    //         List<LikeList> likelist = likeListDao.findByPid(post.getPid());
    //         cntlist.add(likelist.size());
    //     }
    //     return cntlist;
    // }

    @DeleteMapping("/delete/{no}")
    @ApiOperation("좋아요 삭제")
    public String delete(@PathVariable int no) throws SQLException, IOException {
        likeListDao.delete(likeListDao.findByNo(no));
        return "좋아요 삭제 완료";
    }
}