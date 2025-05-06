package com.example.health.controller;

import com.example.health.api.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author huanghaiming
 * @date 2025/5/3 15:18
 * @description: 文件上传接口
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    /**
     * 文件存储本地路径
     */
    @Value("${file.local-path}")
    private String urlPath;

    /**
     * URL访问前缀
     */
    @Value("${file.url-prefix}")
    private String urlPrefix;

    /**
     * 文件上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ApiResponse<String> upload(MultipartFile file) {
        // 校验文件是否为空
        if (file.isEmpty()) {
            return ApiResponse.error("文件不能为空");
        }
        // 文件名获取与校验
//        String fileName = file.getOriginalFilename();
//        if (fileName == null || fileName.contains("..")) {
//            return ApiResponse.error("文件名不合法");
//        }
        // 保存文件
//        try {
//            File dest = new File(urlPath + fileName);
//            if (!dest.getParentFile().exists()) {
//                dest.getParentFile().mkdirs();
//            }
//            file.transferTo(dest);

            // 返回可直接访问的URL
//            String fileUrl = "http://localhost:21090" + urlPrefix + URLEncoder.encode(fileName, "UTF-8");
//            return ApiResponse.success(fileUrl);
//        } catch (IOException e) {
//            return ApiResponse.error("文件上传失败");
//        }
        // 保存文件
        String fileName = file.getOriginalFilename();  // 获取文件名
        File dest = new File(urlPath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs(); // 创建父目录（如果不存在）
        }
        try {
            file.transferTo(dest);  // 保存文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回访问URL
        String final_fileName = "http://localhost:21090" + urlPrefix + fileName;
        return ApiResponse.success(final_fileName);
    }
}
