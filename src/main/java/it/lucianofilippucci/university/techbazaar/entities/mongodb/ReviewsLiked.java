package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "liked_reviews")
public class ReviewsLiked {
    @Id
    private String id;

    private int userId;
    private List<Integer> productId;

}
