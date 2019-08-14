package cn.edu.njnu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class ResourceController {

    @Value("${web.upload-path}")
    private String baseDirPath;

    @RequestMapping("factory")
    public String uploadFactoryImg(@RequestParam("file") MultipartFile file) {
        return upload(file, baseDirPath + "factory/");
    }

    @RequestMapping("product")
    public String uploadProductImg(@RequestParam("file") MultipartFile file){
        return upload(file,baseDirPath+"product/");
    }

    @RequestMapping("equipment")
    public String uploadEquipmentImg(@RequestParam("file") MultipartFile file){
        return upload(file,baseDirPath+"equipment/");
    }

    private String upload(MultipartFile file, String dirPath) {
        //获取文件原名
        String originalName = file.getOriginalFilename();
        if (originalName == null) return "error";
        String ex = originalName.substring(originalName.lastIndexOf('.'));
        String newName = UUID.randomUUID().toString() + ex;
        String completeName = dirPath + newName;
        try {
            FileOutputStream os = new FileOutputStream(completeName);
            FileCopyUtils.copy(file.getInputStream(), os);
            return newName;
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

}
