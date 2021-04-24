package com.web.blog.model.user;

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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rvid;
    
    private int pid;
    private String email;
    private String title;
    private String content;
    @Column(name="img")
    private String img;
    private int star;
    private LocalDateTime createDate;
    @Column(name="proimg")
    private String proimg;
    private String nickname;
}