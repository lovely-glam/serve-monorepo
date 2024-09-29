package com.lovelyglam.database.repository;

import com.lovelyglam.database.model.entity.ShopService;

import java.math.BigDecimal;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface NailServiceRepository extends BaseRepository<ShopService, BigDecimal> {
    default Page<ShopService> searchByParameterAndShopId(Map<String, String> param, Pageable pageable, BigDecimal shopId) {
        Specification<ShopService> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (param != null && !param.isEmpty()) {
                for (Map.Entry<String, String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)), "%" + item.getValue().toLowerCase() + "%"));
                }
            }

            predicates.add(criteriaBuilder.equal(root.get("shopProfile").get("id"), shopId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec, pageable);
    }
}
