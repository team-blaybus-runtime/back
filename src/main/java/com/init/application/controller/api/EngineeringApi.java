package com.init.application.controller.api;

import com.init.application.dto.engineering.res.EngineeringReadRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "[엔지니어링 API]")
public interface EngineeringApi {
    @Operation(summary = "엔지니어링 제품 타입 목록 조회")
    List<EngineeringReadRes> read();
}
