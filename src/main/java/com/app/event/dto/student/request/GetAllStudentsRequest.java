package com.app.event.dto.student.request;

import com.app.event.dto.common.request.BasePageFilterRequest;
import com.app.event.entity.*;
import com.app.event.enums.Role;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllStudentsRequest extends BasePageFilterRequest<Student> {

    private String query;

    private Long majorId;

    private Long courseId;

    @Override
    public Specification<Student> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(query)) {
                query = "%" + query.trim() + "%";
                List<Predicate> queryPredicates = new ArrayList<>();
                queryPredicates.add(cb.like(root.join(Student.Fields.account).get(Account.Fields.code), query));
                queryPredicates.add(cb.like(root.join(Student.Fields.account).get(Account.Fields.name), query));
                queryPredicates.add(cb.like(root.join(Student.Fields.account).get(Account.Fields.username), query));
                queryPredicates.add(cb.like(root.join(Student.Fields.account).get(Account.Fields.email), query));
                queryPredicates.add(cb.like(root.join(Student.Fields.account).get(Account.Fields.phone), query));

                predicates.add(cb.or(queryPredicates.toArray(new Predicate[0])));
            }

            if (majorId != null) {
                predicates.add(cb.equal(root.join(Student.Fields.major).get(Major.Fields.id), majorId));
            }

            if (courseId != null) {
                predicates.add(cb.equal(root.join(Student.Fields.courses).get(Course.Fields.id), courseId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
