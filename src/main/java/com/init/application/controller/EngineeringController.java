package com.init.application.controller;

import com.init.application.controller.api.EngineeringApi;
import com.init.application.dto.engineering.res.EngineeringReadRes;
import com.init.domain.business.engineering.service.EngineeringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
