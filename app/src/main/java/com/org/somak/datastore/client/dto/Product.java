package com.org.somak.datastore.client.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private String productDetails;
    private int units;
    private double price;
}
