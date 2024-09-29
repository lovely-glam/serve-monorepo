package com.lovelyglam.userserver.service.impl;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailServiceResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.entity.ShopService;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.NailServiceRepository;
import com.lovelyglam.database.repository.ShopRepository;
import com.lovelyglam.userserver.service.ShopNailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ShopNailServiceImpl implements ShopNailService {
    private final NailServiceRepository nailShopRepository;
    private final ShopRepository shopRepository;
    @Override
    public PaginationResponse<NailServiceResponse> getShopNailServices(SearchRequestParamsDto request) {
        try {
            Page<NailServiceResponse> orderPage = nailShopRepository.searchAnyByParameter(request.search(), request.pagination())
                    .map(item -> NailServiceResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .basePrice(item.getBasePrice())
                            .duration(item.getDuration())
                            .build());
            return convert(orderPage);
        } catch (Exception  ex) {
            throw new ActionFailedException(
                    String.format("Get NailServices failed with with reason: %s", ex.getMessage()));
        }
    }
    public static <T> PaginationResponse<T> convert(Page<T> page) {
        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }


    @Override
    public NailServiceResponse getShopNailServiceById(BigDecimal id) {
        ShopService shopService = nailShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found nail service with id: %s", id.toString())));
        try{
            var item = nailShopRepository.save(shopService);
            return NailServiceResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .basePrice(item.getBasePrice())
                    .duration(item.getDuration())
                    .build();
        } catch (Exception  ex) {
            throw new ActionFailedException(
                    String.format("Get NailService failed with with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public PaginationResponse<NailServiceResponse> getShopNailServicesByShopId(BigDecimal shopId, SearchRequestParamsDto request) {
        try {
            var shop = shopRepository.findById(shopId)
                    .orElseThrow(() -> new NotFoundException(String.format("Not found Shop with id: %s", shopId.toString())));

            Page<NailServiceResponse> orderPage = nailShopRepository.searchByParameterAndShopId(request.search(), request.pagination(), shopId)
                    .map(item -> NailServiceResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .basePrice(item.getBasePrice())
                            .duration(item.getDuration())
                            .build());
            return convert(orderPage);

        } catch (Exception ex) {
            throw new ActionFailedException(
                    String.format("Get shop services failed with reason: %s", ex.getMessage()));
        }
    }






}
