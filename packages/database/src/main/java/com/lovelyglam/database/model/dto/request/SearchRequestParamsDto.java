package com.lovelyglam.database.model.dto.request;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.utils.general.TextUtils;

public class SearchRequestParamsDto {
    private Map<String, String> search;
    private Pageable pageable;
    public SearchRequestParamsDto() {
        this.search = new HashMap<>();
    }

    private SearchRequestParamsDto(Map<String, String> search, Pageable pageable) {
        this.search = search;
        this.pageable = pageable;
    }

    public Map<String, String> search() {
        return this.search;
    }

    public String searchByField(String field){
        return this.search.get(field);
    }

    public Pageable pagination () {
        return this.pageable;
    }
    public static class SearchRequestParamsDtoBuilder {
        private Map<String, String> search;
        private Pageable pageable;

        public SearchRequestParamsDtoBuilder() {
            this.search = new HashMap<>();
        }


        public SearchRequestParamsDtoBuilder search(String queryString) {
            if (queryString == null || queryString.isEmpty()) {
                this.search = new HashMap<>();
            } else {
                try {
                    String decodedQuery = URLDecoder.decode(queryString, "UTF-8");
                    String[] queryPairs = decodedQuery.split("&");
                    Map<String, String> queryParams = new HashMap<>();
                    for (String queryPair : queryPairs) {
                        String[] parts = queryPair.split("=");
                        if (parts.length == 2) {
                            queryParams.put(parts[0], parts[1]);
                        }
                    }
                    this.search = TextUtils.convertKeysToCamel(queryParams);
                } catch (Exception e) {
                    throw new ValidationFailedException("The query is not valid");
                }
            }
            return this;
        }

        public SearchRequestParamsDtoBuilder wrapSort(Pageable ipPageable) {
            int page = ipPageable.getPageNumber();
            int pageSize = ipPageable.getPageSize();
            Sort sort = ipPageable.getSort();
            var orderList = sort.get().map(o -> {
                String prop = o.getProperty();
                Direction a = o.getDirection();
                String camelProp = TextUtils.kebabToCamel(prop);
                Order camelOrder = null;
                if(a.isAscending()) {
                    camelOrder = Order.asc(camelProp);
                }else {
                    camelOrder = Order.desc(camelProp);
                }
                return camelOrder;
            }).toList();
            Sort camelSort = Sort.by(orderList);
            this.pageable = PageRequest.of(page, pageSize, camelSort);
            return this;
        }

        public SearchRequestParamsDtoBuilder pageable(Pageable pageable) {
            this.pageable = pageable;
            return this;
        }

        public SearchRequestParamsDto build() {
            if (this.pageable == null) {
                this.pageable = PageRequest.of(0, 10);
            }
            return new SearchRequestParamsDto(search, pageable);
        }
    }
    public static SearchRequestParamsDtoBuilder builder() {
        return new SearchRequestParamsDtoBuilder();
    }
}
