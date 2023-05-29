package com.glodenlaundry.matcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glodenlaundry.matcher.documents.CustomerRepo;
import com.glodenlaundry.matcher.documents.RideDocument;
import com.glodenlaundry.matcher.documents.RideRepo;
import com.glodenlaundry.matcher.documents.RideStatus;
import io.nats.client.Connection;
import io.nats.client.impl.NatsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RequestDriverApi {

    private final RideRepo rideRepo;

    private final CustomerRepo customerRepo;

    private final Connection natsConnection;

    record LocationInfo(double lon, double lat) {
    }

    record RequestDriverBody(String customerId, LocationInfo pickupLocation) {
    }

    @PostMapping("/rides")
    public Mono<RideDocument> requestDriver(@RequestBody RequestDriverBody requestDriverBody) {

        return customerRepo.findById(requestDriverBody.customerId())
                .map(customerDocument -> RideDocument.builder()
                        .id(UUID.randomUUID().toString())
                        .customer(customerDocument)
                        .pickupLocation(new GeoJsonPoint(requestDriverBody.pickupLocation().lon(), requestDriverBody.pickupLocation().lat()))
                        .status(RideStatus.REQUESTED).build())
                .flatMap(rideDocument -> {
                    return Mono.zip(this.rideRepo.save(rideDocument), Mono.create(monoSink -> {
                        var id = rideDocument.getId();
                        try {
                            var updatedObject = new ObjectMapper().writer().writeValueAsBytes(rideDocument);
                            natsConnection.publish(String.format("rides.%s",id), updatedObject);
                        } catch (JsonProcessingException e) {
                            natsConnection.publish(String.format("rides.%s",id), new byte[]{});
                        }
                        monoSink.success(id);
                    }), (newRideDoc, senderResult) -> {
                        return newRideDoc;
                    });
                });
    }

}
