package com.ra.service.productDetail;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.ConfigRequestDTO;
import com.ra.model.dto.request.ProductDetailRequestDTO;
import com.ra.model.dto.response.ColorResponseDTO;
import com.ra.model.dto.response.ConfigResponseDTO;
import com.ra.model.dto.response.ProductDetailResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.*;
import com.ra.repository.IConfigRepository;
import com.ra.repository.IProductDetailRepository;
import com.ra.service.UploadService;
import com.ra.service.brand.IBrandService;
import com.ra.service.category.ICategoryService;
import com.ra.service.color.IColorService;
import com.ra.service.config.IConfigService;
import com.ra.service.product.IProductService;
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
import java.util.stream.Collectors;


@Service
public class ProductDetailServiceImpl implements IProductDetailService {
    @Autowired
    private IProductDetailRepository productDetailRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private IColorService colorService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IConfigService configService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    protected IConfigRepository configRepository;

    @Override
    public ResponseEntity<?> getProductDetail(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<ProductDetailResponseDTO> productDetailPage;
        if (keyword != null && !keyword.isEmpty()) {
            productDetailPage = searchByNameWithPaginationAndSort(keyword, pageable);
        } else {
            productDetailPage = findAllWithPaginationAndSort(pageable);
        }
        if (productDetailPage == null || productDetailPage.isEmpty()) {
            throw new CustomException("ProductDetail is not found", HttpStatus.NOT_FOUND);

        }
        PageDataDTO<ProductDetailResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(productDetailPage.getNumber());
        pageDataDTO.setTotalPage(productDetailPage.getTotalPages());
        pageDataDTO.setLimit(productDetailPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setTotalElement(productDetailPage.getTotalElements());
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(productDetailPage.getContent());
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<ProductDetailResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<ProductDetail> list = productDetailRepository.findAll(pageable);
        return list.map(ProductDetailResponseDTO::new);
    }

    @Override
    public Page<ProductDetailResponseDTO> searchByNameWithPaginationAndSort(String name, Pageable pageable) {
        Page<ProductDetail> list = productDetailRepository.findAllByProductDetailNameContainingIgnoreCase(name, pageable);
        return list.map(ProductDetailResponseDTO::new);
    }

    @Override
    public ProductDetailResponseDTO findById(Long id) throws CustomException {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new CustomException("ProductDetail is not found!! " + id, HttpStatus.NOT_FOUND));
        return ProductDetailResponseDTO.builder()
                .id(productDetail.getId())
                .unitPrice(productDetail.getUnitPrice())
                .stock(productDetail.getStock())
                .image(productDetail.getImage())
                .color(productDetail.getColor().getColorName())
                .configs(productDetail.getConfigs().stream()
                        .map(Config::getConfigName)
                        .collect(Collectors.toSet()))
                .product(productDetail.getProduct().getProductName())
                .status(productDetail.isStatus())
                .build();
    }

    @Override
    public ResponseEntity<?> save(ProductDetailRequestDTO productDetailRequestDTO) throws CustomException {
        // Kiểm tra nếu tên ProductDetail đã tồn tại
        if (productDetailRequestDTO.getId() == null) {
            if (productDetailRepository.existsByProductDetailName(productDetailRequestDTO.getProductDetailName())) {
                throw new CustomException("ProductDetail name has already existed!", HttpStatus.CONFLICT);
            }
        }

        // Tải lên tập tin hình ảnh
        String imageUrl = uploadService.uploadFileToServer(productDetailRequestDTO.getImage());

        // Tìm kiếm màu sắc bằng ID
        ColorResponseDTO colorResponseDTO = colorService.findById(productDetailRequestDTO.getColorId());
        if (colorResponseDTO == null) {
            throw new CustomException("Color not found", HttpStatus.NOT_FOUND);
        }
        Color color = Color.builder()
                .id(colorResponseDTO.getId())
                .colorName(colorResponseDTO.getColorName())
                .status(colorResponseDTO.isStatus())
                .build();

        // Tạo danh sách để lưu trữ các cấu hình
        Set<Config> configSet = new HashSet<>();
        // Lưu trữ các cấu hình vào cơ sở dữ liệu và thêm vào danh sách
        for (ConfigRequestDTO configDTO : productDetailRequestDTO.getConfigs()) {
            // Tạo đối tượng Config từ DTO
            Config config = Config.builder()
                    .configName(configDTO.getConfigName())
                    .configValue(configDTO.getConfigValue())
                    .status(true) // Mặc định là true
                    .build();
            // Lưu trữ cấu hình vào cơ sở dữ liệu
            config = configRepository.save(config);
            // Thêm cấu hình vào danh sách
            configSet.add(config);
        }

        // Tìm kiếm product bằng ID
        ProductResponseDTO productResponseDTO = productService.findById(productDetailRequestDTO.getProductId());
        if (productResponseDTO == null) {
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }
        Product product = Product.builder()
                .id(productResponseDTO.getId())
                .productName(productResponseDTO.getProductName())
                .description(productResponseDTO.getDescription())
                .sku(productResponseDTO.getSku())
                .status(productResponseDTO.isStatus())
                .image(productResponseDTO.getImage())
                .brand(brandService.findBrandByBrandName(productResponseDTO.getBrand()))
                .category(categoryService.findCategoryByCategoryName(productResponseDTO.getCategory()))
                .createdAt(productResponseDTO.getCreatedAt())
                .build();

        // Tạo và lưu ProductDetail
        ProductDetail productDetail = ProductDetail.builder()
                .productDetailName(productDetailRequestDTO.getProductDetailName())
                .unitPrice(productDetailRequestDTO.getUnitPrice())
                .stock(productDetailRequestDTO.getStock())
                .image(imageUrl)
                .color(color)
                .configs(configSet)
                .product(product)
                .status(true) // Mặc định là true
                .build();

        productDetail = productDetailRepository.save(productDetail);

        // Tạo phản hồi
        ProductDetailResponseDTO productDetailResponseDTO = new ProductDetailResponseDTO(productDetail);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        productDetailResponseDTO), HttpStatus.CREATED);
    }




    @Override
    public ResponseEntity<?> changeStatus(Long id) throws CustomException {
        productDetailRepository.changeStatus(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "ProductDetail 'Status change successfully !!\""
        ), HttpStatus.OK);
    }

    @Override
    public List<ProductDetailResponseDTO> findAllByStatus(boolean status) {
        List<ProductDetail> productDetails = productDetailRepository.findAllByStatus(status);
        return productDetails.stream().map(ProductDetailResponseDTO::new).toList();
    }

    @Override
    public ResponseEntity<?> editProduct(Long id, ProductDetailRequestDTO productDetailRequestDTO) throws CustomException {
        // Tìm product detail theo id
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new CustomException("ProductDetail not found!! " + id, HttpStatus.NOT_FOUND));

        // Cập nhật thông tin từ ProductDetailRequestDTO
        if (productDetailRequestDTO.getProductDetailName() != null && !productDetailRequestDTO.getProductDetailName().isEmpty()) {
            productDetail.setProductDetailName(productDetailRequestDTO.getProductDetailName());
        }
        if (productDetailRequestDTO.getUnitPrice() > 0) {
            productDetail.setUnitPrice(productDetailRequestDTO.getUnitPrice());
        }
        if (productDetailRequestDTO.getStock() >= 0) {
            productDetail.setStock(productDetailRequestDTO.getStock());
        }

        // Cập nhật ảnh chính nếu có
        if (productDetailRequestDTO.getImage() != null && productDetailRequestDTO.getImage().getSize() > 0) {
            String imageUrl = uploadService.uploadFileToServer(productDetailRequestDTO.getImage());
            productDetail.setImage(imageUrl);
        }

        // Cập nhật color nếu có
        if (productDetailRequestDTO.getColorId() != null) {
            ColorResponseDTO colorResponseDTO = colorService.findById(productDetailRequestDTO.getColorId());
            if (colorResponseDTO == null) {
                throw new CustomException("Color not found", HttpStatus.NOT_FOUND);
            }
            Color color = Color.builder()
                    .id(colorResponseDTO.getId())
                    .colorName(colorResponseDTO.getColorName())
                    .status(colorResponseDTO.isStatus())
                    .build();
            productDetail.setColor(color);
        }

        // Cập nhật configs nếu có
        if (productDetailRequestDTO.getConfigs() != null && !productDetailRequestDTO.getConfigs().isEmpty()) {
            Set<Config> configSet = productDetailRequestDTO.getConfigs().stream()
                    .map(config -> {
                        ConfigResponseDTO configResponseDTO;
                        try {
                            configResponseDTO = configService.findById(config.getId());
                        } catch (CustomException e) {
                            throw new RuntimeException("Config not found: " + config.getId());
                        }
                        return Config.builder()
                                .id(configResponseDTO.getId())
                                .configName(configResponseDTO.getConfigName())
                                .status(configResponseDTO.isStatus())
                                .build();
                    })
                    .collect(Collectors.toSet());
            productDetail.setConfigs(configSet);
        }

        // Cập nhật product nếu có
        if (productDetailRequestDTO.getProductId() != null) {
            ProductResponseDTO productResponseDTO = productService.findById(productDetailRequestDTO.getProductId());
            if (productResponseDTO == null) {
                throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
            }
            Product product = Product.builder()
                    .id(productResponseDTO.getId())
                    .productName(productResponseDTO.getProductName())
                    .description(productResponseDTO.getDescription())
                    .sku(productResponseDTO.getSku())
                    .status(productResponseDTO.isStatus())
                    .image(productResponseDTO.getImage())
                    .brand(brandService.findBrandByBrandName(productResponseDTO.getBrand()))
                    .category(categoryService.findCategoryByCategoryName(productResponseDTO.getCategory()))
                    .createdAt(productResponseDTO.getCreatedAt())
                    .build();
            productDetail.setProduct(product);
        }

        // Lưu các thay đổi vào cơ sở dữ liệu
        productDetail = productDetailRepository.save(productDetail);

        // Tạo phản hồi
        ProductDetailResponseDTO productDetailResponseDTO = new ProductDetailResponseDTO(productDetail);


        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        productDetailResponseDTO), HttpStatus.OK);
    }

    @Override
    public ProductDetail findProductDetailById(Long id) throws CustomException {
        return productDetailRepository.findById(id).orElseThrow(() -> new CustomException("ProductDetail not found!! " + id, HttpStatus.NOT_FOUND));
    }
}
