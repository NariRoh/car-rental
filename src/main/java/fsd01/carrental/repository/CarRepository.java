package fsd01.carrental.repository;

import fsd01.carrental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car c where c.id = ?1")
    Car findCarById(Long id);

    @Query("select c from Car c")
    List<Car> getListOfCars();

    @Query("select c from Car c where c.dailyRate between ?1 and ?2 and c.transmission like ?3 and c.mileage between ?4 and ?5 and c.fuelType like ?6 and c.seats between ?7 and ?8")
    List<Car> getListOfCarsSearch(double minPrice, double maxPrice, String transmission, int minMileage, int maxMileage, String fuel, int minSeats, int maxSeats);

    @Query("select c from Car c order by c.noBookings desc")
    List<Car> getIndexFeaturedCars();
}
