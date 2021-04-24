package com.web.blog.controller.post;

import java.io.Console;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/post")
public class PostListController {

    @Autowired
    PostListDao postDao;

    @Autowired
    TagDao tagDao;

    
    @GetMapping("/getList/{type}/{page}")
    @ApiOperation(value = "승인된 리스트 가져오기")
    public List<PostList> getList(@PathVariable String type, @PathVariable int page) {

        List<PostList> temp = new LinkedList<>();
        if (type.equals("all")) {
            temp = postDao.findByFlagOrderByCreateDateDesc(1, PageRequest.of(page, 8));
        } else if (type.equals("spring")) {
            temp = postDao.findBySpringAndFlagOrderByCreateDateDesc(1, 1, PageRequest.of(page, 8));
        } else if (type.equals("summer")) {
            temp = postDao.findBySummerAndFlagOrderByCreateDateDesc(1, 1, PageRequest.of(page, 8));
        } else if (type.equals("autumn")) {
            temp = postDao.findByAutumnAndFlagOrderByCreateDateDesc(1, 1, PageRequest.of(page, 8));
        } else if (type.equals("winter")) {
            temp = postDao.findByWinterAndFlagOrderByCreateDateDesc(1, 1, PageRequest.of(page, 8));
        } else {
            temp = postDao.findByPlaceAndFlagOrderByCreateDateDesc(type, 1, PageRequest.of(page, 8));
        }

        return temp;
    }

    @GetMapping("/getThatList/{type}/{page}")
    @ApiOperation(value = "페이지 리로딩")
    public List<PostList> getThatList(@PathVariable String type, @PathVariable int page) throws SQLException, IOException{
        int start = page * 8;
        int end = start + 8;

        List<PostList> temp = new LinkedList<>();
        if (type.equals("all")) {
            temp = postDao.findByFlagOrderByCreateDateDesc(1);
        } else if (type.equals("spring")) {
            temp = postDao.findBySpringAndFlagOrderByCreateDateDesc(1, 1);
        } else if (type.equals("summer")) {
            temp = postDao.findBySummerAndFlagOrderByCreateDateDesc(1, 1);
        } else if (type.equals("autumn")) {
            temp = postDao.findByAutumnAndFlagOrderByCreateDateDesc(1, 1);
        } else if (type.equals("winter")) {
            temp = postDao.findByWinterAndFlagOrderByCreateDateDesc(1, 1);
        } else {
            temp = postDao.findByPlaceAndFlagOrderByCreateDateDesc(type, 1);
        }

        if(end > temp.size()) {
            end = temp.size();
        }

        List<PostList> tlist = new LinkedList<>();
        for (int i = 0; i < end; i++) {
            tlist.add(temp.get(i));
        }

        return tlist;
    }

    @GetMapping("/search/{type}/{key}/{word}/{page}")
    @ApiOperation(value = "검색")
    public List<PostList> search(@PathVariable String type, @PathVariable String key, @PathVariable String word,
            @PathVariable int page) throws SQLException, IOException {
        List<PostList> searchpost = new LinkedList<>();
        if (key.equals("title")) {
            searchpost = postDao.findByFlagAndTitleLikeOrderByCreateDateDesc(1, "%" + word + "%", PageRequest.of(page, 8));
        } else if (key.equals("activity")) {
            searchpost = postDao.findByFlagAndActivityLikeOrderByCreateDateDesc(1, "%" + word + "%", PageRequest.of(page, 8));
        } else if (key.equals("price")) {
            int price = Integer.parseInt(word);
            searchpost = postDao.findByFlagAndPriceLessThanEqualOrderByCreateDateDesc(1, price, PageRequest.of(page, 8));
        }
        List<PostList> post = new LinkedList<>();
        if (type.equals("all")) {
            post = searchpost;
        } else if (type.equals("spring")) {
            for (PostList p : searchpost) {
                if (p.getSpring() == 1)
                    post.add(p);
            }
        } else if (type.equals("summer")) {
            for (PostList p : searchpost) {
                if (p.getSummer() == 1)
                    post.add(p);
            }
        } else if (type.equals("autumn")) {
            for (PostList p : searchpost) {
                if (p.getAutumn() == 1)
                    post.add(p);
            }
        } else if (type.equals("winter")) {
            for (PostList p : searchpost) {
                if (p.getWinter() == 1)
                    post.add(p);
            }
        } else {
            for (PostList p : searchpost) {
                if (p.getPlace().equals(type))
                    post.add(p);
            }
        }

        return post;
    }

    @GetMapping("/searchReloading/{type}/{key}/{word}/{page}")
    @ApiOperation(value = "검색 페이지 리로딩")
    public List<PostList> searchReloading(@PathVariable String type, @PathVariable String key, @PathVariable String word,
            @PathVariable int page) throws SQLException, IOException {
        List<PostList> searchpost = new LinkedList<>();
        if (key.equals("title")) {
            searchpost = postDao.findByFlagAndTitleLikeOrderByCreateDateDesc(1, "%" + word + "%");
        } else if (key.equals("activity")) {
            searchpost = postDao.findByFlagAndActivityLikeOrderByCreateDateDesc(1, "%" + word + "%");
        } else if (key.equals("price")) {
            int price = Integer.parseInt(word);
            searchpost = postDao.findByFlagAndPriceLessThanEqualOrderByCreateDateDesc(1, price);
        }
        List<PostList> post = new LinkedList<>();
        if (type.equals("all")) {
            post = searchpost;
        } else if (type.equals("spring")) {
            for (PostList p : searchpost) {
                if (p.getSpring() == 1)
                    post.add(p);
            }
        } else if (type.equals("summer")) {
            for (PostList p : searchpost) {
                if (p.getSummer() == 1)
                    post.add(p);
            }
        } else if (type.equals("autumn")) {
            for (PostList p : searchpost) {
                if (p.getAutumn() == 1)
                    post.add(p);
            }
        } else if (type.equals("winter")) {
            for (PostList p : searchpost) {
                if (p.getWinter() == 1)
                    post.add(p);
            }
        } else {
            for (PostList p : searchpost) {
                if (p.getPlace().equals(type))
                    post.add(p);
            }
        }

        int start = page * 8;
        int end = start + 8;

        if (end > post.size()) {
            end = post.size();
        }

        List<PostList> list = new LinkedList<>();
        for (int i = 0; i < end; i++) {
            list.add(post.get(i));
        }

        return list;
    }

    // @GetMapping("/list")
    // @ApiOperation(value = "포스트 리스트")
    // public List<PostList> selectAll() throws SQLException, IOException {
    //     List<PostList> temp = new LinkedList<>();
    //     temp = postDao.findByFlagOrderByCreateDateDesc(1);
    //     return temp;
    // }

    @GetMapping("/listbylike")
    @ApiOperation(value = "포스트 리스트 좋아요 정렬")
    public List<PostList> selectAllByLike() throws SQLException, IOException {
        List<PostList> temp = new LinkedList<>();
        temp = postDao.findTop4ByFlagOrderByLikecntDesc(1);
        return temp;
    }

    @GetMapping("/detail/{pid}")
    @ApiOperation(value = "포스트 상세정보")
    public Object selectDetail(@PathVariable int pid) throws SQLException, IOException {
        PostList post = postDao.findByPid(pid);
        if (post != null) {
            // System.out.println(post);
            return post;
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modify/{tagValue}")
    @ApiOperation(value = "포스트 수정하기")
    public Object modify(@Valid @RequestBody PostList request, @PathVariable List<String> tagValue) throws SQLException, IOException {
        try {

            System.out.println(request.toString());
            PostList post = postDao.findByPid(request.getPid());
            if (post != null) {
               System.out.println(request.getLocation());
                PostList newTemp = post;
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
                // System.out.println(newTemp);
                postDao.save(newTemp);
                
                int pid = newTemp.getPid();

                tagDao.deleteAll(tagDao.findByPid(pid));
                
                List<String> tags = new LinkedList<>();
                tags = tagValue;
                for (String tagname : tags) {
                    Tag newTag = new Tag();
                    newTag.setPid(pid);
                    newTag.setTagname(tagname);
                    tagDao.save(newTag);
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
    @ApiOperation(value = "포스트 삭제/거절")
    public Object delete(@PathVariable int pid) throws SQLException, IOException {
        PostList post = postDao.findByPid(pid);
        if (post != null) {
            postDao.delete(post);
            return "포스트 삭제 완료";
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mypost/{email}/{page}")
    @ApiOperation(value = "내가 쓴 글(승인)")
    public Object mypost(@PathVariable String email, @PathVariable int page) throws SQLException, IOException {
        List<PostList> list = postDao.findByEmailAndFlag(email,1, PageRequest.of(page - 1, 10)); //내가 쓰고 승인된 글
        if (list != null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/count/mypost/{email}")
    @ApiOperation(value = "내가 쓴 글(승인) 카운트")
    public Object countMypost(@PathVariable String email) throws SQLException, IOException {
        List<PostList> list = postDao.findByEmailAndFlag(email,1); //내가 쓰고 승인된 글
        if (list != null) {
            return new ResponseEntity<>(list.size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/myreject/{email}")
    @ApiOperation(value = "내가 쓴 글(승인x)")
    public Object myreject(@PathVariable String email) throws SQLException, IOException {
        List<PostList> list = postDao.findByEmailAndFlag(email,2); //내가 쓰고 승인x 글
        if (list != null) {
            return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/permit/{pid}")
    @ApiOperation(value = "관리자 포스트 승인")
    public Object permit(@PathVariable int pid) throws SQLException, IOException {
        PostList post = postDao.findByPid(pid);
        if (post != null) {
            post.setFlag(1);
            postDao.save(post);
            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/countStandpost")
    @ApiOperation(value = "관리자 대기 포스트 리스트 카운트")
    public Object countStandpost() throws SQLException, IOException {
        List<PostList> list = postDao.findByFlag(2);
        if (list != null) {
            return new ResponseEntity<>(list.size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/standpost/{page}")
    @ApiOperation(value = "관리자 대기 포스트 리스트")
    public Object standpost(@PathVariable int page) throws SQLException, IOException {
        List<PostList> list = postDao.findByFlag(2, PageRequest.of(page - 1, 10));
        if (list != null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/regist/{tagValue}")
    @ApiOperation("포스트 등록(아직 승인 x)") //승인 안된건 2
    public Object regist(@RequestBody PostList request, @PathVariable List<String> tagValue)
    throws SQLException, IOException {
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
            temp.setFlag(2);
            temp.setActivity(request.getActivity());
            temp.setSpring(request.getSpring());
            temp.setSummer(request.getSummer());
            temp.setAutumn(request.getAutumn());
            temp.setWinter(request.getWinter());
            temp.setPlace(request.getPlace());
            temp.setLocationdetail(request.getLocationdetail());
            postDao.save(temp);

            int pid = temp.getPid();
            List<String> tags = new LinkedList<>();
            tags = tagValue;
            for (String tagname : tags) {
                Tag newTag = new Tag();
                newTag.setPid(pid);
                newTag.setTagname(tagname);
                tagDao.save(newTag);
            }
            return new ResponseEntity<>(temp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    // 태그 검색
    @GetMapping("/getTagList/{pids}/{page}")
    @ApiOperation("태그 검색 위한 리스트 불러오기")
    public Object getTagList(@PathVariable List<Integer> pids, @PathVariable int page) throws SQLException, IOException {
        try {
            List<PostList> list = new LinkedList<>();
            list = postDao.findByFlagAndPidInOrderByCreateDateDesc(1, pids, PageRequest.of(page, 8));
            if(list != null) {
               return list; 
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getTagReloading/{pids}/{page}")
    @ApiOperation("태그 검색 페이지 리로딩")
    public Object tagReloading(@PathVariable List<Integer> pids, @PathVariable int page) throws SQLException, IOException {
        try {
            List<PostList> list = new LinkedList<>();
            list = postDao.findByFlagAndPidInOrderByCreateDateDesc(1, pids);
            if(list != null) {
                int start = page * 8;
                int end = start + 8;

                if(end > list.size()) {
                    end = list.size();
                }

                List<PostList> tempList = new LinkedList<>();
                for (int i = 0; i < end; i++) {
                    tempList.add(list.get(i));
                }

               return tempList; 
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test/{page}")
    @ApiOperation("페이징 테스트")
    public Object pageTest(@PathVariable int page) throws SQLException, IOException {
        // return postDao.findAll(PageRequest.of(page, 5, Direction.DESC, "createDate"));
        return postDao.findByFlagOrderByCreateDateDesc(1, PageRequest.of(page, 5));
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
        post.setImgurl(file.getName());
        postDao.save(post);
        return file.getName();
    }
}