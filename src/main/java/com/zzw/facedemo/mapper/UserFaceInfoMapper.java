package com.zzw.facedemo.mapper;

import com.zzw.facedemo.domain.UserFaceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author  zhenwei.wang
 * @date 2020/8/5
 */
@Mapper
public interface UserFaceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFaceInfo record);

    int insertSelective(UserFaceInfo record);

    UserFaceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFaceInfo record);

    int updateByPrimaryKey(UserFaceInfo record);


    List<UserFaceInfo> findUserFaceInfoList();

    List<UserFaceInfo> findByGroupId(Integer groupId);

}