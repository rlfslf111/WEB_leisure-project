package com.web.blog.controller.account;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.web.blog.dao.user.ReportDao;
import com.web.blog.dao.user.ReportUserDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.user.Report;
import com.web.blog.model.user.ReportUser;
import com.web.blog.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportDao reportDao;

    @Autowired
    ReportUserDao reportUserDao;

    @Autowired
    UserDao userDao;

    @PostMapping("/regist")
    @ApiOperation("신고 등록")
    public Object regist(@RequestBody Report request) throws SQLException, IOException {
        try {
            Report report = new Report();
            report.setReason(request.getReason());
            report.setEmail(request.getEmail());
            report.setRemail(request.getRemail());
            report.setPid(request.getPid());
            reportDao.save(report);

            return new ResponseEntity<>(report, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{page}")
    @ApiOperation("신고 리스트")
    public Object list(@PathVariable int page) throws SQLException, IOException {
        try {
            Page<Report> list = reportDao.findAll(PageRequest.of(page - 1, 10));
            for (Report r : list) {
                ReportUser ru = reportUserDao.findByEmail(r.getRemail());
                if (ru != null)
                    r.setCnt(ru.getCnt());
                else
                    r.setCnt(0);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/count")
    @ApiOperation("신고 리스트 카운트")
    public Object count() throws SQLException, IOException {
        List<Report> list = reportDao.findAll();
        for (Report r : list) {
            ReportUser ru = reportUserDao.findByEmail(r.getRemail());
            if (ru != null)
                r.setCnt(ru.getCnt());
            else
                r.setCnt(0);
        }
        return new ResponseEntity<>(list.size(), HttpStatus.OK);
    }
    
    @GetMapping("/detail/{rpid}")
    @ApiOperation("신고 상세정보")
    public Object detail(@PathVariable int rpid) throws SQLException, IOException {
        Report report = reportDao.findByRpid(rpid);
        return new ResponseEntity<>(report, HttpStatus.ACCEPTED);
    }

    @GetMapping("/cnt/{remail}")
    @ApiOperation("신고당한 사람 정보")
    public Object cnt(@PathVariable String remail) throws SQLException, IOException {
        ReportUser user = reportUserDao.findByEmail(remail);
        return new ResponseEntity<>(user.getCnt(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/cancel/{rpid}")
    @ApiOperation("사용자 신고 취소")
    public Object cancel(@PathVariable int rpid) throws SQLException, IOException {
        Report report = reportDao.findByRpid(rpid);
        reportDao.delete(report);
        return new ResponseEntity<>(report, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/dropUser/{rpid}")
    @ApiOperation("사용자 탈퇴 처리")
    public Object dropUser(@PathVariable int rpid) throws SQLException, IOException {
        try {
            Report report = reportDao.findByRpid(rpid);
            System.out.println(rpid);
            Optional<User> user = userDao.findUserByEmail(report.getRemail());
            System.out.println(report.getRemail());
            if (user.isPresent()) {
                ReportUser cur = reportUserDao.findByEmail(report.getRemail());
                if (cur == null) { // 신고당한 사람이 신고유저리스트에 없을 경우
                    ReportUser ruser = new ReportUser();
                    ruser.setEmail(report.getRemail());
                    ruser.setCnt(1);
                    ruser.setIsdrop(1);
                    reportUserDao.save(ruser);
                } else { // 있을 경우
                    cur.setCnt(cur.getCnt() + 1);
                    cur.setIsdrop(1);
                    reportUserDao.save(cur);
                }
                userDao.delete(user.get());
                reportDao.delete(report);
                return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/warnUser/{rpid}")
    @ApiOperation("사용자 경고 처리")
    public Object warnUser(@PathVariable int rpid) throws SQLException, IOException {
        try {
            Report report = reportDao.findByRpid(rpid);
            Optional<User> user = userDao.findUserByEmail(report.getRemail());
            if (user.isPresent()) {
                ReportUser cur = reportUserDao.findByEmail(report.getRemail());
                if (cur == null) { // 신고당한 사람이 신고유저리스트에 없을 경우
                    ReportUser ruser = new ReportUser();
                    ruser.setEmail(report.getRemail());
                    ruser.setCnt(1);
                    ruser.setIsdrop(0);
                    reportUserDao.save(ruser);
                } else { // 없을 경우
                    cur.setCnt(cur.getCnt() + 1);
                    cur.setIsdrop(0);
                    reportUserDao.save(cur);
                }
                reportDao.delete(report);
                return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reports/{email}")
    @ApiOperation("신고 여부 확인")
    public Object report(@PathVariable String email) throws SQLException, IOException {
        try {
            ReportUser user = reportUserDao.findByEmail(email);
            if (user != null) {
                return new ResponseEntity<>(user.getIsdrop(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(3, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}