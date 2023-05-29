package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.CustomerDocument;
import com.glodenlaundry.matcher.documents.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GetCustomerApi {

    private final CustomerRepo customerRepo;

    @GetMapping("/customers/{id}")
    public Mono<CustomerDocument> getCustomer(@PathVariable String id) {
        return this.customerRepo.findById(id);
    }
}
