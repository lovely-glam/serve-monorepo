package com.lovelyglam.nailserver.service;

import java.math.BigDecimal;

import com.lovelyglam.database.model.dto.request.NailPostRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailPostResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface NailPostService {
    PaginationResponse<NailPostResponse> getNailPosts (SearchRequestParamsDto requests);
    NailPostResponse getNailById (BigDecimal id);
    NailPostResponse createNailPost (NailPostRequest request);
    NailPostResponse updateNailPost (NailPostRequest request, BigDecimal id);
}
