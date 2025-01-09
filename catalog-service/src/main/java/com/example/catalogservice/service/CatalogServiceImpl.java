package com.example.catalogservice.service;

import com.example.catalogservice.dto.CatalogDto;
import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CatalogServiceImpl implements CatalogService{
    private final CatalogRepository catalogRepository;
    private final ModelMapper mapper;

    @Override
    public List<CatalogDto> getAllCatalogs() {
        Iterable<CatalogEntity> catalogList = catalogRepository.findAll();

        List<CatalogDto> result = new ArrayList<>();
        catalogList.forEach(
                catalog -> result.add(mapper.map(catalog, CatalogDto.class))
        );

        return result;
    }
}
