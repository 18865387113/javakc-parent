package com.javakc.oss.controller;

import com.javakc.commonutils.api.APICODE;
import com.javakc.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "阿里云OSS存储")
@RestController
@RequestMapping("/serviceoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "文件上传")
    @PostMapping("uploadFile")
    public APICODE uploadFile(MultipartFile file) {
        String url = ossService.uploadFile(file);
        return APICODE.OK().message("文件上传成功").data("url", url);
    }

}
