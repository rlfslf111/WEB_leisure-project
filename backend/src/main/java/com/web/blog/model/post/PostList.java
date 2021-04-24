package com.web.blog.model.post;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    private String email;
    private String title;
    private String location;
    @Column(name="imgurl")
    private String imgurl;
    private int price;
    private LocalDate sdate;
    private LocalDate edate;
    private String companyInfo;
    private String detail;
    private int flag;
    private String activity;
    private int spring;
    private int summer;
    private int autumn;
    private int winter;
    private String place;
    private int likecnt;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createDate;
    private float star;
    private String locationdetail;
}