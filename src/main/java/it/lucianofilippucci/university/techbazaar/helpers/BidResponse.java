package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {
    private boolean isAccepted; // if isAccepted is true, no one won. if it's false someone won and in winnerName there will be the winner name.
    private String cause;
    private String error;

    public BidResponse setIsAccepted(boolean isAccepted) { this.isAccepted = isAccepted; return this; }
    public BidResponse setCause(String cause) { this.cause = cause; return this; }

    public BidResponse setErrorMessage(String error) { this.error = error; return this;}
}
