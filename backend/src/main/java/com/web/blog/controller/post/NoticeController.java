package com.web.blog.controller.post;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.post.NoticeDao;
import com.web.blog.model.post.Notice;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/notice")
public class NoticeController {
    
    @Autowired
    NoticeDao noticeDao;

    //create
    @PostMapping("/regist")
    @ApiOperation("공지사항 등록")
    public Object regist(@RequestBody Notice request) throws SQLException, IOException {
        try {
            Notice notice = new Notice();
            notice.setTitle(request.getTitle());
            notice.setContent(request.getContent());
            notice.setVisit(request.getVisit());
            notice.setImportance(request.getImportance());
            LocalDateTime time = LocalDateTime.now();
            notice.setCreateDate(time);
            noticeDao.save(notice);
            return notice;
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //count
    @GetMapping("/count")
    @ApiOperation("공지사항 리스트")
    public int count() {
        List<Notice> list = new LinkedList<>();
        list = noticeDao.findAll();
        return list.size();
    }

    //read(paging)
    @GetMapping("/list/{page}")
    @ApiOperation("공지사항 리스트")
    public List<Notice> selectAll(@PathVariable int page) {
        List<Notice> list = new LinkedList<>();
        list = noticeDao.findAllByOrderByCreateDateDesc();

        int start = (page-1) * 10;
        int end = start + 10;

        if(end > list.size()) {
            end = list.size();
        }
        
        List<Notice> plist = new LinkedList<>();
        for (int i = start; i < end; i++) {
            plist.add(list.get(i));
        }

        return plist;
    }

    //detail
    @GetMapping("/detail/{nid}")
    @ApiOperation("공지사항 상세")
    public Object detail(@PathVariable int nid) throws SQLException, IOException {
        try {
            Notice notice = new Notice();
            notice = noticeDao.findByNid(nid);
            if(notice != null) {
                return new ResponseEntity<>(notice, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //visit++
    @PutMapping("/visitPlus/{nid}")
    @ApiOperation("조회수 증가")
    public int visitPlus(@PathVariable int nid) throws SQLException, IOException {
        Notice notice = new Notice();
        notice = noticeDao.findByNid(nid);
        int visit = notice.getVisit();
        visit += 1;
        notice.setVisit(visit);
        noticeDao.save(notice);

        return visit;
    }

    //update
    @PutMapping("/modify")
    @ApiOperation("공지사항 수정")
    public Object modify(@RequestBody Notice request) throws SQLException, IOException {
        try {
            Notice temp = noticeDao.findByNid(request.getNid());
            
            if(temp != null) {
                Notice nTemp = temp;
                nTemp.setTitle(request.getTitle());
                nTemp.setContent(request.getContent());
    
                // LocalDateTime time = LocalDateTime.now();
                // nTemp.setCreateDate(time);
    
                noticeDao.save(nTemp);

                return nTemp;
            } else {
                System.out.println("DB에 없음");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //delete
    @DeleteMapping("/delete/{nid}")
    @ApiOperation("공지사항 삭제")
    public Object delete(@PathVariable int nid) throws SQLException, IOException {
        Notice notice = noticeDao.findByNid(nid);
        if(notice != null) {
            noticeDao.delete(notice);
            return "삭제 완료";
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    } 
}