package fsd01.carrental.service;

import fsd01.carrental.entity.Review;
import fsd01.carrental.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> getReviewList() {
        return reviewRepository.getListOfReviews();
    }
}
