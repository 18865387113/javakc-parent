package com.javakc.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.javakc.oss.utils.PropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OssService {

    public String uploadFile(MultipartFile file) {
        // ## 获取OSS配置文件信息
        String endPoint = PropertiesUtils.END_POINT;
        String keyId = PropertiesUtils.KEY_ID;
        String keySecret = PropertiesUtils.KEY_SECRET;
        String bucketName = PropertiesUtils.BUCKET_NAME;

        // ## 定义上传后返回的路径
        String uploadUrl = null;

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = endPoint;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = keyId;
        String accessKeySecret = keySecret;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // ## 获取流文件
            InputStream inputStream = file.getInputStream();

            // ## 设置日期路径
            String filePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            // ## 设置文件名
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newName = fileName + fileType;
            String fileUrl = "image/" + filePath + "/" + newName;

            // 上传文件流。
            ossClient.putObject(bucketName, fileUrl, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            // ## 获取url地址
            uploadUrl = "https://" + bucketName + "." + endPoint + "/" + fileUrl;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadUrl;
    }

}
