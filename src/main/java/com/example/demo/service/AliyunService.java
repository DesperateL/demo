package com.example.demo.service;

import com.aliyun.oss.OSSClient;
import com.example.demo.controller.LoginController;
import com.example.demo.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class AliyunService {
    private static Logger logger = LoggerFactory.getLogger(AliyunService.class);
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    String accessKeyId = "LTAIU6Sp9L02Yj5Y";
    String accessKeySecret = "2s3AtL0fxwUaAcUqhtbFvMk5MBLAGa";

    public String saveImage(MultipartFile file) {
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if(doPos<0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(doPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;


        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        try {
            ossClient.putObject("xiaojiaqi", fileName, file.getInputStream());
        } catch (IOException e) {
            logger.error("图片上传失败",e.getMessage());
            return null;
        }finally {
            // 关闭OSSClient。

            ossClient.shutdown();
        }
        //System.out.println(fileName+"上传成功");

        //xxx.jpg
        return ToutiaoUtil.ALIYUN_DOMAIN+fileName;


    }



}
