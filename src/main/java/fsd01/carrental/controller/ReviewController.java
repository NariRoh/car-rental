package fsd01.carrental.controller;

import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Review;
import fsd01.carrental.entity.User;
import fsd01.carrental.repository.BookingRepository;
import fsd01.carrental.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/reviews")
    public ModelAndView showUserReviews(@AuthenticationPrincipal User user) {
        ModelAndView mav = new ModelAndView("reviews");
        mav.addObject("reviews", reviewRepository.getListOfReviewsByUser(user.getId()));
        return mav;
    }

    private Booking currentBooking;

    @GetMapping("/review/{id}")
    public ModelAndView showReviewForm(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("leave-review");
        Review newReview = new Review();
        mav.addObject("review", newReview);
        currentBooking = bookingRepository.getBookingById(id);
        return mav;
    }

    @PostMapping("/review")
    public String handleReviewForm(@ModelAttribute @Valid Review review, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "leave-review";
        }

        review.setRating(Integer.parseInt(review.getRatingString()));
        review.setCreatedAtDate(new Date());
        review.setBooking(currentBooking);
        currentBooking.setReviewed(true);

        reviewRepository.save(review);
        bookingRepository.save(currentBooking);
        currentBooking = null;

        return "redirect:/past-bookings";
    }
}
