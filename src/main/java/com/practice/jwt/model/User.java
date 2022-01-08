package com.practice.jwt.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// mysql때문에
    private long id;
    private String username;
    private String password;
    private String roles; // USER,ADMIN

    public List<String> getRoleList() {
        if(this.roles.length() >0) {
            return Arrays.asList(this.roles.split(","));
        }// role이 하나라도 있으면 , 를 잡아서 스플릿하고 그걸 리스트화 함.
        return new ArrayList<>(); //null 안뜨게
    }
}
