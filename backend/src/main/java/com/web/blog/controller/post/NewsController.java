package com.web.blog.controller.post;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.model.post.News;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/news")
public class NewsController {

    @GetMapping("/list")
    @ApiOperation("크롤링")
    public Object crwaling() throws IOException {

        try {
            String URL = "https://www.google.com/search?biw=954&bih=754&tbm=nws&sxsrf=ALeKk00gPZAOtpMBZf7YtyySD6CEwYXA2w%3A1597161742550&ei=DsEyX9WHIaLfmAWf_ofICw&q=%EB%A0%88%EC%A0%80+%EC%8A%A4%ED%8F%AC%EC%B8%A0&oq=%EB%A0%88%EC%A0%80+%EC%8A%A4%ED%8F%AC%EC%B8%A0&gs_l=psy-ab.12...0.0.0.80189.0.0.0.0.0.0.0.0..0.0....0...1c..64.psy-ab..0.0.0....0._BkD1C_8T8Y";
            
            List<News> list = new LinkedList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new News());
            }
    
            Document doc = Jsoup.connect(URL).get();
            Elements el = doc.select(".dbsr");
            
            int index = 0;
            for (Element e : el.select("div[class=\"XTjFC WF4CUc\"]")) {
                list.get(index).setNewsroom(e.text());
                index++;
            }
            
            index = 0;
            for (Element e : el.select("div[class=\"JheGif nDgy9d\"]")) {
                list.get(index).setTitle(e.text());
                index++;
            }
            
            index = 0;
            for (Element e : el.select("div[class=\"Y3v8qd\"]")) {
                list.get(index).setContent(e.text());
                index++;
            }
            
            index = 0;
            for (Element e : el.select("a")) {
                list.get(index).setNewsUrl(e.attr("href"));
                index++;
            }
            
            index = 0;
            for (Element e : el.select("span[class=\"WG9SHc\"]")) {
                list.get(index).setCreateDate(e.text());
                index++;
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}