package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetDriverRideApi {

    private final RideRepo rideRepo;

    @GetMapping("/drivers/rides/{id}")
    public Mono<RideDocument> getDriverRide(@PathVariable String id) {
        return this.rideRepo.findFirstByDriver_Id_AndStatusNotIn(id, List.of(RideStatus.PAID, RideStatus.CANCELED));
    }
}
