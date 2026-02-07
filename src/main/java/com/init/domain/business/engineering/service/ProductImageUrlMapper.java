package com.init.domain.business.engineering.service;

import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.global.annotation.Helper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Helper
public class ProductImageUrlMapper {
    @Value("${product.drone.image.url}")
    private String droneImageUrl;
    @Value("${product.leaf_spring.image.url}")
    private String leafSpringImageUrl;
    @Value("${product.machine_vice.image.url}")
    private String machineViceImageUrl;
    @Value("${product.robot_arm.image.url}")
    private String robotArmImageUrl;
    @Value("${product.robot_gripper.image.url}")
    private String robotGripperImageUrl;
    @Value("${product.suspension.image.url}")
    private String suspensionImageUrl;
    @Value("${product.v4_engine.image.url}")
    private String v4EngineImageUrl;

    private Map<ProductType, String> typeToUrlMap;

    @PostConstruct
    void init() {
        typeToUrlMap = Map.of(
                ProductType.Drone, droneImageUrl,
                ProductType.Leaf_Spring, leafSpringImageUrl,
                ProductType.Machine_Vice, machineViceImageUrl,
                ProductType.Robot_Arm, robotArmImageUrl,
                ProductType.Robot_Gripper, robotGripperImageUrl,
                ProductType.Suspension, suspensionImageUrl,
                ProductType.V4_Engine, v4EngineImageUrl
        );
    }

    public String getImageUrlForType(ProductType type) {
        return typeToUrlMap.get(type);
    }
}
