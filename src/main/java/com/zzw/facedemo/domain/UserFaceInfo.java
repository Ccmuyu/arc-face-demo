package com.zzw.facedemo.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhenwei.wang
 * @description TODO
 * @date 2020/8/5
 */

public class UserFaceInfo implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 人脸唯一Id
     */
    private String faceId;

    /**
     * 名字
     */
    private String name;

    /**
     * 年纪
     */
    private Integer age;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 性别，1=男，2=女
     */
    private Short gender;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 人脸特征
     */
    private byte[] faceFeature;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 照片路径
     */
    private String fpath = "";

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(byte[] faceFeature) {
        this.faceFeature = faceFeature;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFpath() {
        return fpath;
    }

    public void setFpath(String fpath) {
        this.fpath = fpath;
    }
}