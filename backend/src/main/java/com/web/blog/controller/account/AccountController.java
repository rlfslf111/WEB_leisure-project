package com.web.blog.controller.account;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.web.blog.jwt.JwtService;
import com.web.blog.dao.user.ReportUserDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.dao.user.VerificationDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.user.ReportUser;
import com.web.blog.model.user.User;
import com.web.blog.model.user.Verification;
import com.web.blog.service.FindUtil;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    UserDao userDao;

    @Autowired
    JwtService jwtService;

    @Autowired
    VerificationDao verificationDao;

    @Autowired
    ReportUserDao reportUserDao;

    @GetMapping("/login/{email}/{password}")
    @ApiOperation(value = "?????????")
    public Object login(@PathVariable String email, @PathVariable String password) throws SQLException, IOException {
        try {
            Optional<User> userOpt = userDao.findUserByEmailAndPassword(email, password);
            if (userOpt.isPresent()) {
                User tokenuser = new User();
                tokenuser.setEmail(userOpt.get().getEmail());
                tokenuser.setPassword(userOpt.get().getPassword());
                String token = jwtService.createLoginToken(tokenuser);
                return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/authuser/{token}")
    @ApiOperation(value = "???????????? ???????????? ????????????")
    public Object authUser(@PathVariable String token) throws SQLException, IOException {
        User tokenuser = jwtService.getUser(token);
        Optional<User> userinfo = userDao.findUserByEmail(tokenuser.getEmail());
        try {
            if (userinfo.isPresent()) {
                return new ResponseEntity<>(userinfo.get(), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/certify/{email}")
    @ApiOperation(value = "???????????? ??????")
    public Object certify(@PathVariable String email) throws MessagingException, SQLException, IOException {
        String charSet = "utf-8";
        String hostSMTP = "smtp.naver.com";
        // SMTP ?????????

        String hostSMTPid = "eagleeye0117@naver.com";
        String hostSMTPpwd = "mine0117tjdrhd12";

        // ????????? ??????
        String fromEmail = hostSMTPid;
        String fromName = "????????????";

        String subject = "???????????? ??????????????? ???????????????!!!";

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setDebug(true);
            mail.setCharset(charSet);
            mail.setSSLOnConnect(true);

            // SSL ??????(TLS??? ?????? ?????? SSL ??????)
            mail.setHostName(hostSMTP);
            mail.setSmtpPort(587);
            mail.setAuthentication(hostSMTPid, hostSMTPpwd);
            mail.setStartTLSEnabled(true);
            mail.addTo(email);
            mail.setFrom(fromEmail, fromName, charSet);
            mail.setSubject(subject);
            // ??????

            char[] number = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
            StringBuffer newKey = new StringBuffer();
            for (int i = 0; i < 6; i++) {
                int idx = (int) (number.length * Math.random());
                newKey.append(number[idx]);
            }
            String certifyNum = newKey.toString();
            mail.setHtmlMsg("??????????????? ?????????????????? ???????????? ???????????????.\n" + "???????????? [ " + certifyNum + " ] ??? ??????????????????.");
            mail.send();

            Verification verification = verificationDao.findByEmail(email);
            if (verification == null) {
                Verification ver = new Verification();
                ver.setEmail(email);
                ver.setCode(certifyNum);
                verificationDao.save(ver);
            } else {
                verification.setCode(certifyNum);
                verificationDao.save(verification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }

    @GetMapping("/checkCode/{email}/{code}")
    @ApiOperation(value = "???????????? ??????")
    public Object checkCode(@PathVariable String email, @PathVariable String code) throws SQLException, IOException {
        String result = "";
        try {
            Verification ver = verificationDao.findByEmail(email);
            if (code.equals(ver.getCode())) {
                result = "??????";
            } else {
                result = "??????";
            }
            return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    @ApiOperation(value = "????????????")
    public Object signup(@RequestBody User request) throws SQLException, IOException {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setCheckType(request.getCheckType());
        user.setImgurl(request.getImgurl());
        if (request.getCheckType().equals("business")) {
            user.setClocation(request.getClocation());
            user.setCphone(request.getCphone());
        }
        userDao.save(user);

        return user;
    }

    @PostMapping("/kakaologin")
    @ApiOperation(value = "????????? ?????????")
    public Object viewInfo(@RequestBody User request) throws SQLException, IOException {
        String token = null;
        try {
            Optional<User> userOpt = userDao.findUserByEmail(request.getEmail());
            if (userOpt.isPresent()) {
                User tokenuser = new User();
                tokenuser.setEmail(userOpt.get().getEmail());
                tokenuser.setPassword(userOpt.get().getPassword());
                token = jwtService.createLoginToken(tokenuser);
            } else {
                User user = new User();
                user.setEmail(request.getEmail());
                user.setNickname(request.getNickname());
                user.setName(request.getNickname());
                user.setCheckType("normal");
                user.setPassword("kakaopassword123");
                userDao.save(user);

                User tokenuser = new User();
                tokenuser.setEmail(user.getEmail());
                tokenuser.setPassword(user.getPassword());
                token = jwtService.createLoginToken(tokenuser);
            }
            return new ResponseEntity<>(token, HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/viewInfo/{email}")
    @ApiOperation(value = "??????????????????")
    public Object viewInfo(@PathVariable String email) throws SQLException, IOException {
        try {
            Optional<User> userOpt = userDao.findUserByEmail(email);
            return userOpt.get();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/countAllUser")
    @ApiOperation(value = "?????? ?????????")
    public Object countAllUser() throws SQLException, IOException {
        try {
            return new ResponseEntity<>(userDao.findByUidNot(1).size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/viewAllUser/{page}")
    @ApiOperation(value = "?????? ????????????")
    public Object viewAllUser(@PathVariable int page) throws SQLException, IOException {
        try {
            List<User> list = new LinkedList<>();
            list = userDao.findByUidNot(1, PageRequest.of(page - 1, 10));
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/dropUser/{uid}")
    @ApiOperation(value = "?????? ??????")
    public Object dropUser(@PathVariable int uid) throws SQLException, IOException {
        try {
            User user = userDao.findByUid(uid);
            if (user != null) {
                userDao.delete(user);
                return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/checkEmail/{email}")
    @ApiOperation(value = "???????????????")
    public String checkEmail(@PathVariable String email) {
        String result = "";

        ReportUser reuser = reportUserDao.findByEmail(email);
    if(reuser != null){
        if (reuser.getIsdrop() == 1) {
            result = "?????????????????? ??????????????????";
            return result;
        }else {
            Optional<User> userOpt = userDao.findUserByEmail(email);
            if (userOpt.isPresent()) {
                result = "?????? ???????????? ??????????????????.";
            }
            return result;
        }
        
    } else {
            Optional<User> userOpt = userDao.findUserByEmail(email);
            if (userOpt.isPresent()) {
                result = "?????? ???????????? ??????????????????.";
            }
            return result;
        }
    }

    @GetMapping("/checkNickname/{nickname}")
    @ApiOperation(value = "???????????????")
    public String checkNickname(@PathVariable String nickname) {
        String result = "";
        Optional<User> userOpt = userDao.findUserByNickname(nickname);
        if (userOpt.isPresent()) {
            result = "?????? ???????????? ??????????????????.";
        }
        return result;
    }

    @GetMapping("/getNickname/{email}")
    @ApiOperation(value = "?????????????????????")
    public String getNickname(@PathVariable String email) {
        User user = userDao.getUserByEmail(email);
        return user.getNickname();
    }

    @GetMapping("/getType/{nickname}")
    @ApiOperation(value = "?????????????????????")
    public String getType(@PathVariable String nickname) {
        User user = userDao.findByNickname(nickname);
        return user.getCheckType();
    }

    @PutMapping("/modify/{pwvalidated}")
    @ApiOperation(value = "??????????????????")
    public Object modify(@Valid @RequestBody User request, @PathVariable int pwvalidated)
            throws SQLException, IOException {
        try {
            Optional<User> userOpt = userDao.findUserByEmail(request.getEmail());
            if (userOpt.isPresent()) {
                User newUser = userOpt.get();
                if (pwvalidated == 1) {
                    newUser.setPassword(request.getPassword());
                }
                newUser.setNickname(request.getNickname());
                // newUser.setImgurl(request.getImgurl());
                userDao.save(newUser);
                return newUser;
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{email}")
    @ApiOperation(value = "????????????")
    public Object delete(@PathVariable String email) {
        Optional<User> userOpt = userDao.findUserByEmail(email);
        if (userOpt.isPresent()) {
            userDao.delete(userOpt.get());
            return "?????? ??????";
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getImg/{email}")
    @ApiOperation(value = "??????????????????")
    public String getImg(@PathVariable String email) {
        Optional<User> user = userDao.findUserByEmail(email);
        return user.get().getImgurl();
    }

    @GetMapping("/pwd/{email}/{name}")
    @ApiOperation(value = "?????????????????? ??????")
    public Object sendMail(@PathVariable String email, @PathVariable String name) throws Exception {

        // Mail Server ??????

        String charSet = "utf-8";
        String hostSMTP = "smtp.naver.com";
        // SMTP ?????????

        String hostSMTPid = "eagleeye0117@naver.com";
        String hostSMTPpwd = "mine0117tjdrhd12";

        // ????????? ??????
        String fromEmail = hostSMTPid;
        String fromName = "????????????";

        String subject = "???????????? ?????? ???????????? ??????";

        String newPwd = FindUtil.getNewPwd();

        User user = userDao.findUserByEmailAndName(email, name);
        if (user != null) {
            user.setPassword(newPwd);
            userDao.save(user);

            // email ??????
            try {
                HtmlEmail mail = new HtmlEmail();
                mail.setDebug(true);
                mail.setCharset(charSet);
                mail.setSSLOnConnect(true);

                // SSL ??????(TLS??? ?????? ?????? SSL ??????)
                mail.setHostName(hostSMTP);
                mail.setSmtpPort(587);
                mail.setAuthentication(hostSMTPid, hostSMTPpwd);
                mail.setStartTLSEnabled(true);
                mail.addTo(email);
                mail.setFrom(fromEmail, fromName, charSet);
                mail.setSubject(subject);
                // ??????
                mail.setHtmlMsg("???????????? ?????? ??????????????? [ " + newPwd +" ] ?????????.");
                mail.send();
                System.out.println("??????");
                return "?????? ?????? ??????";
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            System.out.println("?????? ??????????????????");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/file/{email}")
    @ApiOperation(value = "????????? ??????")
    public String fileTest(@RequestPart("file") MultipartFile ff, @PathVariable String email) throws IllegalStateException, IOException {
        
        // String originFilename = ff.getOriginalFilename();
        // String extName = "."+originFilename.substring(originFilename.lastIndexOf(".")+1, originFilename.length()).toLowerCase();
        String fileName = "";
		
		Calendar calendar = Calendar.getInstance();
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
        fileName += ".png";

        // String root = System.getProperty("user.dir");
        // String url = root + "/frontend/public/contents/";
        String hostname = InetAddress.getLocalHost().getHostName();
        System.out.println(hostname);
        File file = null;
        if(hostname.substring(0,7).equals("DESKTOP")){
            //local
            file = new File("C:\\leejaein\\project-sub3\\s03p13b206\\frontend\\public\\contents\\" + fileName);
        }else{
            //aws
            file = new File("/home/ubuntu/ssafy6/s03p13b206/frontend/public/contents/" + fileName);
        }
        // File file = new File(url + fileName);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        ff.transferTo(file);

        User user = userDao.findUserByEmail(email).get();
        user.setImgurl(file.getName());
        userDao.save(user);
        return file.getName();
    }
}