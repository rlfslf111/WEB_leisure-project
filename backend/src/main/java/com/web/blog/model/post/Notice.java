package com.web.blog.model.post;

import java.time.LocalDateTime;

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
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nid;

    private String title;
    private String content;
    private int visit;
    private int importance;

    private LocalDateTime createDate;
}