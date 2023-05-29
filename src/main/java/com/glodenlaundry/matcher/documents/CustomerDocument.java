package com.glodenlaundry.matcher.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Getter
@Setter
@Builder
public class CustomerDocument {
    @Id
    String id;
    String name;
    String email;
    String phone;
}
