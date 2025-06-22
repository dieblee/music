package com.xm.controller;


import cn.hutool.core.io.FileUtil;
import com.xm.common.Result;
import com.xm.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileController {
    private static final String filePath = System.getProperty("user.dir") + "/files/";//获取你当前项目的路径名
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if(FileUtil.isDirectory(filePath)){//如果目录不存在需要先创建
            FileUtil.mkdir(filePath);
        }//提供文件完整路径
        //给文件加一个唯一标识
        String fileName = System.currentTimeMillis() + "_"+ originalFilename;
        String realPath = filePath + fileName;//文件完整路径
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        }catch (IOException e){
            e.printStackTrace();
            throw new CustomException("500","文件上传失败");
        }
        String url = "http://localhost:8080/files/download/" + fileName;
        return Result.success(url);
    }
    //文件下载
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        String realPath = filePath + fileName;
        if (!FileUtil.exist(realPath)) {
            throw new CustomException("404", "文件未找到: " + fileName);
        }
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            OutputStream os = response.getOutputStream();
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件下载失败");
        }
    }
}
