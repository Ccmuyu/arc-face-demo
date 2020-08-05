package com.zzw.facedemo.controller;

import com.zzw.facedemo.domain.UserFaceInfo;
import com.zzw.facedemo.mapper.UserFaceInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserListController {

    @Autowired
    private UserFaceInfoMapper infoMapper;

    @GetMapping("/userInfo")
    public List<UserFaceInfo> getUserInfo() {
        return infoMapper.findUserFaceInfoList();
    }
}
