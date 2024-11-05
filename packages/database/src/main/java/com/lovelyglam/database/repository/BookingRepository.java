package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
                        @Param(value = "id") BigDecimal id,
                        @Param(value = "statuses") List<AppointmentStatus> statuses);

        @Query(value = "SELECT SUM(ss.basePrice) FROM bookings b INNER JOIN b.shopService ss INNER JOIN ss.shopProfile sp WHERE sp.id = :shopId")
        BigDecimal calculateTotalProfitOfShop(@Param(value = "shopId") BigDecimal shopId);

        @Query(value = "SELECT b FROM bookings b " +
                        "WHERE b.appointmentStatus = :appointmentStatus " +
                        "AND b.makingDay = :makingDay " +
                        "AND b.shopService.shopProfile.id = :shopId")
        Collection<Booking> getBookingsByPaymentStatusAndMakingDay(
                        BigDecimal shopId, AppointmentStatus appointmentStatus, Date makingDay);

        @Query("SELECT b FROM bookings b WHERE b.shopService.shopProfile.id = :shopId AND b.appointmentStatus = :appointmentStatus AND b.startTime = :startTime")
        Collection<Booking> getBookingsByPaymentStatusAndMakingTime(
                        @Param("shopId") BigDecimal shopId,
                        @Param("appointmentStatus") AppointmentStatus appointmentStatus,
                        @Param("startTime") Timestamp startTime);

        @Query("SELECT b FROM bookings b WHERE b.startTime BETWEEN :start AND :end")
        List<Booking> findBookingsWithinTimeRange(@Param("start") Timestamp start, @Param("end") Timestamp end);

}
