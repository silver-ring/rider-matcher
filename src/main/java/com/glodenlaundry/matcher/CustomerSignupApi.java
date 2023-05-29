package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.CustomerDocument;
import com.glodenlaundry.matcher.documents.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CustomerSignupApi {

    private final CustomerRepo customerRepo;
    record CustomerSignupBody(String name, String email, String phone) {}

    @PostMapping("/customers")
    public Mono<CustomerDocument> signupCustomer(@RequestBody CustomerSignupBody customerSignupBody) {
        var newCustomer = CustomerDocument.builder()
                .id(UUID.randomUUID().toString())
                .phone(customerSignupBody.phone())
                .email(customerSignupBody.email())
                .name(customerSignupBody.name()).build();
        return customerRepo.save(newCustomer);
    }

}
