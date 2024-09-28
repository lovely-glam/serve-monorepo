package com.lovelyglam.nailserver.service;

import com.lovelyglam.database.model.dto.request.ShopDetailRequest;
import com.lovelyglam.database.model.dto.response.NailProfileDetailResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;

public interface NailProfileService {
    NailProfileDetailResponse getMe ();
    ShopProfileResponse updateProfile (ShopDetailRequest shopUpdateRequest);
}
