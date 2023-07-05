package com.app.event.dto.account.request;

import com.app.event.dto.common.request.BasePageFilterRequest;
import com.app.event.entity.Account;
import com.app.event.enums.Role;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetAllEventManagersRequest extends BasePageFilterRequest<Account> {

    private String query;

    @Override
    public Specification<Account> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(Account.Fields.role), Role.EVENT_MANAGER));

            if (!StringUtils.isEmpty(query)) {
                query = "%" + query.trim() + "%";
                List<Predicate> queryPredicates = new ArrayList<>();
                queryPredicates.add(cb.like(root.get(Account.Fields.name), query));
                queryPredicates.add(cb.like(root.get(Account.Fields.username), query));
                queryPredicates.add(cb.like(root.get(Account.Fields.email), query));
                queryPredicates.add(cb.like(root.get(Account.Fields.phone), query));

                predicates.add(cb.or(queryPredicates.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
