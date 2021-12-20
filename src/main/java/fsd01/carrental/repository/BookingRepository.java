package fsd01.carrental.repository;

import fsd01.carrental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.id = ?1")
    Booking getBookingById(Long id);

    @Query("select b from Booking b")
    List<Booking> getListOfBookings();

    @Query("select b from Booking b WHERE b.dropOffDate < current_date and b.user.id = ?1")
    List<Booking> getListOfPastBookings(Long id);

    @Query("select b from Booking b WHERE b.dropOffDate > current_date and b.user.id = ?1")
    List<Booking> getListOfUpcomingBookingsByUser(Long id);

    @Query("select b from Booking b where b.user.id = ?1")
    List<Booking> getListOfBookingsByUser(Long id);

}
