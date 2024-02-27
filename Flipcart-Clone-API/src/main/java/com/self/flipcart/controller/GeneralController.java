package com.self.flipcart.controller;

import com.self.flipcart.enums.PrimeCategory;
import com.self.flipcart.enums.State;
import com.self.flipcart.util.DistrictList;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class GeneralController {

    @GetMapping("/prime-categories")
    public ResponseEntity<List<String>> getPrimeCategories(){
        return ResponseEntity.ok(Arrays.stream(PrimeCategory.values())
                .map(primeCategory -> primeCategory.name())
                .collect(Collectors.toList()));
    }

    @GetMapping("/states")
    public ResponseEntity<List<String>> getStates(){
        return ResponseEntity.ok(Arrays.stream(State.values())
                .map(state -> state.name().toLowerCase())
                .collect(Collectors.toList()));
    }

    @GetMapping("/states/{stateName}/districts")
    public ResponseEntity<List<String>> getDistrictsByState(@PathVariable String stateName){
        return ResponseEntity.ok(Arrays.asList(DistrictList.of(stateName)));
    }
}
