package fsd01.carrental.service;

import fsd01.carrental.entity.Review;
import fsd01.carrental.entity.User;
import fsd01.carrental.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> getReviewList() {
        return reviewRepository.getListOfReviews();
    }

    public Page<Review> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.reviewRepository.findAll(pageable);
    }
}
