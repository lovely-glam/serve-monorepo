package com.lovelyglam.database.model.entity;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BaseEntity {
    private BigDecimal id;
    private Date createdDate;
    private Date updatedDate;
}
