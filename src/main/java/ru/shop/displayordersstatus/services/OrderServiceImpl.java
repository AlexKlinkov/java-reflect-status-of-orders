package ru.shop.displayordersstatus.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.shop.displayordersstatus.dto.OrderOutputDTO;
import ru.shop.displayordersstatus.entities.*;
import ru.shop.displayordersstatus.mapper.OrderMapper;
import ru.shop.displayordersstatus.utilities.ConverterInLocalDataTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Data
@Service
@Scope("prototype")
public class OrderServiceImpl {
    private String staticLocationForSaveOutput;
    // This object which can correctly represented XML structure and
    // give opportunities to interact with data and manipulate it.
    private Document doc;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ObjectMapper objectMapper;

    // The main method of this class, rest method of this class are auxiliaries
    public ResponseEntity<String> getOrdersWithConfiguration(String externalData) {
        try {
            getObjectWithXML(externalData);
            List<OrderOutputDTO> orderOutputDTOList = getOrdersOutput(getOrdersAsJavaObjectsFromXMLStructure());
            String jsonResult = objectMapper.writeValueAsString(orderOutputDTOList);
            return ResponseEntity.ok(jsonResult);
        } catch (Throwable ex) {
            log.debug("The service has got unsuitable data from external resource for handling, " +
                    "so the service return null. Class - OrderServiceImpl, method - getOrdersWithConfiguration, " +
                                                                                "dataTime is " + LocalDateTime.now());
            System.out.println("i am here");
            return null;
        }
    }

    private void getObjectWithXML(String objAsString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Convert the XML string to an InputStream
            InputStream inputStream = new ByteArrayInputStream(objAsString.getBytes(StandardCharsets.UTF_8));
            doc = builder.parse(new InputSource(inputStream));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private List<Order> getOrdersAsJavaObjectsFromXMLStructure() {
        NodeList visitList = this.doc.getElementsByTagName("Visit");
        List<Order> result = new ArrayList<>();
        for (int i = 0; i < visitList.getLength(); i++) {
            Order order = new Order();
            order.setGuestCount(getAmountOfGuests(i));
            Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
            NodeList orderList = ordersElem.getElementsByTagName("Order");
            for (int j = 0; j < orderList.getLength(); j++) {
                Element orderElem = (Element) orderList.item(j);
                order.setVisit(Long.parseLong(orderElem.getAttribute("visit")));
                order.setOrderIdent(Long.parseLong(orderElem.getAttribute("orderIdent")));
                order.setGuid(orderElem.getAttribute("guid"));
                order.setUrl(orderElem.getAttribute("url"));
                order.setOrderName(orderElem.getAttribute("orderName"));
                if (!orderElem.getAttribute("deleted").isEmpty()) {
                    order.setDeleted(Integer.parseInt(orderElem.getAttribute("deleted")));
                }
                order.setVersion(Long.parseLong(orderElem.getAttribute("version")));
                order.setCrc32(Long.parseLong(orderElem.getAttribute("crc32")));
                order.setOrderSum(Integer.parseInt(orderElem.getAttribute("orderSum")));
                order.setUnpaidSum(Integer.parseInt(orderElem.getAttribute("unpaidSum")));
                order.setDiscountSum(Double.parseDouble(orderElem.getAttribute("discountSum")));
                order.setTotalPieces(Integer.parseInt(orderElem.getAttribute("totalPieces")));
                order.setSeqNumber(Integer.parseInt(orderElem.getAttribute("seqNumber")));
                order.setPaid(Integer.parseInt(orderElem.getAttribute("paid")));
                order.setFinished(Integer.parseInt(orderElem.getAttribute("finished")));
                order.setOpenTime(ConverterInLocalDataTime.localDataTimeFromString(
                        orderElem.getAttribute("openTime"))
                );
                order.setKdsstate(orderElem.getAttribute("kdsstate"));
                order.setFinishTime(ConverterInLocalDataTime.localDataTimeFromString(
                        orderElem.getAttribute("finishTime"))
                );
                order.setFinishTime(ConverterInLocalDataTime.localDataTimeFromString(
                        orderElem.getAttribute("billTime"))
                );
                order.setCreator(createJavaCreatorType(i, j));
                order.setWaiter(createJavaWaiterType(i, j));
                order.setOrderCategory(createJavaOrderCategoryType(i, j));
                order.setOrderType(createJavaOrderTypeType(i, j));
                order.setTable(createJavaTableType(i, j));
                order.setStation(createJavaStationType(i, j));
                order.setExternalProp(createJavaExternalPropType(i, j));
                result.add(order);
            }
        }
        return result;
    }

    private long getAmountOfGuests(int i) {
        NodeList guestsList = doc.getElementsByTagName("Guests");
        Element guestsElem = (Element) guestsList.item(i);
        String count = guestsElem.getAttributeNode("count").getValue();
        return Long.parseLong(count);
    }

    private Creator createJavaCreatorType(int i, int j) {
        Creator creator = new Creator();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element maker = getElem(orderElem, "Creator");
        creator.setId(Long.parseLong(maker.getAttribute("id")));
        creator.setCode(Long.parseLong(maker.getAttribute("code")));
        creator.setName(maker.getAttribute("name"));
        creator.setRole(createJavaRoleType("Creator", i, j));
        return creator;
    }

    private Waiter createJavaWaiterType(int i, int j) {
        Waiter result = new Waiter();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element waiter = getElem(orderElem, "Waiter");
        result.setId(Long.parseLong(waiter.getAttribute("id")));
        result.setCode(Long.parseLong(waiter.getAttribute("code")));
        result.setName(waiter.getAttribute("name"));
        result.setRole(createJavaRoleType("Waiter", i, j));
        return result;
    }

    // This method is used in createJavaCreatorType and createJavaWaiterType methods
    private Role createJavaRoleType(String type, int i, int j) {
        Role result = new Role();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element typeElem;
        if (type.equals("Creator")) {
            typeElem = getElem(orderElem, "Creator");
        } else {
            typeElem = getElem(orderElem, "Waiter");
        }
        Element role = (Element) typeElem.getElementsByTagName("Role").item(0);
        result.setId(Long.parseLong(role.getAttribute("id")));
        result.setCode(Long.parseLong(role.getAttribute("code")));
        result.setName(role.getAttribute("name"));
        return result;
    }

    private OrderCategory createJavaOrderCategoryType(int i, int j) {
        OrderCategory orderCategory = new OrderCategory();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element orderCategoryElem = (Element) orderElem.getElementsByTagName("OrderCategory").item(0);
        orderCategory.setId(Long.parseLong(orderCategoryElem.getAttribute("id")));
        orderCategory.setCode(Long.parseLong(orderCategoryElem.getAttribute("code")));
        orderCategory.setName(orderCategoryElem.getAttribute("name"));
        return orderCategory;
    }

    private OrderType createJavaOrderTypeType(int i, int j) {
        OrderType orderType = new OrderType();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element orderTypeElem = (Element) orderElem.getElementsByTagName("OrderType").item(0);
        orderType.setId(Long.parseLong(orderTypeElem.getAttribute("id")));
        orderType.setCode(Long.parseLong(orderTypeElem.getAttribute("code")));
        orderType.setName(orderTypeElem.getAttribute("name"));
        return orderType;
    }

    private Table createJavaTableType(int i, int j) {
        Table table = new Table();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element tableElem = (Element) orderElem.getElementsByTagName("Table").item(0);
        table.setId(Long.parseLong(tableElem.getAttribute("id")));
        table.setCode(Long.parseLong(tableElem.getAttribute("code")));
        table.setName(tableElem.getAttribute("name"));
        return table;
    }

    private Station createJavaStationType(int i, int j) {
        Station station = new Station();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        Element stationElem = (Element) orderElem.getElementsByTagName("Station").item(0);
        station.setId(Long.parseLong(stationElem.getAttribute("id")));
        station.setCode(Long.parseLong(stationElem.getAttribute("code")));
        station.setName(stationElem.getAttribute("name"));
        return station;
    }

    private ExternalProp createJavaExternalPropType(int i, int j) {
        ExternalProp resultExternalProps = new ExternalProp();
        Element ordersElem = (Element) doc.getElementsByTagName("Orders").item(i);
        Element orderElem = (Element) ordersElem.getElementsByTagName("Order").item(j);
        NodeList externalProps = orderElem.getElementsByTagName("Prop");
        List<Prop> props = new ArrayList<>();
        for (int k = 0; k < externalProps.getLength(); k++) {
            Element propElem = (Element) orderElem.getElementsByTagName("Prop").item(k);
            Prop prop = new Prop();
            prop.setName(propElem.getAttribute("name"));
            prop.setValue(propElem.getAttribute("value"));
            props.add(prop);
        }
        resultExternalProps.setProps(props);
        return resultExternalProps;
    }

    private Element getElem(Element orderElem, String elemName) {
        return (Element) orderElem.getElementsByTagName(elemName).item(0);
    }

    private List<OrderOutputDTO> getOrdersOutput(List<Order> orders) {
        if (orders.isEmpty()) {
            return null;
        }
        List<OrderOutputDTO> result = new ArrayList<>();
        for (Order elem : orders) {
            OrderOutputDTO orderOutputDTO = orderMapper.orderOutputDTOFromOrder(elem);
            result.add(orderOutputDTO);
        }
        return result;
    }
}
