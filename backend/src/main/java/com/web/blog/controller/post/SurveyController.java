package com.web.blog.controller.post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.web.blog.dao.post.PostListDao;
import com.web.blog.model.post.PostList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/survey")
public class SurveyController {
    
    @Autowired
    PostListDao postListDao;

    @GetMapping("/getList/{place}/{location}/{price}/{season}")
    @ApiOperation("설문조사")
    public List<String> survey(@PathVariable String place, @PathVariable String location, @PathVariable int price, @PathVariable String season) throws SQLException, IOException {
        
        List<PostList> placeList = new LinkedList<>();
        
        Map<String, Integer> map = new HashMap<>();

        placeList = postListDao.findByFlagAndPlace(1, place);

        for (PostList postList : placeList) {
            if(!map.containsKey(postList.getActivity())){
                map.put(postList.getActivity(), 1);
            } else {
                int cnt = map.get(postList.getActivity());
                map.put(postList.getActivity(), cnt + 1);
            }
        }
        
        List<PostList> seasonList = new LinkedList<>();
        if(season.equals("spring")) {
            seasonList = postListDao.findBySpringAndFlagOrderByCreateDateDesc(1, 1);

        } else if(season.equals("summer")) {
            seasonList = postListDao.findBySummerAndFlagOrderByCreateDateDesc(1, 1);

        } else if(season.equals("autumn")) {
            seasonList = postListDao.findByAutumnAndFlagOrderByCreateDateDesc(1, 1);

        } else if(season.equals("winter")) {
            seasonList = postListDao.findByWinterAndFlagOrderByCreateDateDesc(1, 1);

        }

        for (PostList postList : seasonList) {
            if(!map.containsKey(postList.getActivity())){
                map.put(postList.getActivity(), 1);
            } else {
                int cnt = map.get(postList.getActivity());
                map.put(postList.getActivity(), cnt + 1);
            }
        }
        
        List<PostList> priceList = new LinkedList<>();
        priceList = postListDao.findByFlagAndPriceLessThanEqual(1, price);
        
        for (PostList postList : priceList) {
            if(!map.containsKey(postList.getActivity())){
                map.put(postList.getActivity(), 1);
            } else {
                int cnt = map.get(postList.getActivity());
                map.put(postList.getActivity(), cnt + 1);
            }
        }
        
        location = "%" + location + "%";
        List<PostList> locationList = new LinkedList<>();
        locationList = postListDao.findByFlagAndLocationLike(1, location);
        
        for (PostList postList : locationList) {
            if(!map.containsKey(postList.getActivity())){
                map.put(postList.getActivity(), 1);
            } else {
                int cnt = map.get(postList.getActivity());
                map.put(postList.getActivity(), cnt + 1);
            }
        }

        List<String> mapSort = new ArrayList<>(map.keySet());
        Collections.sort(mapSort, (o1, o2) -> (map.get(o2).compareTo(map.get(o1))));
        
        List<String> result = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            result.add(mapSort.get(i));
        }

        return result;
    }
    
}