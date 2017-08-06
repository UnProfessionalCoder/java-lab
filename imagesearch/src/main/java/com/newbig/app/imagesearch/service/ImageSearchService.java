package com.newbig.app.imagesearch.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageSearchService {
    List<String> searchByImage(MultipartFile file);
}
