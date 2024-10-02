package com.lovelyglam.nailserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailPostResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.repository.ShopPostRepository;
import com.lovelyglam.nailserver.service.NailPostService;
import com.lovelyglam.utils.general.UrlUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailPostServiceImpl implements NailPostService {
    private final ShopPostRepository shopPostRepository;

    public PaginationResponse<NailPostResponse> getNailPosts (SearchRequestParamsDto requests) {
        return shopPostRepository.searchAnyByParameter(requests.search(), requests.pagination(), (item) -> {
            return NailPostResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .content(item.getContent())
                .description(item.getDescription())
                .images(UrlUtils.convertUrlStringToList(item.getImages(), ",").stream().toList())
            .build();
        });
    }
}
