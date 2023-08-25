package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {
    private boolean isAccepted; // if isAccepted is true, no one won. if it's false someone won and in winnerName there will be the winner name.
    private String winnerName;
    private String error;

    public BidResponse setIsAccepted(boolean isAccepted) { this.isAccepted = isAccepted; return this; }
    public BidResponse setWinnerName(String winnerName) { this.winnerName = winnerName; return this; }

    public BidResponse setErrorMessage(String error) { this.error = error; return this;}
}
