package org.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingDates {
    private String checkin;
    private String checkout;
}
