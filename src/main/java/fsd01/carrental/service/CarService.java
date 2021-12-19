package fsd01.carrental.service;

import fsd01.carrental.entity.Car;
import fsd01.carrental.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private static final Logger log = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;

    public List<Car> getCarList() {
        return carRepository.getListOfCarsByIdAsc();
    }

    public void removeCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        carRepository.delete(car);

        log.info(">>>>>>>>>>>> Deleting a car with id: " + car.getId());
    }

    public void createCar (Car car) {
        carRepository.save(car);
        log.info(">>>>>>>>>>>> Creating a new car with id: " + car.getId());
    }

    @Transactional
    public void updateCar(Long id, Car carObj) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setModel(carObj.getModel());
        car.setMake(carObj.getMake());
        car.setFuelType(carObj.getFuelType());
        car.setMileage(carObj.getMileage());
        car.setDailyRate(carObj.getDailyRate());
        car.setMonthlyLeasingRate(carObj.getMonthlyLeasingRate());
        car.setHourlyRate(carObj.getHourlyRate());
        car.setSeats(carObj.getSeats());
        car.setTransmission(carObj.getTransmission());

        carRepository.save(car);

        log.info(">>>>>> Updating car : {}", car.toString());
    }
}
