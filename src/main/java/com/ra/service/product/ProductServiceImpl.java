package com.ra.service.product;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.ProductEditRequestDTO;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.BrandResponseDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import com.ra.model.entity.Image;
import com.ra.model.entity.Product;
import com.ra.repository.IProductRepository;
import com.ra.service.UploadService;
import com.ra.service.brand.IBrandService;
import com.ra.service.category.ICategoryService;
import com.ra.service.image.ImageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private IBrandService brandService;
    @Override
    public ResponseEntity<?> getProduct(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<ProductResponseDTO> productPage;
        if (keyword != null) {
            productPage = searchByNameWithPaginationAndSort( keyword, pageable);
        } else {
            productPage = findAllWithPaginationAndSort(pageable);

        }
        if (productPage == null || productPage.isEmpty()) {
            throw new CustomException("productPage is not found", HttpStatus.NOT_FOUND);
        }
        PageDataDTO<ProductResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(productPage.getNumber());
        pageDataDTO.setTotalPage(productPage.getTotalPages());
        pageDataDTO.setLimit(productPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(productPage.getContent());
        pageDataDTO.setTotalElement(productPage.getTotalElements());
        ;
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<ProductResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Product> list = productRepository.findAll(pageable);
        return list.map(ProductResponseDTO::new);
    }

    @Override
    public Page<ProductResponseDTO> searchByNameWithPaginationAndSort(String search, Pageable pageable) {
        Page<Product> list = productRepository.findProductByProductName( search, pageable);
        return list.map(ProductResponseDTO::new);
    }

    @Override
    public ProductResponseDTO findById(Long id) throws CustomException {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException("Product is not found with this id " + id, HttpStatus.NOT_FOUND));
        return new ProductResponseDTO(product);
    }

    @Override
    public ResponseEntity<?> save(ProductRequestDTO productRequestDTO) throws CustomException {
        // Kiểm tra nếu sản phẩm đã tồn tại
        if (productRepository.existsByProductName(productRequestDTO.getProductName())) {
            throw new CustomException("Product's name exists", HttpStatus.CONFLICT); // HttpStatus.CONFLICT phù hợp hơn cho xung đột dữ liệu
        }

        // Kiểm tra nếu hình ảnh tồn tại
        if (productRequestDTO.getImage() == null || productRequestDTO.getImage().getSize() == 0) {
            throw new CustomException("File image is not found", HttpStatus.BAD_REQUEST); // HttpStatus.BAD_REQUEST phù hợp hơn
        }

        // Kiểm tra nếu bộ ảnh phụ tồn tại
        if (productRequestDTO.getImageSet() == null || productRequestDTO.getImageSet().isEmpty()) {
            throw new CustomException("File imageSub is not found", HttpStatus.BAD_REQUEST); // HttpStatus.BAD_REQUEST phù hợp hơn
        }

        // Khởi tạo List để lưu trữ các đối tượng Image của ảnh phụ
        List<Image> subImages = new ArrayList<>();

        // Tải lên tập tin hình ảnh chính
        String fileName = uploadService.uploadFileToServer(productRequestDTO.getImage());

        // Tải lên các tập tin hình ảnh phụ và thêm vào subImages
        productRequestDTO.getImageSet().forEach(imageFile -> {
            String subImageFileName = uploadService.uploadFileToServer(imageFile);
            Image subImage = new Image();
            subImage.setSrc(subImageFileName);
            subImages.add(subImage);
        });

        // Tìm kiếm danh mục bằng ID
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(productRequestDTO.getCategoryId());
        if (categoryResponseDTO == null) {
            throw new CustomException("Category not found", HttpStatus.NOT_FOUND);
        }
        // Tìm kiếm brand bằng ID
        BrandResponseDTO brandResponseDTO = brandService.findById(productRequestDTO.getBrandId());
        if (brandResponseDTO == null) {
            throw new CustomException("Brand not found", HttpStatus.NOT_FOUND);
        }

        // Chuyển đổi từ CategoryResponseDTO sang Category
        Category category = Category.builder()
                .id(categoryResponseDTO.getId())
                .categoryName(categoryResponseDTO.getCategoryName())
                .status(categoryResponseDTO.isStatus())
                .products(categoryResponseDTO.getProducts())
                .build();
        // Chuyển đổi từ BrandResponseDTO sang Brand
        Brand brand = Brand.builder()
                .id(brandResponseDTO.getId())
                .brandName(brandResponseDTO.getBrandName())
                .status(brandResponseDTO.isStatus())
                .products(brandResponseDTO.getProducts())
                .build();

        // Tạo và lưu sản phẩm
        Product product = Product.builder()
                .id(productRequestDTO.getId())
                .productName(productRequestDTO.getProductName())
                .description(productRequestDTO.getDescription())
                .sku(RandomStringUtils.randomAlphanumeric(8))
                .status(productRequestDTO.isStatus())
                .image(fileName)
                .brand(brand)
                .category(category)
                .images(subImages) // Thêm các ảnh phụ vào sản phẩm
                .createdAt(productRequestDTO.getCreatedAt())
                .build();

        // Lưu sản phẩm vào cơ sở dữ liệu
        product = productRepository.save(product);
        // Gán product vào từng đối tượng Image và lưu lại
        for (Image image : subImages) {
            image.setProduct(product);
            imageService.saveOrUpdate(image);
        }
      ProductResponseDTO productResponseDTO=  new ProductResponseDTO(product);
        // Tạo phản hồi
        Map<String, ProductResponseDTO> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add new product successfully", productResponseDTO);

        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        responseDTOMap), HttpStatus.CREATED);
    }



    @Override
    public  ResponseEntity<?> changeStatus(Long id) throws CustomException {
        ProductResponseDTO productResponseDTO = findById(id);
        if (productResponseDTO != null) {
            productRepository.changeStatus(id);
            return new ResponseEntity<>(new ResponseMapper<>(
                    HttpResponse.SUCCESS,
                    HttpStatus.OK.value(),
                    HttpStatus.OK.name(),
                    "productStatus change successfully !!\""
            ), HttpStatus.OK);
        } else {
            throw  new CustomException("Product is not found with this id " + id, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public List<ProductResponseDTO> findAllByStatus(boolean status) {
        List<Product> list = productRepository.findAllByStatus(status);
        return list.stream().map(ProductResponseDTO::new).toList();
    }

    @Override
    public ResponseEntity<?> editProduct(Long id, ProductEditRequestDTO productEditRequestDTO) throws CustomException {
        // Tìm sản phẩm theo id
        Product product = productRepository.findById(id).orElseThrow(() ->
                new CustomException("Product not found", HttpStatus.NOT_FOUND));

        // Cập nhật thông tin sản phẩm từ ProductRequestDTO
        if (productEditRequestDTO.getProductName() != null && !productEditRequestDTO.getProductName().isEmpty()) {
            product.setProductName(productEditRequestDTO.getProductName());
        }
        if (productEditRequestDTO.getDescription() != null && !productEditRequestDTO.getDescription().isEmpty()) {
            product.setDescription(productEditRequestDTO.getDescription());
        }

        // Cập nhật ảnh chính nếu có
        if (productEditRequestDTO.getImage() != null && productEditRequestDTO.getImage().getSize() > 0) {
            String fileName = uploadService.uploadFileToServer(productEditRequestDTO.getImage());
            product.setImage(fileName);
        }

        // Cập nhật category nếu có
        if (productEditRequestDTO.getCategoryId() != null) {
            CategoryResponseDTO categoryResponseDTO = categoryService.findById(productEditRequestDTO.getCategoryId());
            if (categoryResponseDTO == null) {
                throw new CustomException("Category not found", HttpStatus.NOT_FOUND);
            }
            Category category = Category.builder()
                    .id(categoryResponseDTO.getId())
                    .categoryName(categoryResponseDTO.getCategoryName())
                    .status(categoryResponseDTO.isStatus())
                    .products(categoryResponseDTO.getProducts())
                    .build();
            product.setCategory(category);
        }

        // Cập nhật brand nếu có
        if (productEditRequestDTO.getBrandId() != null) {
            BrandResponseDTO brandResponseDTO = brandService.findById(productEditRequestDTO.getBrandId());
            if (brandResponseDTO == null) {
                throw new CustomException("Brand not found", HttpStatus.NOT_FOUND);
            }
            Brand brand = Brand.builder()
                    .id(brandResponseDTO.getId())
                    .brandName(brandResponseDTO.getBrandName())
                    .status(brandResponseDTO.isStatus())
                    .products(brandResponseDTO.getProducts())
                    .build();
            product.setBrand(brand);
        }

        // Cập nhật ảnh phụ nếu có
        if (productEditRequestDTO.getImageSet() != null && !productEditRequestDTO.getImageSet().isEmpty()) {
            List<Image> subImages = new ArrayList<>();
            productEditRequestDTO.getImageSet().forEach(imageFile -> {
                String subImageFileName = uploadService.uploadFileToServer(imageFile);
                Image subImage = new Image();
                subImage.setSrc(subImageFileName);
                subImages.add(subImage);
            });
            // Lưu ảnh phụ vào cơ sở dữ liệu
            subImages.forEach(imageService::saveOrUpdate);
            product.setImages(subImages);
            for (Image image : subImages) {
                image.setProduct(product);
                imageService.saveOrUpdate(image);
            }
        }


        // Lưu các thay đổi vào cơ sở dữ liệu
        product = productRepository.save(product);

        // Tạo phản hồi
        ProductResponseDTO productResponseDTO = new ProductResponseDTO(product);
        Map<String, ProductResponseDTO> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Edit product successfully", productResponseDTO);

        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        responseDTOMap), HttpStatus.OK);
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String brand, String category, Double minPrice, Double maxPrice) {
        List<Product> list = productRepository.searchProducts(brand, category, minPrice, maxPrice);
        return list.stream().map(ProductResponseDTO::new).toList();
    }

}
