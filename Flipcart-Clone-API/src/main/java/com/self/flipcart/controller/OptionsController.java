package com.self.flipcart.controller;

import com.self.flipcart.dto.CategoryDTO;
import com.self.flipcart.dto.TopCategoryDTO;
import com.self.flipcart.enums.State;
import com.self.flipcart.enums.SubCategory;
import com.self.flipcart.enums.TopCategory;
import com.self.flipcart.util.DistrictList;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class OptionsController {

    @GetMapping("/top-categories")
    public ResponseEntity<List<String>> getPrimeCategories() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(Arrays.stream(TopCategory.values()).map(TopCategory::getName).collect(Collectors.toList()));
    }

    @GetMapping("/top-category/{primeCategory}/sub-categories")
    public ResponseEntity<List<String>> getSubCategories(@PathVariable String primeCategory){
         return ResponseEntity.ok()
                 .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                 .body(TopCategory.valueOf(primeCategory.toUpperCase()).getSubCategories()
                         .stream().map(SubCategory::getName).collect(Collectors.toList()));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<TopCategoryDTO>> getCategories(){
       List<TopCategoryDTO> response = Arrays.stream(TopCategory.values()).map(category -> TopCategoryDTO.builder()
                    .topCategoryName(category.getName())
                    .topCategoryImage("/categories/"+category.getName().toLowerCase()+"/images")
                    .categories(category.getSubCategories().stream().map(subCategory -> CategoryDTO.builder()
                                .categoryName(subCategory.getName())
                                .productTypes(new ArrayList<>()).build()
                    ).collect(Collectors.toList())).build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(15)))
                .body(response);
    }

    @GetMapping("/states")
    public ResponseEntity<List<String>> getStates() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(Arrays.stream(State.values())
                        .map(state -> state.name().toLowerCase())
                        .collect(Collectors.toList()));
    }

    @GetMapping("/states/{stateName}/districts")
    public ResponseEntity<List<String>> getDistrictsByState(@PathVariable String stateName) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(Arrays.asList(DistrictList.of(stateName)));
    }
}
