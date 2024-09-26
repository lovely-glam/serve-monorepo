package com.lovelyglam.database.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.lovelyglam.database.model.dto.response.PaginationResponse;

import jakarta.persistence.criteria.Predicate;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends 
    JpaRepository<T,ID>, JpaSpecificationExecutor<T> {
        default Specification<T> searchByParameterAnySpecification(Map<String, String> param) {
        return (root, query, criteriaBuilder) -> {
            if (param == null || param.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                List<Predicate> predicates = new ArrayList<>();
                for(Map.Entry<String,String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)), "%" + item.getValue().toLowerCase() + "%"));
                }
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
            
        };
    }

    default Specification<T> searchByParameterSpecification(Map<String, String> param) {
        return (root, query, criteriaBuilder) -> {
            if (param == null || param.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                List<Predicate> predicates = new ArrayList<>();
                for(Map.Entry<String,String> item : param.entrySet()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(item.getKey()).as(String.class)), "%" + item.getValue().toLowerCase() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
    default Page<T> searchByParameter(Map<String, String> param, Pageable pageable) {
        Specification<T> spec = searchByParameterSpecification(param);
        return findAll(spec,pageable);
    }

    default Page<T> searchAnyByParameter(Map<String, String> param, Pageable pageable) {
        Specification<T> spec = searchByParameterAnySpecification(param);
        return findAll(spec,pageable);
    }

    default <D> PaginationResponse<D> searchAnyByParameter(Map<String,String> param, Pageable pageable, Function<T,D> mapper) {
        Specification<T> spec = searchByParameterAnySpecification(param);
        var entityResult = findAll(spec,pageable);
        var dtoResult = entityResult.map(mapper::apply).toList();
        return new PaginationResponse<D> (
            dtoResult,
            entityResult.getNumber(), 
            entityResult.getSize(), 
            entityResult.getNumberOfElements(), 
            entityResult.getTotalPages(), 
            entityResult.isFirst(), 
            entityResult.isLast()
        );
    }

    default <D> PaginationResponse<D> searchByParameter(Map<String,String> param, Pageable pageable, Function<T,D> mapper, Function<Map<String,String>, Specification<T>> query) {
        var entityResult = findAll(query.apply(param),pageable);
        var dtoResult = entityResult.map(mapper::apply).toList();
        return new PaginationResponse<D> (
            dtoResult,
            entityResult.getNumber(), 
            entityResult.getSize(), 
            entityResult.getNumberOfElements(), 
            entityResult.getTotalPages(), 
            entityResult.isFirst(), 
            entityResult.isLast()
        );
    }
    default Page<T> searchByParameter(Map<String,String> param, Pageable pageable, Function<Map<String,String>, Specification<T>> query) {
        return findAll(query.apply(param),pageable);
    }
}
