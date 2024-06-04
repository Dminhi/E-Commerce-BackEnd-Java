package com.ra.service.config;


import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.ConfigRequestDTO;
import com.ra.model.dto.response.ConfigResponseDTO;
import com.ra.model.entity.Config;
import com.ra.repository.IConfigRepository;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements IConfigService {
@Autowired
private IConfigRepository configRepository;
    @Autowired
    private UploadService uploadService;
    @Override
    public ResponseEntity<?> getConfig(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<ConfigResponseDTO> configPage;
        if (keyword != null && !keyword.isEmpty()) {
            configPage = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            configPage = findAllWithPaginationAndSort(pageable);
        }
        if (configPage == null || configPage.isEmpty()) {
            throw new CustomException("Config is not found", HttpStatus.NOT_FOUND);

        }
        PageDataDTO<ConfigResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(configPage.getNumber());
        pageDataDTO.setTotalPage(configPage.getTotalPages());
        pageDataDTO.setLimit(configPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setTotalElement(configPage.getTotalElements());
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(configPage.getContent());
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<ConfigResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Config> list = configRepository.findAll(pageable);
        return list.map(ConfigResponseDTO::new);
    }

    @Override
    public Page<ConfigResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Config> list = configRepository.findAllByConfigNameContainingIgnoreCase(pageable, name);
        return list.map(ConfigResponseDTO::new);
    }

    @Override
    public ResponseEntity<?> save(ConfigRequestDTO configRequestDTO) throws CustomException {
        if(configRequestDTO.getId() ==null) {
            if(configRepository.existsByConfigName(configRequestDTO.getConfigName())) {
                throw new CustomException("Config name has been already existed!", HttpStatus.CONFLICT);
            }
        }
        Config config = configRepository.save(Config.builder()
                .id(configRequestDTO.getId())
                .configName(configRequestDTO.getConfigName())
                .configValue(configRequestDTO.getConfigValue())
                .status(configRequestDTO.isStatus())
                .build());
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        config), HttpStatus.CREATED);
    }

    @Override
    public List<ConfigResponseDTO> findAllByStatus(boolean status) {
        List<Config> configs = configRepository.findAllByStatus(status);
        return configs.stream().map(ConfigResponseDTO::new).toList();
    }

    @Override
    public List<ConfigResponseDTO> findAll() {
        List<Config> configs = configRepository.findAll();
        return configs.stream().map(ConfigResponseDTO::new).toList();
    }

    @Override
    public ResponseEntity<?> edit(Long id, ConfigRequestDTO configRequestDTO) throws CustomException {
        configRequestDTO.setId(id);

        boolean configExist = configRepository.existsByConfigName(configRequestDTO.getConfigName());

        if (configExist) {
            throw new CustomException("ConfigName name has been already existed", HttpStatus.CONFLICT);
        }

        Config config = configRepository.save(Config.builder()
                .id(configRequestDTO.getId())
                .configName(configRequestDTO.getConfigName())
                .configValue(configRequestDTO.getConfigValue())
                .status(configRequestDTO.isStatus())
                .build());

        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        config), HttpStatus.CREATED);
    }

    @Override
    public ConfigResponseDTO findById(Long id) throws CustomException {
        Config config = configRepository.findById(id).orElseThrow(() -> new CustomException("Config is not found!!" + id, HttpStatus.NOT_FOUND));

        return ConfigResponseDTO.builder()
                .id(config.getId())
                .configName(config.getConfigName())
                .configValue(config.getConfigValue())
                .status(config.isStatus())

                .build()
                ;
    }

    @Override
    public ResponseEntity<?> getConfigById(Long id) throws CustomException {
        ConfigResponseDTO configPage = findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                configPage
        ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id) {
        configRepository.changStatus(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "ConfigStatus change successfully !!\""
        ), HttpStatus.OK);
    }

}
