package ru.shop.displayordersstatus.mapper;


import lombok.Data;
import org.springframework.stereotype.Component;
import ru.shop.displayordersstatus.dto.OrderOutputDTO;
import ru.shop.displayordersstatus.entities.Order;
import ru.shop.displayordersstatus.entities.OrderType;
import ru.shop.displayordersstatus.entities.Prop;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
public class OrderMapper {
    public OrderOutputDTO orderOutputDTOFromOrder(Order order) {
        if (order == null) {
            return null;
        }
        OrderOutputDTO outputDTO = new OrderOutputDTO();
        float tableOrder;
        if (!order.getOrderName().contains("/")) {
            tableOrder = Float.parseFloat(order.getOrderName());
        } else {
            tableOrder = Float.parseFloat(order.getTable().getName());
        }
        long duration = Duration.between(order.getOpenTime(), LocalDateTime.now()).toMinutes();
        // Create an instance of TableOrderDuration and set its properties
        OrderOutputDTO.TableOrderDuration tableDuration = new OrderOutputDTO.TableOrderDuration();
        tableDuration.setTableAndOrder(tableOrder);
        // Set the TableOrderDuration instance in the outputDTO
        outputDTO.setOrder(tableDuration);

        // Now, set properties (VisualFormattingProperties)
        OrderOutputDTO.VisualFormattingProperties visualProperties = new OrderOutputDTO.VisualFormattingProperties();
        boolean isFlickerFrame = isFlickerFrame(duration);
        visualProperties.setColorOfFrame(getColorFrame(isFlickerFrame)); // Set color of frame (red or black)
        visualProperties.setFlickerFrame(isFlickerFrame); // Set isFlickerFrame (true or false)
        OrderType orderType = order.getOrderType();
        int unPaidSum = order.getUnpaidSum();
        List<Prop> properties = order.getExternalProp().getProps();
        String status = getStatus(
                getValueOfProperty0C5E(properties), orderType.getName(), unPaidSum);
        visualProperties.setStatus(status);
        visualProperties.setColorOfElem(getColorNameOfOrderElem(status));
        visualProperties.setOrderType(orderType);
        // Set the VisualFormattingProperties instance in the outputDTO
        outputDTO.setProperties(visualProperties);
        return outputDTO;
    }

    private boolean isFlickerFrame(long duration) {
        return duration >= 15;
    }

    private String getColorFrame(boolean isFlickerFrame) {
        return isFlickerFrame ? "#FF0000" : "#000000";
    }
    private String getStatus(String valueOfProperty0C5E, String orderTypeName, int unpaidSum) {
        if (valueOfProperty0C5E != null) {
            if (isOrderPaid(unpaidSum)) { // Оплачен
                return switch (valueOfProperty0C5E) {
                    case "1" -> "Принят, оплачен"; // color is "Turquoise"
                    case "2" -> "Готов, оплачен"; // color is "MediumBlue"
                    default -> // case '3'
                            "Выдан, оплачен"; // color is "DarkViolet"
                };
            } else { // Не оплачен
                return switch (valueOfProperty0C5E) {
                    case "1" -> "Принят, не оплачен!"; // color is "Turquoise"
                    case "2" -> "Готов, не оплачен!"; // color is "MediumBlue"
                    default -> // case '3'
                            "Выдан, не оплачен!"; // color is "DarkViolet"
                };
            }
        }
        if (orderTypeName.equals("На месте")) {
            return isOrderPaid(unpaidSum) ? "На месте, оплачен" : "На месте, не оплачен!"; // color is "Lime" or
                                                                                                    // "ForestGreen"
        }
        if (orderTypeName.equals("Доставка")) {
            return isOrderPaid(unpaidSum) ? "Доставка, оплачена" : "Доставка, не оплачена!"; // color is "Red"
        }
        return isOrderPaid(unpaidSum) ? "С собой, оплачено" : "С собой, не оплачено!"; // color is "Silver"
    }

    // This method is used in method getStatus
    private boolean isOrderPaid(int unpaidSum) {
        return unpaidSum == 0;
    }
    // This method is used in method getStatus
    private String getValueOfProperty0C5E(List<Prop> props) {
        for (Prop prop : props) {
            if (prop.getName().endsWith("0C5E}")) {
                return prop.getValue();
            }
        }
        return null;
    }
    private String getColorNameOfOrderElem(String status) {
        if (status.contains("Принят")) {
            return "#40E0D0";
        } else if (status.contains("Готов")) {
            return "#0000CD";
        } else if (status.contains("Выдан")) {
            return "#9400D3";
        } else if (status.contains("На месте, оплачен")) {
            return "#00FF00";
        } else if (status.contains("На месте, не оплачен!")) {
            return "#228B22";
        } else if (status.contains("Доставка")) {
            return "#F08080";
        } else {
            return "#C0C0C0";
        }
    }
}
