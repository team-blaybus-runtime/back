package com.init.domain.business.engineering.service;

import com.init.application.dto.engineering.res.EngineeringReadRes;
import com.init.domain.persistence.engineering.entity.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EngineeringService {
    private final ProductImageUrlMapper productImageUrlMapper;

    public List<EngineeringReadRes> read() {
        return Arrays.stream(ProductType.values())
                .map(type ->
                        new EngineeringReadRes(
                                type.getDescription(), productImageUrlMapper.getImageUrlForType(type)
                        )
                )
                .toList();
    }
}
