package com.web.blog.model.packaging;

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
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int packno;
    
    private String email;
    private int price;
    private int cnt;
}