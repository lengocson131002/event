package com.app.event.dto.major.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MajorResponse {
    private Long id;

    private String image;

    private String code;

    private String name;

    private String description;
}
