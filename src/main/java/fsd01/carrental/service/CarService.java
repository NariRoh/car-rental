package fsd01.carrental.service;

import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.User;
import fsd01.carrental.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final CarRepository carRepository;

    public List<Car> getCarList() {
        return carRepository.findAll();
    }
}
