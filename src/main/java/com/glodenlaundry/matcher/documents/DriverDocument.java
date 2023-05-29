package com.glodenlaundry.matcher.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "drivers")
@Getter
@Setter
@Builder
public class DriverDocument {
    @Id
    String id;
    String name;
    String email;
    String phone;
    Boolean verified;
}
