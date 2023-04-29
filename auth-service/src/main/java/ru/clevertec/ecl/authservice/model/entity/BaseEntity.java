package ru.clevertec.ecl.authservice.model.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> extends Serializable {

    K getId();
}
