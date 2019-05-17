package com.video.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Users {
    @Id
    private String id;

    private String username;

    private String password;

    private String faceImage;

    private String nickname;

    private Integer fansCounts;

    private Integer followCounts;

    private Integer receiveLikeCounts;

    private  Boolean isActive;

}