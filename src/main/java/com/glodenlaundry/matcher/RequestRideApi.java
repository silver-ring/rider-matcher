package com.glodenlaundry.matcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glodenlaundry.matcher.documents.DriverRepo;
import com.glodenlaundry.matcher.documents.RideDocument;
import com.glodenlaundry.matcher.documents.RideRepo;
import com.glodenlaundry.matcher.documents.RideStatus;
import io.nats.client.Connection;
import io.nats.client.impl.NatsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RequestRideApi {

    private final RideRepo rideRepo;

    private final DriverRepo driverRepo;

    private final Connection natsConnection;

    @GetMapping("/rides/near")
    public Mono<RideDocument> requestRide(@RequestParam String driverId, @RequestParam double lon, @RequestParam double lat) {

        return Mono.zip(
                rideRepo.findFirstByPickupLocationNearAndStatus(
                        new GeoJsonPoint(lon, lat),
                        new Distance(10),
                        RideStatus.REQUESTED
                ),
                driverRepo.findById(driverId),
                (rideDocument, driverDocument) -> {
                    rideDocument.setStatus(RideStatus.ON_HOLD);
                    rideDocument.setDriver(driverDocument);
                    return rideDocument;
                }
        ).flatMap(rideDocument -> {
            return Mono.zip(this.rideRepo.save(rideDocument), Mono.create(monoSink -> {
                var id = rideDocument.getId();
                try {
                    var updatedObject = new ObjectMapper().writer().writeValueAsBytes(rideDocument);
                    natsConnection.publish(String.format("rides.%s",id), updatedObject);
                } catch (JsonProcessingException e) {
                    natsConnection.publish(String.format("rides.%s",id), new byte[]{});
                }
                monoSink.success(id);
            }), (rideDoc, senderResult) -> {
                return rideDoc;
            });
        });
    }

}
