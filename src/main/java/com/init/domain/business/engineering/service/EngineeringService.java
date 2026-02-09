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
                        extractKoreanContent(knowledge.getContent()),
                        knowledge.getAssetUrl(),
                        knowledge.getImageUrl()
                ))
                .toList();
    }

    /**
     * content 문자열에서 [KO] 태그 이후의 한글 설명만 추출합니다.
     *
     * @param content 원본 content 문자열
     * @return 한글 설명(없으면 빈 문자열)
     */
    public String extractKoreanContent(String content) {
        if (content == null) {
            return "";
        }
        int koStart = content.indexOf("[KO]");
        if (koStart == -1) {
            return "";
        }
        int start = koStart + 4; // [KO] 이후
        int enStart = content.indexOf("[EN]", start);
        if (enStart == -1) {
            return content.substring(start).trim();
        }
        return content.substring(start, enStart).trim();
    }
}
