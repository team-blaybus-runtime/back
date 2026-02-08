package com.init.application.controller;

import com.init.application.controller.api.EngineeringApi;
import com.init.application.dto.engineering.res.EngineeringPartReadRes;
import com.init.application.dto.engineering.res.EngineeringReadRes;
import com.init.domain.business.engineering.service.EngineeringService;
import com.init.domain.persistence.engineering.entity.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EngineeringController implements EngineeringApi {
    private final EngineeringService engineeringService;

    @Override
    @GetMapping("/engineering")
    public List<EngineeringReadRes> read() {
        return engineeringService.read();
    }

    @Override
    @GetMapping("/engineering/parts")
    public List<EngineeringPartReadRes> readParts(@RequestParam ProductType productType) {
        return engineeringService.readParts(productType);
    }
}
