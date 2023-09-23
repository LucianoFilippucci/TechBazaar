package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product_images")
public class ProductResourceEntity {
    @MongoId
    private ObjectId resourceId;

    private int productId;
    private List<String> productImages;
    private List<ReviewResources> reviewImages;

    public void setReviewImages(List<String> paths, int userId) {
        if(this.reviewImages == null)
            this.reviewImages = new ArrayList<>();
        this.reviewImages.add(new ReviewResources(userId, paths));
    }
}

@Getter
@Setter
@AllArgsConstructor
class ReviewResources {
    private int userId;
    private List<String> images;
}