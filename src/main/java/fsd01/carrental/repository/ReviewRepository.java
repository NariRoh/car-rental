package fsd01.carrental.repository;

import fsd01.carrental.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r")
    List<Review> getListOfReviews();

    @Query("select r from Review r join Booking b ON b.user.id = ?1 where r.booking = b")
    List<Review> getListOfReviewsByUser(Long userId);

    @Query("select r.rating from Review r join Booking b ON r.booking = b join Car c ON b.car = c where c.id = ?1")
    List<Integer> getRatingsOfCar(Long carId);

    @Query("select r from Review r join Booking b ON r.booking = b join Car c ON b.car = c where c.id = ?1")
    List<Review> getReviewsOfCar(Long carId);
}
