package com.zzw.facedemo.service.impl;

import com.zzw.facedemo.domain.UserFaceInfo;
import com.zzw.facedemo.mapper.UserFaceInfoMapper;
import com.zzw.facedemo.service.UserFaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserFaceInfoServiceImpl implements UserFaceInfoService {


    @Autowired
    UserFaceInfoMapper mapper;

    @Override
    public void insertSelective(UserFaceInfo userFaceInfo) {
        mapper.insertSelective(userFaceInfo);
    }
}
