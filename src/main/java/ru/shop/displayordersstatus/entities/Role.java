package ru.shop.displayordersstatus.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Role {
    @XmlAttribute(name = "id")
    private long id;
    @XmlAttribute(name = "code")
    private long code;
    @XmlAttribute(name = "name")
    private String name;
}
