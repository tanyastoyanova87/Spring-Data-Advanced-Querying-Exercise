package com.example.springintro.model.entity;

import com.example.springintro.model.enums.AgeRestriction;
import com.example.springintro.model.enums.EditionType;

import java.math.BigDecimal;

public interface BookInfo {
    String getTitle();
    EditionType getEditionType();
    AgeRestriction getAgeRestriction();
    BigDecimal getPrice();
}
