package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.entity.Booking;

@Repository
public interface BookingRepository extends BaseRepository<Booking, BigDecimal> {

    @Query(value = "SELECT b FROM bookings b WHERE b.id = :id AND b.appointmentStatus IN :statuses")
    Collection<Booking> findBookingByStatusAndServiceId(
        @Param(value = "id")BigDecimal id, 
        @Param(value = "statuses") List<AppointmentStatus> statuses
    );
}
