package ru.clevertec.ecl.newsservice.model.entity;

import java.io.Serializable;

/**
 * Base entity to set ID extends Serializable
 *
 * @author Konstantin Voytko
 */
public interface BaseEntity<K extends Serializable> extends Serializable {

    K getId();
}
