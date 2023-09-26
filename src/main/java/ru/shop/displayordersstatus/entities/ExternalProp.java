package ru.shop.displayordersstatus.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ExternalProp {
    @XmlElement(name = "Prop")
    private List<Prop> props;
}
