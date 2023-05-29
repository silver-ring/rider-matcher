package com.glodenlaundry.matcher.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rides")
@Getter
@Setter
@Builder
public class RideDocument {

    @Id()
    private String id;

    private CustomerDocument customer;

    private DriverDocument driver;

    @GeoSpatialIndexed
    private GeoJsonPoint pickupLocation;

    private RideStatus status;

}
