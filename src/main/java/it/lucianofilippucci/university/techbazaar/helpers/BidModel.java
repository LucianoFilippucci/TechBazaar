package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BidModel {
    private int userId;
    private LocalDateTime bidDate;
    private float price;
}
