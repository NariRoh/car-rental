package fsd01.carrental.service;

import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Car;
import fsd01.carrental.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;

    public List<Booking> getBookingList() {
        return bookingRepository.findAll();
    }

    public Page<Booking> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.bookingRepository.findAll(pageable);
    }
}
