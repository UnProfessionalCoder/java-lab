package com.newbig.app.web.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageSearchDto {
    private MultipartFile file;
}
