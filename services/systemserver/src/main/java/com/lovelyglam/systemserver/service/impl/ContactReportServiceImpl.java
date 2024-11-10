package com.lovelyglam.systemserver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ContactReportResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.repository.SystemContactReportRepository;
import com.lovelyglam.systemserver.service.ContactReportService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactReportServiceImpl implements ContactReportService {
    private final SystemContactReportRepository systemContactReportRepository;

    public PaginationResponse<ContactReportResponse> getContacts(SearchRequestParamsDto request) {
        var result = systemContactReportRepository.searchByParameter(request.search(), request.pagination(),
                (entity) -> {
                    return ContactReportResponse.builder()
                            .contactName(entity.getContactName())
                            .email(entity.getEmail())
                            .feedback(entity.getFeedback())
                            .message(entity.getMessage())
                            .id(entity.getId())
                            .build();
                },
                (param) -> {
                    return (root, query, criteriaBuilder) -> {
                        if (param == null || param.isEmpty()) {
                            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                        } else {
                            List<Predicate> predicates = new ArrayList<>();
                            for (Map.Entry<String, String> item : param.entrySet()) {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                                        "%" + item.getValue().toLowerCase() + "%"));
                            }
                            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                        }
                    };
                });
        return result;
    }

    @Override
    public String setReadAll() {
        int result = systemContactReportRepository.markAllAsRead();
        return String.format("Has read of %d record", result);
    }
}
