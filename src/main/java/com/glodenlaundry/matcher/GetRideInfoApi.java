package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.RideDocument;
import com.glodenlaundry.matcher.documents.RideRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GetRideInfoApi {

    private final RideRepo rideRepo;

    @GetMapping("/rides/{id}")
    public Mono<RideDocument> getRideInfo(@PathVariable String id) {
        return this.rideRepo.findById(id);
    }
}
