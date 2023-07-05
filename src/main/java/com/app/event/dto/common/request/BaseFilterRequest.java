package com.app.event.dto.common.request;

import com.app.event.config.BaseConstants;
import com.app.event.util.TrimString;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseFilterRequest<T> {
    @JsonDeserialize(using = TrimString.class)
    private String sortBy;

    private Sort.Direction sortDir = Sort.Direction.ASC;

    public Sort getSort() {

        return sortBy != null
                ? Sort.by(sortDir, sortBy)
                : Sort.unsorted();
    }

    public abstract Specification<T> getSpecification();
}
