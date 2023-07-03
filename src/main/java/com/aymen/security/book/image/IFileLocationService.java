package com.aymen.security.book.image;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileLocationService {
    public Image save(MultipartFile file) throws Exception ;
    public FileSystemResource find(Long imageId) ;

}
