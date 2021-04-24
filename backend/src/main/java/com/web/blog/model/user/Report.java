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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rpid;
    
    private String email;
    private String remail;
    private String reason;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createDate;
    private int pid;
    private int cnt;
}