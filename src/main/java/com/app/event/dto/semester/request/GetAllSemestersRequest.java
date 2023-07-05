package com.app.event.dto.semester.request;

import com.app.event.dto.common.request.BaseFilterRequest;
import com.app.event.entity.*;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class GetAllSemestersRequest extends BaseFilterRequest<Semester> {

    private String query;

    @Override
    public Specification<Semester> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(query)) {
                query = "%" + query.trim() + "%";
                List<Predicate> queryPredicates = new ArrayList<>();
                queryPredicates.add(cb.like(root.get(Semester.Fields.enName), query));
                queryPredicates.add(cb.like(root.get(Semester.Fields.vnName), query));

                predicates.add(cb.or(queryPredicates.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
