package com.risi.learningreactive.webfluxlearning.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ItemCapped {

    @Id
    private String id;
    private String description;
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var item = (ItemCapped) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
