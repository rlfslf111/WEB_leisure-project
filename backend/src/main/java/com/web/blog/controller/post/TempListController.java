package com.web.blog.controller.post;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import com.web.blog.dao.post.PostListDao;
import com.web.blog.dao.post.TagDao;
import com.web.blog.model.post.PostList;
import com.web.blog.model.post.Tag;

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
@RequestMapping("/temp")
public class TempListController {

    @Autowired
    PostListDao postDao;

    @Autowired
    TagDao tagDao;

    @GetMapping("/list/{email}/{page}")
    @ApiOperation(value = "임시저장 리스트")
    public Object selectAll(@PathVariable String email, @PathVariable int page) throws SQLException, IOException {
        try {
            List<PostList> temp = new LinkedList<>();
            temp = postDao.findByEmailAndFlagOrderByCreateDateDesc(email, 0, PageRequest.of(page - 1, 10));
            return new ResponseEntity<>(temp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/count/list/{email}")
    @ApiOperation(value = "임시저장 리스트 카운트")
    public Object countAll(@PathVariable String email) throws SQLException, IOException {
        try {
            List<PostList> temp = new LinkedList<>();
            temp = postDao.findByEmailAndFlagOrderByCreateDateDesc(email,0);
            return new ResponseEntity<>(temp.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{pid}")
    @ApiOperation(value = "임시저장 상세정보")
    public Object selectDetail(@PathVariable int pid) throws SQLException, IOException {
        try {
            PostList post = postDao.findByPid(pid);
            if(post!=null){
                return new ResponseEntity<>(post, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/regist/{tagValue}")
    @ApiOperation("임시저장 등록")
    public Object regist(@RequestBody PostList request, @PathVariable List<String> tagValue) throws SQLException, IOException {
        try {
            PostList temp = new PostList();
            temp.setEmail(request.getEmail());
            temp.setTitle(request.getTitle());
            temp.setLocation(request.getLocation());
            // temp.setImgurl(request.getImgurl());
            temp.setPrice(request.getPrice());
            temp.setSdate(request.getSdate());
            temp.setEdate(request.getEdate());
            temp.setCompanyInfo(request.getCompanyInfo());
            temp.setDetail(request.getDetail());
            temp.setFlag(0);
            temp.setActivity(request.getActivity());
            temp.setSpring(request.getSpring());
            temp.setSummer(request.getSummer());
            temp.setAutumn(request.getAutumn());
            temp.setWinter(request.getWinter());
            temp.setPlace(request.getPlace());
            LocalDateTime time = LocalDateTime.now();
            temp.setCreateDate(time);
            temp.setLocationdetail(request.getLocationdetail());
            postDao.save(temp);

            if(!tagValue.get(0).equals("nononotag")) {
                int pid = temp.getPid();
                List<String> tags = new LinkedList<>();
                tags = tagValue;
                for (String tagname : tags) {
                    Tag newTag = new Tag();
                    newTag.setPid(pid);
                    newTag.setTagname(tagname);
                    tagDao.save(newTag);
                }
            }


            return new ResponseEntity<>(temp, HttpStatus.OK);    
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modify/{tagValue}")
    @ApiOperation(value = "임시저장 수정하기")
    public Object modify(@Valid @RequestBody PostList request, @PathVariable List<String> tagValue) throws SQLException, IOException {
        try {
           PostList temp = postDao.findByPid(request.getPid());
            if(temp!=null){
                PostList newTemp = temp;
                newTemp.setTitle(request.getTitle());
                newTemp.setLocation(request.getLocation());
                // newTemp.setImgurl(request.getImgurl());
                newTemp.setPrice(request.getPrice());
                newTemp.setSdate(request.getSdate());
                newTemp.setEdate(request.getEdate());
                newTemp.setCompanyInfo(request.getCompanyInfo());
                newTemp.setDetail(request.getDetail());
                newTemp.setActivity(request.getActivity());
                newTemp.setSpring(request.getSpring());
                newTemp.setSummer(request.getSummer());
                newTemp.setAutumn(request.getAutumn());
                newTemp.setWinter(request.getWinter());
                newTemp.setPlace(request.getPlace());
                LocalDateTime time = LocalDateTime.now();
                newTemp.setCreateDate(time);
                newTemp.setLocationdetail(request.getLocationdetail());
                
                postDao.save(newTemp);

                if(!tagValue.get(0).equals("nononotag")) {
                    int pid = temp.getPid();
                    List<String> tags = new LinkedList<>();
                    tags = tagValue;
                    for (String tagname : tags) {
                        Tag newTag = new Tag();
                        newTag.setPid(pid);
                        newTag.setTagname(tagname);
                        tagDao.save(newTag);
                    }
                }

                
                return new ResponseEntity<>(newTemp, HttpStatus.OK);
            } else {
                System.out.println("DB에 없음.");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{pid}")
    @ApiOperation(value = "임시저장 삭제")
    public Object delete(@PathVariable int pid) throws SQLException, IOException {
        try {
            PostList post = postDao.findByPid(pid);
            if(post != null){
                postDao.delete(post);
                return new ResponseEntity<>("임시저장 삭제", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/file/{pid}")
    @ApiOperation(value = "이미지 저장")
    public String fileTest(@RequestPart("file") MultipartFile ff, @PathVariable int pid) throws IllegalStateException, IOException {
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

        PostList post = postDao.findByPid(pid);
        System.out.println(post);
        post.setImgurl(file.getName());
        postDao.save(post);
        System.out.println(post);
        return file.getName();
    }

    @PostMapping("/temptopost/{tagValue}")
    @ApiOperation("임시저장 했던 글 포스트에 등록")
    public Object temptopost(@RequestBody PostList request, @PathVariable List<String> tagValue) throws SQLException, IOException {
        try {
            PostList temp = postDao.findByPid(request.getPid());
            temp.setEmail(request.getEmail());
            temp.setTitle(request.getTitle());
            temp.setLocation(request.getLocation());
            // temp.setImgurl(request.getImgurl());
            temp.setPrice(request.getPrice());
            temp.setSdate(request.getSdate());
            temp.setEdate(request.getEdate());
            temp.setCompanyInfo(request.getCompanyInfo());
            temp.setDetail(request.getDetail());
            temp.setFlag(2);
            temp.setActivity(request.getActivity());
            temp.setSpring(request.getSpring());
            temp.setSummer(request.getSummer());
            temp.setAutumn(request.getAutumn());
            temp.setWinter(request.getWinter());
            temp.setPlace(request.getPlace());
            LocalDateTime time = LocalDateTime.now();
            temp.setCreateDate(time);
            temp.setLocationdetail(request.getLocationdetail());
            postDao.save(temp);

            if(!tagValue.get(0).equals("nononotag")) {
                int pid = temp.getPid();
                List<String> tags = new LinkedList<>();
                tags = tagValue;
                for (String tagname : tags) {
                    Tag newTag = new Tag();
                    newTag.setPid(pid);
                    newTag.setTagname(tagname);
                    tagDao.save(newTag);
                }
            }
            return new ResponseEntity<>(temp, HttpStatus.OK);    
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}