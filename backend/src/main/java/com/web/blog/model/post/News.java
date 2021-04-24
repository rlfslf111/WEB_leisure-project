package com.web.blog.model.post;

import lombok.Data;

@Data
public class News {
    private String newsroom;
    private String title;
    private String content;
    private String newsUrl;
    private String createDate;
}