package com.glodenlaundry.matcher.documents;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepo  extends ReactiveMongoRepository<CustomerDocument, String> {

}
