package ru.shop.displayordersstatus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.shop.displayordersstatus.entities.OrderType;

@Data
public class OrderOutputDTO {

    private TableOrderDuration order;
    private VisualFormattingProperties properties;
    @Data
    public static class TableOrderDuration {
        @JsonProperty("tableAndOrder")
        private float tableAndOrder; // Order field is 'name' in XML structure
    }
    @Data
    public static class VisualFormattingProperties {
        @JsonProperty("colorOfElem")
        private String colorOfElem;
        @JsonProperty("colorOfFrame")
        private String colorOfFrame;
        @JsonProperty("status")
        private String status; // Paid or Unpaid
        @JsonProperty("isFlickerFrame")
        private boolean isFlickerFrame;
        @JsonProperty("orderType")
        private OrderType orderType;
    }
}
