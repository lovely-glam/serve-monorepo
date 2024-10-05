package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    default Page<Booking> searchByParameterAndShopId(Map<String, String> param, Pageable pageable, BigDecimal userAccountId) {
        Specification<Booking> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (param != null && !param.isEmpty()) {
                for (Map.Entry<String, String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)), "%" + item.getValue().toLowerCase() + "%"));
                }
            }

            predicates.add(criteriaBuilder.equal(root.get("shop_service_id").get("id"), userAccountId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec, pageable);
    }

    default Page<Booking> searchByParameterAndShop(Map<String, String> param, Pageable pageable, BigDecimal userAccountId, LocalDate makingDay) {
        Specification<Booking> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Handle search parameters
            if (param != null && !param.isEmpty()) {
                for (Map.Entry<String, String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(item.getKey()).as(String.class)),
                            "%" + item.getValue().toLowerCase() + "%"));
                }
            }

            // Filter by user account id
            predicates.add(criteriaBuilder.equal(root.get("shopService").get("id"), userAccountId));

            // Filter by makingDay if provided
            if (makingDay != null) {
                predicates.add(criteriaBuilder.equal(root.get("makingDay"), makingDay));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec, pageable);
    }

}
