package com.glodenlaundry.matcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glodenlaundry.matcher.documents.RideDocument;
import com.glodenlaundry.matcher.documents.RideRepo;
import com.glodenlaundry.matcher.documents.RideStatus;
import io.nats.client.Connection;
import io.nats.client.impl.NatsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class EndRideApi {

    private final RideRepo rideRepo;

    private final Connection natsConnection;

    @PatchMapping("/rides/{id}/end")
    public Mono<RideDocument> getRideInfo(@PathVariable String id) {

        return rideRepo.findById(id)
                .flatMap(rideDocument -> {
                    rideDocument.setStatus(RideStatus.ENDED);
                    return Mono.zip(this.rideRepo.save(rideDocument), Mono.create(monoSink -> {
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
