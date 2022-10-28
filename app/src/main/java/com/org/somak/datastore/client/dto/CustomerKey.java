package com.org.somak.datastore.client.dto;

import lombok.*;

import java.io.Serializable;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerKey implements Serializable, Comparable<CustomerKey> {

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String customerId;


    @Override
    public int compareTo(CustomerKey target) {
        if(target!=null && target.getCustomerId()!=null && this.getCustomerId()!=null)
            return this.getCustomerId().compareTo(target.getCustomerId());
        else
            return -1;
    }
}
