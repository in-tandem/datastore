package com.org.somak.datastore.client.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( onlyExplicitlyIncluded = true)
public class ProductKey implements Serializable, Comparable<ProductKey> {

    @EqualsAndHashCode.Include
    private String productName;


    @Override
    public int compareTo(ProductKey target) {
        if(target!=null && target.getProductName()!=null && this.getProductName()!=null)
            return this.getProductName().compareTo(target.getProductName());
        else
            return -1;
    }
}
