package com.glodenlaundry.matcher;

import com.glodenlaundry.matcher.documents.*;
import io.nats.client.Nats;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetCustomerRideApi {

    private final RideRepo rideRepo;

    @GetMapping("/customers/rides/{id}")
    public Mono<RideDocument> getCustomerRide(@PathVariable String id) {
        return this.rideRepo.findFirstByCustomer_Id_AndStatusNotIn(id, List.of(RideStatus.PAID, RideStatus.CANCELED));
    }

}
