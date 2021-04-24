package com.web.blog.controller.account;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.post.PostListDao;
import com.web.blog.dao.user.ReviewDao;
import com.web.blog.model.post.PostList;
import com.web.blog.model.user.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewDao reviewDao;

    @Autowired
    PostListDao postDao;

    @PostMapping("/regist")
    @ApiOperation("후기 등록")
    public Object regist(@RequestBody Review request) throws SQLException, IOException {
        try {
            // 평균 평점 구하기
            float reviewCnt = reviewDao.findByPid(request.getPid()).size();
            PostList post = postDao.findByPid(request.getPid());
            if (reviewCnt == 0) {
                post.setStar(request.getStar());
            } else {
                float cur = post.getStar();
                float newStar = (cur * reviewCnt + (float) request.getStar()) / (reviewCnt + 1);
                post.setStar(newStar);
            }
            postDao.save(post);

            // 후기 등록
            Review review = new Review();
            review.setPid(request.getPid());
            review.setEmail(request.getEmail());
            review.setTitle(request.getTitle());
            review.setContent(request.getContent());
            // review.setImg(request.getImg());
            review.setStar(request.getStar());
            review.setProimg(request.getProimg());
            LocalDateTime time = LocalDateTime.now();
            review.setCreateDate(time);
            review.setNickname(request.getNickname());
            reviewDao.save(review);

            return new ResponseEntity<>(review, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{pid}")
    @ApiOperation("후기 리스트")
    public List<Review> list(@PathVariable int pid) {
        List<Review> list = new LinkedList<>();
        list = reviewDao.findByPid(pid);
        return list;
    }

    @PutMapping("/modify")
    @ApiOperation("후기 수정")
    public Object modify(@RequestBody Review request) throws SQLException, IOException {
        try {
            Review temp = reviewDao.findByRvid(request.getRvid());

            if (temp != null) {
                float reviewCnt = reviewDao.findByPid(request.getPid()).size();
                PostList post = postDao.findByPid(request.getPid());
                float cur = post.getStar();
                float newStar = (cur * reviewCnt - (float) temp.getStar() + (float) request.getStar()) / reviewCnt;
                post.setStar(newStar);
                postDao.save(post);

                Review nTemp = temp;
                nTemp.setTitle(request.getTitle());
                nTemp.setContent(request.getContent());
                nTemp.setImg(request.getImg());
                nTemp.setStar(request.getStar());
                LocalDateTime time = LocalDateTime.now();
                nTemp.setCreateDate(time);

                reviewDao.save(nTemp);

                return new ResponseEntity<>(nTemp, HttpStatus.ACCEPTED);
            } else {
                System.out.println("DB에 없음");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{rvid}")
    @ApiOperation("후기 삭제")
    public Object delete(@PathVariable int rvid) throws SQLException, IOException {
        Review review = reviewDao.findByRvid(rvid);
        if (review != null) {
            float reviewCnt = reviewDao.findByPid(review.getPid()).size();
            PostList post = postDao.findByPid(review.getPid());
            float cur = post.getStar();
            float newStar;
            if(reviewCnt > 1) {
                newStar = (cur * reviewCnt - (float)review.getStar()) / (reviewCnt - 1);
            } else {
                newStar = 0;
            }
            post.setStar(newStar);
            postDao.save(post);

            reviewDao.delete(review);
            return new ResponseEntity<>("후기 삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getLastReview")
    @ApiOperation("최신 리뷰 불러오기")
    public List<Review> getLastReview() throws SQLException, IOException {
        List<Review> list = new LinkedList<>();
        list = reviewDao.findTop6ByOrderByCreateDateDesc();
        return list;
    }

    @GetMapping("/reviewDetail/{pid}/{rvid}")
    @ApiOperation("pid와 rvid로 데이터 가져오기")
    public Object reviewDetail(@PathVariable int pid, @PathVariable int rvid) throws SQLException, IOException {
        Review review = new Review();
        try {
            review = reviewDao.findByPidAndRvid(pid, rvid);
            if (review != null) {
                return review;
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listbyemail/{email}/{page}")
    @ApiOperation("내가 쓴 후기 리스트")
    public Object list(@PathVariable String email, @PathVariable int page) {
        try {
            List<Review> list = new LinkedList<>();
            list = reviewDao.findByEmail(email, PageRequest.of(page - 1, 8));
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/listbyemail/{email}")
    @ApiOperation("내가 쓴 후기 리스트 카운트")
    public Object countList(@PathVariable String email) {
        try {
            List<Review> list = new LinkedList<>();
            list = reviewDao.findByEmail(email);
            return new ResponseEntity<>(list.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/file/{rvid}")
    @ApiOperation(value = "이미지 저장")
    public String fileTest(@RequestPart("file") MultipartFile ff, @PathVariable int rvid) throws IllegalStateException, IOException {
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
        
        String hostname = InetAddress.getLocalHost().getHostName();
        
        File file = null;
        if(hostname.substring(0,7).equals("DESKTOP")){
            //local
            file = new File("C:\\leejaein\\project-sub3\\s03p13b206\\frontend\\public\\contents\\" + fileName);
        }else{
            //aws
            file = new File("/home/ubuntu/ssafy6/s03p13b206/frontend/public/contents/" + fileName);
        }
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
            ff.transferTo(file);

        Review review = reviewDao.findByRvid(rvid);
        review.setImg(file.getName());
        reviewDao.save(review);

        return file.getName();
    }

}