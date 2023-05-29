package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.DriverDocument;
import com.glodenlaundry.matcher.documents.DriverRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GetDriverApi {

    private final DriverRepo driverRepo;

    @GetMapping("/drivers/{id}")
    public Mono<DriverDocument> getDriver(@PathVariable String id) {
        return this.driverRepo.findById(id);
    }
}
