package com.lovelyglam.database.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.model.entity.SystemContactReport;

@Repository
public interface SystemContactReportRepository extends BaseRepository<SystemContactReport, String> {
    @Modifying
    @Transactional
    @Query("UPDATE system_contact_report scr SET scr.read = true WHERE scr.id = :id")
    int markAsRead(String id);

    @Modifying
    @Transactional
    @Query("UPDATE system_contact_report scr SET scr.read = true")
    int markAllAsRead();
}
