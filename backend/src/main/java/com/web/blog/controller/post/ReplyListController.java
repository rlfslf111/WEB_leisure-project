package com.web.blog.controller.post;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.post.ReplyListDao;
import com.web.blog.dao.post.RereplyListDao;
import com.web.blog.model.post.ReplyList;
import com.web.blog.model.post.RereplyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reply")

public class ReplyListController {
    
    @Autowired
    ReplyListDao replyListDao;

    @Autowired
    RereplyListDao rereplyListDao;

    // 댓글

    @GetMapping("/list/{pid}")
    @ApiOperation(value ="댓글 목록")
    public List<ReplyList> selectAll(@PathVariable int pid) throws SQLException, IOException{
        List<ReplyList> list = new LinkedList<>();
        list = replyListDao.findByPid(pid);
        // list = replyListDao.findAll();
        return list;
    }

    @GetMapping("/rlist/{pid}/{rid}")
    @ApiOperation(value ="rid별 댓글 목록")
    public List<ReplyList> selectComment(@PathVariable int pid, @PathVariable int rid) throws SQLException, IOException{
        List<ReplyList> list = new LinkedList<>();
        list = replyListDao.findByPidAndRid(pid, rid);
        return list;
    }

    @PostMapping("/register")
    @ApiOperation(value = "댓글 등록")
    public Object register(@RequestBody ReplyList request) throws SQLException, IOException{
        try {
            ReplyList temp = new ReplyList();
            temp.setRid(request.getRid());
            temp.setPid(request.getPid());
            temp.setContent(request.getContent());
            temp.setNickname(request.getNickname());
              
            LocalDateTime time = LocalDateTime.now();
            temp.setCreateDate(time);
            replyListDao.save(temp);

            return temp;
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modify")
    @ApiOperation(value = "댓글 수정")
    public Object modify(@RequestBody ReplyList request) throws SQLException, IOException{
        try {
            ReplyList temp = replyListDao.findByRid(request.getRid());
            temp.setContent(request.getContent());       
            LocalDateTime time = LocalDateTime.now();
            temp.setCreateDate(time);
            replyListDao.save(temp);
            return temp;
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("delete/{rid}")
    @ApiOperation(value = "댓글 삭제")
    public Object delete(@PathVariable int rid) throws SQLException, IOException{
        ReplyList reply = replyListDao.findByRid(rid);
        if(reply != null){
            replyListDao.delete(reply);
            return "댓글 삭제";
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 대댓글

    @PostMapping("/reRegist")
    @ApiOperation("대댓글 작성")
    public Object reRegist(@RequestBody RereplyList request) throws SQLException, IOException {
        try {
            RereplyList list = new RereplyList();
            list.setRid(request.getRid());
            list.setNickname(request.getNickname());
            list.setContent(request.getContent());
            list.setCreateDate(LocalDateTime.now());
            rereplyListDao.save(list);

            return list;
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reList/{rid}")
    @ApiOperation("대댓글 리스트")
    public List<RereplyList> reList(@PathVariable int rid) throws SQLException, IOException {
        return rereplyListDao.findAllByRid(rid);
    }

    @PutMapping("/reModify")
    @ApiOperation("대댓글 수정")
    public Object reModify(@RequestBody RereplyList request) throws SQLException, IOException {
        try {
            RereplyList temp = rereplyListDao.findByRrid(request.getRrid());
            if(temp != null) {
                RereplyList newTemp = temp;
                newTemp.setRid(request.getRid());
                newTemp.setNickname(request.getNickname());
                newTemp.setContent(request.getContent());
                newTemp.setCreateDate(LocalDateTime.now());
                rereplyListDao.save(newTemp);

                return newTemp;
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/reDelete/{rrid}")
    @ApiOperation("대댓글 삭제")
    public Object reDelete(@PathVariable int rrid) throws SQLException, IOException {
        try {
            RereplyList temp = rereplyListDao.findByRrid(rrid);
            if(temp != null) {
                rereplyListDao.delete(temp);
                return "대댓글 삭제";
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}