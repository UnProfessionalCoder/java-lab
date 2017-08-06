package com.newbig.app.imagesearch.controller;


import com.newbig.app.imagesearch.model.vo.ResponseVO;
import com.newbig.app.imagesearch.service.ImageSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;


@RestController
@RequestMapping(value = "/image")
@Slf4j
public class ImageSearchController {
    @Autowired
    private ImageSearchService imageSearchService;

//    @ApiOperation(value="获取图片列表", notes="")
//    @GetMapping(value = "/list")
//    public ResponseVO<PageInfo> list(@RequestParam(required = false,defaultValue = "1")Integer pageNum,
//                                           @RequestParam(required = false,defaultValue = "29")Integer pageSize){
//        return ResponseVO.success(userService.getUserListByPage(pageNum,pageSize));
//    }

    @PostMapping(value = "/search" )
    public ResponseVO<List<String>> search(@RequestParam(value = "file", required = false) MultipartFile file){
        List<String> images = imageSearchService.searchByImage(file);
        return ResponseVO.success(images);
    }

    @RequestMapping(value = "/tfs/{fileName:.+}")
    public void tfsWithoutPostfix(HttpServletResponse response, @PathVariable(required = false) String fileName) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("image/jpeg");
            FileInputStream fin = new FileInputStream(new File("/work/tessdata/testimage/"+fileName.replace("/image/tfs/","")));
//            byte[] image1=  IOUtils.toByteArray(fin);
//                ByteArrayInputStream fis = new ByteArrayInputStream(image1);
//                byte[] bytesRead = new byte[1024 * 1024];
//                int length = 0;
//                while ((length = fis.read(bytesRead)) != -1) {
//                    response.getOutputStream().write(bytesRead, 0, length);
//                }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("file download", e);
        }
    }
}
