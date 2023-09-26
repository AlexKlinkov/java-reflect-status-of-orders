package ru.shop.displayordersstatus.entities;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    @XmlAttribute(name = "visit")
    private long visit;
    @XmlElement(name = "Guests")
    private long guestCount;
    @XmlAttribute(name = "orderIdent")
    private long orderIdent;
    @XmlAttribute(name = "guid")
    private String guid;
    @XmlAttribute(name = "url")
    private String url;
    @XmlAttribute(name = "orderName")
    private String orderName;
    @XmlAttribute(name = "deleted")
    private int deleted;
    @XmlAttribute(name = "version")
    private long version;
    @XmlAttribute(name = "crc32")
    private long crc32;
    @XmlAttribute(name = "orderSum")
    private int orderSum;
    @XmlAttribute(name = "unpaidSum")
    private int unpaidSum;
    @XmlAttribute(name = "discountSum")
    private double discountSum;
    @XmlAttribute(name = "totalPieces")
    private int totalPieces;
    @XmlAttribute(name = "seqNumber")
    private int seqNumber;
    @XmlAttribute(name = "paid")
    private int paid;
    @XmlAttribute(name = "finished")
    private int finished;
    @XmlAttribute(name = "openTime")
    private LocalDateTime openTime;
    @XmlAttribute(name = "kdsstate")
    private String kdsstate;
    @XmlAttribute(name = "finishTime")
    private LocalDateTime finishTime;
    @XmlAttribute(name = "billTime")
    private LocalDateTime billTime;
    @XmlElement(name = "Creator")
    private Creator creator;
    @XmlElement(name = "Waiter")
    private Waiter waiter;
    @XmlElement(name = "orderCategory")
    private OrderCategory orderCategory;
    @XmlElement(name = "OrderType")
    private OrderType orderType;
    @XmlElement(name = "Table")
    private Table table;
    @XmlElement(name = "Station")
    private Station station;
    @XmlElement(name = "ExternalProps")
    private ExternalProp externalProp;
}
