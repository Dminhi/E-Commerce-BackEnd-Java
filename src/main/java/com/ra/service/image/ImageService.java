package com.ra.service.image;


import com.ra.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<Image> findAll();
    List<Image>findByProductId(Long productId);
    Image saveOrUpdate(Image images);

}
