package com.zzw.facedemo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import com.zzw.facedemo.domain.UserFaceInfo;
import com.zzw.facedemo.dto.FaceUserInfo;
import com.zzw.facedemo.dto.ProcessInfo;
import com.zzw.facedemo.factory.FaceEngineFactory;
import com.zzw.facedemo.mapper.UserFaceInfoMapper;
import com.zzw.facedemo.service.FaceEngineService;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
public class FaceEngineServiceImpl implements FaceEngineService {

    public final static Logger logger = LoggerFactory.getLogger(FaceEngineServiceImpl.class);

    @Value("${arcface-sdk.sdk-lib-path}")
    public String sdkLibPath;
    @Value("${arcface-sdk.app-id}")
    public String appId;

    @Value("${arcface-sdk.sdk-key}")
    public String sdkKey;

    @Value("${arcface-sdk.thread-pool-size}")
    public Integer threadPoolSize;


    private ExecutorService executorService;

    @Autowired
    UserFaceInfoMapper userFaceInfoMapper;

    private GenericObjectPool<FaceEngine> faceEngineObjectPool;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        GenericObjectPoolConfig<FaceEngine> poolConfig = initPoolConfig();
        EngineConfiguration engineConfiguration = initEngine();

        //底层库算法对象池
        FaceEngineFactory engineFactory = new FaceEngineFactory(sdkLibPath, appId, sdkKey, engineConfiguration);
        faceEngineObjectPool = new GenericObjectPool<>(engineFactory, poolConfig);

    }

    private GenericObjectPoolConfig<FaceEngine> initPoolConfig() {
        GenericObjectPoolConfig<FaceEngine> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);
        return poolConfig;
    }

    private EngineConfiguration initEngine() {
        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        return engineConfiguration;
    }


    private int plusHundred(Float value) {
        BigDecimal target = new BigDecimal(value);
        BigDecimal hundred = new BigDecimal("100");
        return target.multiply(hundred).intValue();

    }

    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            return faceInfoList;
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return null;
    }


    @Override
    public List<ProcessInfo> process(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<>();
            //人脸检测
            faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
                    imageInfo.getImageFormat(), faceInfoList);
            int processResult = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
                    imageInfo.getImageFormat(), faceInfoList, FunctionConfiguration.builder().supportAge(true).supportGender(true).build());
            List<ProcessInfo> processInfoList = Lists.newLinkedList();

            List<GenderInfo> genderInfoList = new ArrayList<>();
            //性别提取
            int genderCode = faceEngine.getGender(genderInfoList);
            //年龄提取
            List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
            int ageCode = faceEngine.getAge(ageInfoList);
            for (int i = 0; i < genderInfoList.size(); i++) {
                ProcessInfo processInfo = new ProcessInfo();
                processInfo.setGender(genderInfoList.get(i).getGender());
                processInfo.setAge(ageInfoList.get(i).getAge());
                processInfoList.add(processInfo);
            }
            return processInfoList;

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    /**
     * 人脸特征
     *
     * @param imageInfo
     * @return
     */
    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo) {

        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            int i = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);

            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                FaceFeature faceFeature = new FaceFeature();
                //提取人脸特征
                faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);

                return faceFeature.getFeatureData();
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }

        }

        return null;
    }

    @Override
    public List<FaceUserInfo> compareFaceFeature(byte[] faceFeature, Integer groupId) throws InterruptedException, ExecutionException {
        List<FaceUserInfo> resultFaceInfoList = Lists.newLinkedList();//识别到的人脸列表

        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);
        List<UserFaceInfo> byGroupId = userFaceInfoMapper.findByGroupId(groupId);//从数据库中取出人脸库
        List<FaceUserInfo> faceInfoList = byGroupId.stream()
                .map(e -> new FaceUserInfo(e.getFaceId(), e.getName(), null, e.getFaceFeature()))
                .collect(Collectors.toList());
        List<List<FaceUserInfo>> faceUserInfoPartList = Lists.partition(faceInfoList, 1000);//分成1000一组，多线程处理
        CompletionService<List<FaceUserInfo>> completionService = new ExecutorCompletionService<>(executorService);
        for (List<FaceUserInfo> part : faceUserInfoPartList) {
            completionService.submit(new CompareFaceTask(part, targetFaceFeature));
        }
        for (int i = 0; i < faceUserInfoPartList.size(); i++) {
            List<FaceUserInfo> faceUserInfoList = completionService.take().get();
            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                resultFaceInfoList.addAll(faceUserInfoList);
            }
        }

        resultFaceInfoList.sort((h1, h2) -> h2.getSimilarValue().compareTo(h1.getSimilarValue()));//从大到小排序

        return resultFaceInfoList;
    }


    private class CompareFaceTask implements Callable<List<FaceUserInfo>> {

        private List<FaceUserInfo> faceUserInfoList;
        private FaceFeature targetFaceFeature;


        public CompareFaceTask(List<FaceUserInfo> faceUserInfoList, FaceFeature targetFaceFeature) {
            this.faceUserInfoList = faceUserInfoList;
            this.targetFaceFeature = targetFaceFeature;
        }

        @Override
        public List<FaceUserInfo> call() {
            FaceEngine faceEngine = null;
            List<FaceUserInfo> resultFaceInfoList = Lists.newLinkedList();//识别到的人脸列表
            try {
                faceEngine = faceEngineObjectPool.borrowObject();
                for (FaceUserInfo faceUserInfo : faceUserInfoList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(faceUserInfo.getFaceFeature());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    int similarValue = plusHundred(faceSimilar.getScore());//获取相似值
                    int passRate = 80;
                    if (similarValue > passRate) {//相似值大于配置预期，加入到识别到人脸的列表

                        FaceUserInfo info = new FaceUserInfo();
                        info.setName(faceUserInfo.getName());
                        info.setFaceId(faceUserInfo.getFaceId());
                        info.setSimilarValue(similarValue);
                        resultFaceInfoList.add(info);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                if (faceEngine != null) {
                    faceEngineObjectPool.returnObject(faceEngine);
                }
            }

            return resultFaceInfoList;
        }

    }
}
