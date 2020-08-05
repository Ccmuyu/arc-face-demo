
参考项目：https://github.com/itboyst/ArcSoftFaceDemo
--- 

## 开发环境准备：
###开发使用到的软件和工具：
* Jdk8、mysql5.7、libarcsoft_face.dll(so)、libarcsoft_face_engine.dll(so)、libarcsoft_face_engine_jni.dll(so)、idea
* 注：引擎库在虹软官网 http://ai.arcsoft.com.cn/ 免费下载


* 本地配置：
	* 配置环境(推荐jdk8，mysql5.7，maven3，idea)
    * 引擎库
      * libarcsoft_face、
      * libarcsoft_face_engine、
      * libarcsoft_face_engine_jni。
  
* 初始化项目
  表：arcsoft_face_demo.sql

* 使用idea启动项目
    * 修改配置文件src\main\resources\application.properties
        * 数据库地址：url
        * 数据库用户名：username
        * 数据库密码：password
        * 人脸识别id：app-id
        * 人脸识别key：sdk-key

 
* 启动项目
    * 右键Application，选择Run ‘Application’
 

* 项目访问地址
    * http://127.0.0.1:8089/demo


### 项目流程图
 ![](人脸流程.png)

