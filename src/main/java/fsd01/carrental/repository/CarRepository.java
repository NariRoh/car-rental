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

}
