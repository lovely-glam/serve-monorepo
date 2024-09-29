package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lovelyglam.database.model.entity.ShopService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.Booking;

@Repository
public interface BookingRepository extends BaseRepository<Booking, BigDecimal> {
    default Page<Booking> searchByParameterAndUserAccountId(Map<String, String> param, Pageable pageable, BigDecimal userAccountId) {
        Specification<Booking> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (param != null && !param.isEmpty()) {
                for (Map.Entry<String, String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)), "%" + item.getValue().toLowerCase() + "%"));
                }
            }

            predicates.add(criteriaBuilder.equal(root.get("userAccount").get("id"), userAccountId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec, pageable);
    }
}
