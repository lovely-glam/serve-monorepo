package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.Appointment;

@Repository
public interface AppointmentRepository extends BaseRepository<Appointment, BigDecimal> {
    
}
