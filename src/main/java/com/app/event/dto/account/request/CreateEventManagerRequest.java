package com.app.event.dto.account.request;

import com.app.event.annotations.Phone;
import com.app.event.util.TrimString;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventManagerRequest {

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String name;

    @Phone
    @JsonDeserialize(using = TrimString.class)
    private String phone;

    @Email
    @JsonDeserialize(using = TrimString.class)
    private String email;

    @JsonDeserialize(using = TrimString.class)
    private String description;
}