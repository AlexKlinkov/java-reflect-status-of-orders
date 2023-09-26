package ru.shop.displayordersstatus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDTOForShowToFiniteUser {
    @JsonProperty("tableAndOrderNumbers")
    private float tableAndOrderNumbers;
    @JsonProperty("diffBetweenCurrentTimeAndStartPreparationOrder")
    private long diffBetweenCurrentTimeAndStartPreparationOrder;
}
