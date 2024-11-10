package com.lovelyglam.userserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.ContactReportRequest;
import com.lovelyglam.database.model.dto.response.ContactReportResponse;
import com.lovelyglam.database.model.entity.SystemContactReport;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.repository.SystemContactReportRepository;
import com.lovelyglam.userserver.service.ContactReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactReportServiceImpl implements ContactReportService {
    private final SystemContactReportRepository systemContactReportRepository;
    
    @Override
    public ContactReportResponse createContactReport(ContactReportRequest request) {
        var contactEntity = SystemContactReport.builder()
            .contactName(request.getContactName())
            .email(request.getEmail())
            .message(request.getMessage())
            .read(false)
        .build();
        try {
            var result = systemContactReportRepository.save(contactEntity);
            return ContactReportResponse.builder()
            .contactName(result.getContactName())
            .email(result.getEmail())
            .message(result.getMessage())
            .id(result.getId())
            .build();
        } catch (Exception ex) {
            throw new ActionFailedException("Failed To Save Contact", ex);
        }
    }
    
}
