package com.app.event.dto.events.request;

import com.app.event.dto.common.request.BasePageFilterRequest;
import com.app.event.entity.*;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetAllEventsRequest extends BasePageFilterRequest<Event> {
    private String query;

    private OffsetDateTime from;

    private OffsetDateTime to;

    private Long semesterId;

    private Long majorId;

    private Long subjectId;

    @Override
    public Specification<Event> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(query)) {
                query = "%" + query.trim() + "%";
                List<Predicate> queryPredicates = new ArrayList<>();
                queryPredicates.add(cb.like(root.get(Event.Fields.enName), query));
                queryPredicates.add(cb.like(root.get(Event.Fields.vnName), query));
                queryPredicates.add(cb.like(root.get(Event.Fields.description), query));

                predicates.add(cb.or(queryPredicates.toArray(new Predicate[0])));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(Event.Fields.startTime), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(Event.Fields.startTime), to));
            }

            if (semesterId != null) {
                predicates.add(cb.equal(root.join(Event.Fields.semester).get(Semester.Fields.id), semesterId));
            }

            if (subjectId != null) {
                predicates.add(cb.equal(root.join(Event.Fields.subjects).get(Subject.Fields.id), subjectId));
            }

            if (majorId != null) {
                predicates.add(cb.equal(
                        root.join(Event.Fields.subjects)
                                .join(Subject.Fields.majors)
                                .get(Major.Fields.id), majorId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
