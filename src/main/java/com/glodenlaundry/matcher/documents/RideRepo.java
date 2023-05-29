package com.glodenlaundry.matcher.documents;

import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RideRepo extends ReactiveMongoRepository<RideDocument, String> {

    Mono<RideDocument> findFirstByPickupLocationNearAndStatus(GeoJsonPoint pickupLocation, Distance distance, RideStatus status);

    Mono<RideDocument> findFirstByCustomer_Id_AndStatusNotIn(String customerId, List<RideStatus> rideStatus);

    Mono<RideDocument> findFirstByDriver_Id_AndStatusNotIn(String driverId, List<RideStatus> rideStatus);

}
