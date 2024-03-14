package com.self.flipcart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDTO {
    private String categoryName;
    private String categoryImage;
    private List<SubCategoryDTO> SubCategories;
}
