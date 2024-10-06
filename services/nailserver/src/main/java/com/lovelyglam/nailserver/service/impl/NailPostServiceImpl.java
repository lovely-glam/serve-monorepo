package com.lovelyglam.nailserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.NailPostRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailPostResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.entity.ShopPost;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopPostRepository;
import com.lovelyglam.nailserver.service.NailPostService;
import com.lovelyglam.nailserver.utils.AuthUtils;
import com.lovelyglam.utils.general.UrlUtils;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailPostServiceImpl implements NailPostService {
    private final ShopPostRepository shopPostRepository;
    private final AuthUtils authUtils;

    public PaginationResponse<NailPostResponse> getNailPosts (SearchRequestParamsDto requests) {
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        return shopPostRepository.searchByParameter(requests.search(), requests.pagination(), (item) -> {
            return NailPostResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .content(item.getContent())
                .description(item.getDescription())
                .images(UrlUtils.convertUrlStringToList(item.getImages(), ",").stream().toList())
            .build();
        }, (param) -> {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("shopProfile"), shopProfile));
                for(Map.Entry<String,String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)), "%" + item.getValue().toLowerCase() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        });
    }

    public NailPostResponse getNailById (BigDecimal id) {
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        var queryResult = shopPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Not Found Post With This Id"));
        if (shopProfile.equals(queryResult.getShopProfile())) throw new ValidationFailedException("This post is not yours");
        return NailPostResponse.builder()
            .id(queryResult.getId())
            .content(queryResult.getContent())
            .description(queryResult.getDescription())
            .title(queryResult.getTitle())
            .images(UrlUtils.convertUrlStringToList(queryResult.getImages(), ",").stream().toList())
        .build();
    }

    public NailPostResponse createNailPost (NailPostRequest request) {
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        var shopPost = ShopPost.builder()
        .shopProfile(shopProfile)
        .content(request.getContent())
        .description(request.getDescription())
        .title(request.getTitle())
        .images(UrlUtils.convertUrlListToString(request.getImages(), ","))
        .build();
        try {
            var queryResult = shopPostRepository.save(shopPost);
            return NailPostResponse.builder()
            .id(queryResult.getId())
            .content(queryResult.getContent())
            .description(queryResult.getDescription())
            .title(queryResult.getTitle())
            .images(UrlUtils.convertUrlStringToList(queryResult.getImages(), ",").stream().toList())
        .build();
        } catch (Exception ex) {
            throw new ActionFailedException("Can't add nail post");
        }
    }

    @Override
    public NailPostResponse updateNailPost(NailPostRequest request, BigDecimal id) {
        var nailPostCurrent = shopPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found post with this id"));
        var shopProfile = authUtils.getUserAccountFromAuthentication().getShopProfile();
        if (!shopProfile.equals(nailPostCurrent.getShopProfile())) throw new ValidationFailedException("This post is not yours");
        nailPostCurrent.setTitle(request.getTitle());
        nailPostCurrent.setImages(UrlUtils.convertUrlListToString(request.getImages(), ","));
        nailPostCurrent.setContent(request.getContent());
        nailPostCurrent.setDescription(request.getDescription());
        try {
            var queryResult = shopPostRepository.save(nailPostCurrent);
            return NailPostResponse.builder()
            .id(queryResult.getId())
            .content(queryResult.getContent())
            .description(queryResult.getDescription())
            .title(queryResult.getTitle())
            .images(UrlUtils.convertUrlStringToList(queryResult.getImages(), ",").stream().toList())
            .build();
        } catch (Exception ex) {
            throw new ActionFailedException("Can't Update nail post");
        }
    }
}
