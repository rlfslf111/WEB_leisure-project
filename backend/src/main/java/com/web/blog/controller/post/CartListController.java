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
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/cart")
public class CartListController {
    
    @Autowired
    LikeListDao likeListDao;
    @Autowired
    PostListDao postListDao;
    
    @GetMapping("/list/{email}/{page}")
    @ApiOperation("장바구니 리스트")
    public Object selectAll(@PathVariable String email, @PathVariable int page) throws SQLException, IOException {
        try {
            List<LikeList> plist = new LinkedList<>();
            plist = likeListDao.findByEmailAndCart(email, 1, PageRequest.of(page - 1, 8));
            
            List<PostList> list = new LinkedList<>();
            for (LikeList likeList : plist) {
                list.add(postListDao.findByPid(likeList.getPid()));
            }
            
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/{email}")
    @ApiOperation("장바구니 리스트 개수")
    public Object countAll(@PathVariable String email) throws SQLException, IOException {
        try {
            List<LikeList> plist = new LinkedList<>();
            plist = likeListDao.findByEmailAndCart(email, 1);
    
            List<PostList> list = new LinkedList<>();
            for (LikeList likeList : plist) {
                list.add(postListDao.findByPid(likeList.getPid()));
            }
            return new ResponseEntity<>(list.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/regist/{email}/{pid}")
    @ApiOperation("장바구니 등록")
    public Object regist(@PathVariable String email, @PathVariable String pid) throws SQLException, IOException {
        try {
            LikeList list = new LikeList();
            list.setPid(Integer.parseInt(pid));
            list.setEmail(email);
            list.setCart(1);
            likeListDao.save(list);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/check/{email}/{pid}")
    @ApiOperation("장바구니 동일상품 확인")
    public Object check(@PathVariable String email, @PathVariable int pid) throws SQLException, IOException {
        LikeList like = likeListDao.findByEmailAndPidAndCart(email, pid, 1);

        try {
            if(like != null){ //이미 존재
                return new ResponseEntity<>(true, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/likelist/{email}")
    @ApiOperation("like 리스트")
    public Object selectLike(@PathVariable String email) throws SQLException, IOException {
        try {
            List<LikeList> list = new LinkedList<>();
            list = likeListDao.findByEmailAndCart(email, 1);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{no}")
    @ApiOperation("장바구니 삭제")
    public Object delete(@PathVariable List<Integer> no) throws SQLException, IOException {
        try {
            if(!likeListDao.findByNoIn(no).isEmpty()){
                likeListDao.deleteAll(likeListDao.findByNoIn(no));
                return new ResponseEntity<>("장바구니 삭제", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //0: 좋아요, 1: 장바구니
    @DeleteMapping("/deleteCart/{pid}/{email}/{cart}")
    @ApiOperation("마이페이지 장바구니 삭제")
    public Object delete(@PathVariable int pid, @PathVariable String email, @PathVariable int cart) {
        try {
            LikeList likeList = new LikeList();
            likeList = likeListDao.findByEmailAndPidAndCart(email, pid, cart);
            if(likeList != null) {
                likeListDao.delete(likeList);
                return new ResponseEntity<>("마이페이지 장바구니 삭제", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/preview/{no}")
    @ApiOperation("구매할 목록 미리보기")
    public Object preview(@PathVariable List<Integer> no) throws SQLException, IOException {
        try {
            List<LikeList> tlist = new LinkedList<>();
            tlist = likeListDao.findByNoIn(no);

            List<PostList> plist = new LinkedList<>();
            for (LikeList likeList : tlist) {
                plist.add(postListDao.findByPid(likeList.getPid()));
            }
            return new ResponseEntity<>(plist, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}