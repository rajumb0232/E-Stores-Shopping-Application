package com.self.flipcart.repository;

import com.self.flipcart.model.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecificationRepo extends MongoRepository<Specification, String> {
}
