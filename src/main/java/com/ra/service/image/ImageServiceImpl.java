package com.ra.service.image;


import com.ra.model.entity.Image;
import com.ra.repository.ImageRepository;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

@Autowired
    ImageRepository imageRepository;
@Autowired
    UploadService uploadService;
    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public List<Image> findByProductId(Long productId) {
        return imageRepository.findByProductId(productId);
    }

    @Override
    public Image saveOrUpdate(Image images) {
        return imageRepository.save(images);
    }

}
