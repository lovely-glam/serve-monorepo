package com.lovelyglam.nailserver.service;

import java.math.BigDecimal;

import com.lovelyglam.database.model.dto.request.NailServiceRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailServiceDetailResponse;
import com.lovelyglam.database.model.dto.response.NailServiceResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface NailServiceService {
    NailServiceResponse createNailService (NailServiceRequest request);
    NailServiceResponse updateNailService (NailServiceRequest request, BigDecimal id);
    NailServiceDetailResponse getServiceDetailById (BigDecimal id);
    PaginationResponse<NailServiceResponse> getNailService (SearchRequestParamsDto request);
}
