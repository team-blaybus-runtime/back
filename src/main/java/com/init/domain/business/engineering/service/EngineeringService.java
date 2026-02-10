package com.init.domain.business.engineering.service;

import com.init.application.dto.engineering.res.EngineeringPartReadRes;
import com.init.application.dto.engineering.res.EngineeringReadRes;
import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.engineering.repository.EngineeringKnowledgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EngineeringService {
    private final EngineeringKnowledgeRepository engineeringKnowledgeRepository;
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

    public List<EngineeringPartReadRes> readParts(String productTypeDesc) {
        ProductType productType = ProductType.fromDescription(productTypeDesc);
        List<EngineeringKnowledge> knowledges = engineeringKnowledgeRepository.findByProductType(productType);
        return knowledges.stream()
                .map(knowledge -> new EngineeringPartReadRes(
                        knowledge.getPartName(),
                        knowledge.getContent(),
                        knowledge.getPosition(),
                        knowledge.getAssetUrl(),
                        knowledge.getImageUrl()
                ))
                .toList();
    }

}
