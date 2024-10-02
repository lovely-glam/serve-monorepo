package com.lovelyglam.nailserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailPostResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface NailPostService {
    PaginationResponse<NailPostResponse> getNailPosts (SearchRequestParamsDto requests);
}
