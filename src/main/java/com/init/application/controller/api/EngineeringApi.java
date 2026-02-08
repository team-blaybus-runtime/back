package com.init.application.controller.api;

import com.init.application.dto.engineering.res.EngineeringPartReadRes;
import com.init.application.dto.engineering.res.EngineeringReadRes;
import com.init.domain.persistence.engineering.entity.ProductType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "[엔지니어링 API]")
public interface EngineeringApi {
    @Operation(summary = "엔지니어링 제품 타입 목록 조회")
    List<EngineeringReadRes> read();

    @Operation(summary = "엔지니어링 부품 목록 조회", description = "제품 타입에 해당하는 부품 목록을 조회합니다.")
    List<EngineeringPartReadRes> readParts(ProductType productType);
}
