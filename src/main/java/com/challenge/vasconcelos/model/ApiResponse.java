package com.challenge.vasconcelos.model;

import java.util.List;

import lombok.Getter;

@Getter
public class ApiResponse {
    private List<Asset> data;
    private Long time;
}
