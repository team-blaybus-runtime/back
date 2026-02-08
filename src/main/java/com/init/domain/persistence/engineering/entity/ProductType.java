package com.init.domain.persistence.engineering.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductType {
    Drone("Drone"),
    Leaf_Spring("Leaf Spring"),
    Machine_Vice("Machine Vice"),
    Robot_Arm("Robot Arm"),
    Robot_Gripper("Robot Gripper"),
    Suspension("Suspension"),
    V4_Engine("V4 Engine");

    private final String description;
}
