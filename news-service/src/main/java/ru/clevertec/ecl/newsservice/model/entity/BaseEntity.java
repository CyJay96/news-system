package ru.clevertec.ecl.newsservice.model.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> extends Serializable {

    K getId();
}
