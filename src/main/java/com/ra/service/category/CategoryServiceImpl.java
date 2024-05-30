package com.ra.service.category;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import com.ra.repository.ICategoryRepository;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements  ICategoryService{
@Autowired
private ICategoryRepository iCategoryRepository;
@Autowired
private UploadService uploadService;

    @Override
    public ResponseEntity<?> getCategory(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<CategoryResponseDTO> categoryPage;
        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            categoryPage = findAllWithPaginationAndSort(pageable);
        }
        if (categoryPage == null || categoryPage.isEmpty()) {
            throw new CustomException("Category is not found", HttpStatus.NOT_FOUND);

        }
       PageDataDTO<CategoryResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(categoryPage.getNumber());
        pageDataDTO.setTotalPage(categoryPage.getTotalPages());
        pageDataDTO.setLimit(categoryPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setTotalElement(categoryPage.getTotalElements());
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(categoryPage.getContent());
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<CategoryResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Category> list = iCategoryRepository.findAll(pageable);
        return list.map(CategoryResponseDTO::new);
    }

    @Override
    public Page<CategoryResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Category> list = iCategoryRepository.findAllByCategoryNameContainingIgnoreCase(pageable, name);
        return list.map(CategoryResponseDTO::new);
    }

    @Override
    public ResponseEntity<?> save(CategoryRequestDTO categoryRequestDTO) throws CustomException {
        if(categoryRequestDTO.getId() ==null) {
            if(iCategoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName())) {
                throw new CustomException("Category name has been already existed!", HttpStatus.CONFLICT);
            }
        }
            String imageUrl = uploadService.uploadFileToServer(categoryRequestDTO.getFile());

        Category category = iCategoryRepository.save(Category.builder()
                .id(categoryRequestDTO.getId())
                .categoryName(categoryRequestDTO.getCategoryName())
                .description(categoryRequestDTO.getDescription())
                        .image(imageUrl)
                .status(categoryRequestDTO.isStatus())
                        .createdAt(LocalDate.now())
                .build());

        Map<String, Category> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add new category successfully", category);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        responseDTOMap), HttpStatus.CREATED);
    }

    @Override
    public List<CategoryResponseDTO> findAllByStatus(boolean status) {
        List<Category> categories = iCategoryRepository.findAllByStatus(status);
        return categories.stream().map(CategoryResponseDTO::new).toList();

    }

    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> categories = iCategoryRepository.findAll();
        return categories.stream().map(CategoryResponseDTO::new).toList();
    }

    @Override
    public ResponseEntity<?> edit(Long id, CategoryRequestDTO categoryRequestDTO) throws CustomException {
        categoryRequestDTO.setId(id);
        CategoryResponseDTO editCategoryResponseDTO = findById(categoryRequestDTO.getId());
        boolean categoryExist = iCategoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName());

        if (categoryExist) {
            throw new CustomException("Category name has been already existed", HttpStatus.CONFLICT);
        }
        String imageUrl = editCategoryResponseDTO.getImage();
        if(!categoryRequestDTO.getFile().isEmpty()) {
             imageUrl = uploadService.uploadFileToServer(categoryRequestDTO.getFile());
        }

        Category category = iCategoryRepository.save(Category.builder()
                .id(categoryRequestDTO.getId())
                .categoryName(categoryRequestDTO.getCategoryName())
                .description(categoryRequestDTO.getDescription())
                .image(imageUrl)
                        .createdAt(editCategoryResponseDTO.getCreatedAt())
                .status(categoryRequestDTO.isStatus())
                .createdAt(LocalDate.now())
                .build());
//        Map<String, Category> responseDTOMap = new HashMap<>();
//        responseDTOMap.put("Edit category successfully", category);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        category), HttpStatus.CREATED);

    }

    @Override
    public CategoryResponseDTO findById(Long id) throws CustomException {
        Category category = iCategoryRepository.findById(id).orElseThrow(() -> new CustomException("Category is not found!!" + id, HttpStatus.NOT_FOUND));

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .image(category.getImage())
                .status(category.isStatus())
                .createdAt(category.getCreatedAt())
                .products(category.getProducts())
                .build()
                ;
    }

    @Override
    public ResponseEntity<?> getCategoryById(Long id) throws CustomException {
        CategoryResponseDTO categoryPage = findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                categoryPage
        ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id) {
        iCategoryRepository.changStatus(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "categoryStatus change successfully !!\""
        ), HttpStatus.OK);

    }
}
