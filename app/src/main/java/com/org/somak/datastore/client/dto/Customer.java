package com.org.somak.datastore.client.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    private String customerName;
    private String gender;
    private String customerBirthDate;

}
