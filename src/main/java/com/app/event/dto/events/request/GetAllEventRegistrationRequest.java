package com.app.event.dto.events.request;

import com.app.event.dto.common.request.BaseFilterRequest;
import com.app.event.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class GetAllEventRegistrationRequest extends BaseFilterRequest<EventRegistration> {

    private String query;

    @JsonIgnore
    private Long eventId;

    @JsonIgnore
    private Long studentId;

    @Override
    public Specification<EventRegistration> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(query)) {
                query = "%" + query.trim() + "%";
                List<Predicate> queryPredicates = new ArrayList<>();
                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.student)
                        .join(Student.Fields.account)
                        .get(Account.Fields.code), query));

                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.student)
                        .join(Student.Fields.account)
                        .get(Account.Fields.name), query));

                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.student)
                        .join(Student.Fields.account)
                        .get(Account.Fields.phone), query));

                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.student)
                        .join(Student.Fields.account)
                        .get(Account.Fields.email), query));

                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.event)
                        .get(Event.Fields.vnName), query));

                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.event)
                        .get(Event.Fields.enName), query));

                queryPredicates.add(cb.like(root
                        .join(EventRegistration.Fields.event)
                        .get(Event.Fields.description), query));

                predicates.add(cb.or(queryPredicates.toArray(new Predicate[0])));
            }

            if (eventId != null) {
                predicates.add(cb.equal(root.join(EventRegistration.Fields.event).get(Event.Fields.id), eventId));
            }

            if (studentId != null) {
                predicates.add(cb.equal(root.join(EventRegistration.Fields.student).get(Student.Fields.id), studentId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
