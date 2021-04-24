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

public class ReplyList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rid;
    
    private int pid;
    private String nickname;
    private String content;

        // @JsonIgnore
        private LocalDateTime createDate;
}