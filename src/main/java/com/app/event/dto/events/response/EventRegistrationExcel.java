package com.app.event.dto.events.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class EventRegistrationExcel {
    private int no;
    private String name;
    private String code;
    private String email;
    private String phone;
    private String major;
    private String checkinTime;
    private String checkoutTime;
}
