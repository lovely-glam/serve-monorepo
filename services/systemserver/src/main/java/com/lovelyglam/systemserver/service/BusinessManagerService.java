package com.lovelyglam.systemserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailProfileManagerResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface BusinessManagerService {
    PaginationResponse<NailProfileManagerResponse> getBusinessProfile (SearchRequestParamsDto request);
}
