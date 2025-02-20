package com.challenge.vasconcelos.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseHistory {
    private List<AssetHistory> data;
    private Long timestamp;
}
