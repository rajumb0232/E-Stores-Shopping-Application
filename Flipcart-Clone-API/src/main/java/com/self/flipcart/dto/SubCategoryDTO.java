package com.self.flipcart.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class SubCategoryDTO {
    private String subCategoryName;
    private List<String> productTypes;
}
