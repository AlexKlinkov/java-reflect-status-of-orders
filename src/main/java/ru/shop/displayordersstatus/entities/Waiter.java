package ru.shop.displayordersstatus.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Waiter {
    @XmlAttribute(name = "id")
    private long id;
    @XmlAttribute(name = "code")
    private long code;
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "Role")
    private Role role;
}
