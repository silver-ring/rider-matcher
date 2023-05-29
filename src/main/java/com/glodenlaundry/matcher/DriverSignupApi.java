package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.DriverDocument;
import com.glodenlaundry.matcher.documents.DriverRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DriverSignupApi {

    private final DriverRepo driverRepo;
    record DriverSignupBody(String name, String email, String phone) {}

    @PostMapping("/drivers")
    public Mono<DriverDocument> signupCustomer(@RequestBody DriverSignupBody driverSignupBody) {
        var newDriver = DriverDocument.builder()
                .id(UUID.randomUUID().toString())
                .phone(driverSignupBody.phone())
                .email(driverSignupBody.email())
                .name(driverSignupBody.name())
                .verified(false)
                .build();
        return driverRepo.save(newDriver);
    }

}
