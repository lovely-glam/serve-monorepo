package com.lovelyglam.systemserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ContactReportResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface ContactReportService {
    PaginationResponse<ContactReportResponse> getContacts(SearchRequestParamsDto request);
    String setReadAll();
}
