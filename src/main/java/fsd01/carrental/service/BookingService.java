package fsd01.carrental.service;

import fsd01.carrental.entity.Booking;
import fsd01.carrental.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final BookingRepository bookingRepository;


    public List<Booking> getBookingList() {
        return bookingRepository.findAll();
    }
}
