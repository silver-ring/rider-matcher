package com.glodenlaundry.matcher.documents;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DriverRepo extends ReactiveMongoRepository<DriverDocument, String> {

}
