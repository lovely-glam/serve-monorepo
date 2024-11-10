package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.request.ContactReportRequest;
import com.lovelyglam.database.model.dto.response.ContactReportResponse;

public interface ContactReportService {
    public ContactReportResponse createContactReport (ContactReportRequest request);
}
