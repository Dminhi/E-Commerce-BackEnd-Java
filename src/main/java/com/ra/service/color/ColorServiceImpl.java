package com.ra.service.color;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.ColorRequestDTO;
import com.ra.model.dto.response.ColorResponseDTO;
import com.ra.model.dto.response.ConfigResponseDTO;
import com.ra.model.entity.Color;
import com.ra.model.entity.Config;
import com.ra.repository.IColorRepository;
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
public class ColorServiceImpl implements IColorService{
    @Autowired
    private IColorRepository colorRepository;
    @Override
    public ResponseEntity<?> getColor(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<ColorResponseDTO> colorPage;
        if (keyword != null && !keyword.isEmpty()) {
            colorPage = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            colorPage = findAllWithPaginationAndSort(pageable);
        }
        if (colorPage == null || colorPage.isEmpty()) {
            throw new CustomException("Config is not found", HttpStatus.NOT_FOUND);

        }
        PageDataDTO<ColorResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(colorPage.getNumber());
        pageDataDTO.setTotalPage(colorPage.getTotalPages());
        pageDataDTO.setLimit(colorPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setTotalElement(colorPage.getTotalElements());
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(colorPage.getContent());
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<ColorResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Color> list = colorRepository.findAll(pageable);
        return list.map(ColorResponseDTO::new);
    }

    @Override
    public Page<ColorResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Color> list = colorRepository.findAllByColorNameContainingIgnoreCase(pageable, name);
        return list.map(ColorResponseDTO::new);
    }

    @Override
    public ResponseEntity<?> save(ColorRequestDTO colorRequestDTO) throws CustomException {
        if(colorRequestDTO.getId() ==null) {
            if(colorRepository.existsByColorName(colorRequestDTO.getColorName())) {
                throw new CustomException("Color name has been already existed!", HttpStatus.CONFLICT);
            }
        }
        Color color = colorRepository.save(Color.builder()
                .id(colorRequestDTO.getId())
                .colorName(colorRequestDTO.getColorName())
                .status(colorRequestDTO.isStatus())
                .build());
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        color), HttpStatus.CREATED);
    }

    @Override
    public List<ColorResponseDTO> findAllByStatus(boolean status) {
        List<Color> colors = colorRepository.findAllByStatus(status);
        return colors.stream().map(ColorResponseDTO::new).toList();
    }

    @Override
    public List<ColorResponseDTO> findAll() {
        List<Color> colors = colorRepository.findAll();
        return colors.stream().map(ColorResponseDTO::new).toList();
    }

    @Override
    public ResponseEntity<?> edit(Long id, ColorRequestDTO colorRequestDTO) throws CustomException {
        colorRequestDTO.setId(id);

        boolean colorExist = colorRepository.existsByColorName(colorRequestDTO.getColorName());

        if (colorExist) {
            throw new CustomException("ColorName name has been already existed", HttpStatus.CONFLICT);
        }

        Color color = colorRepository.save(Color.builder()
                .id(colorRequestDTO.getId())
                .colorName(colorRequestDTO.getColorName())
                .status(colorRequestDTO.isStatus())
                .build());

        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        color), HttpStatus.CREATED);
    }

    @Override
    public ColorResponseDTO findById(Long id) throws CustomException {
        Color color = colorRepository.findById(id).orElseThrow(() -> new CustomException("Color is not found!!" + id, HttpStatus.NOT_FOUND));

        return ColorResponseDTO.builder()
                .id(color.getId())

                .colorName(color.getColorName())
                .status(color.isStatus())

                .build()
                ;
    }

    @Override
    public ResponseEntity<?> getColorById(Long id) throws CustomException {
        ColorResponseDTO colorPage = findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                colorPage
        ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id) {
        colorRepository.changStatus(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "ColorStatus change successfully !!\""
        ), HttpStatus.OK);
    }
}
