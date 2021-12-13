package fsd01.carrental.service;

import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.entity.User;
import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User saveUser(User user) {
        // save user in db
        return userRepository.save(user);
    }

    public User createUser (UserDTO userDTO) {
        // encode user password
        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        User user = convertDtoToEntity(userDTO);
        return saveUser(user);
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userUpdateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.update(
                userUpdateDTO.getFirstName(),
                userUpdateDTO.getLastName(),
                userUpdateDTO.getPhoneNumber()
        );

        log.info(">>>>>> Updating user : {}", user.toString());
//        User user = modelMapper.map(userUpdateDTO, User.class);
//        return saveUser(user);
//        return saveUser(convertDtoToEntity(userUpdateDTO));
    }

    public void validateEmail(String email, BindingResult bindingResult) {
        if(userRepository.findByEmail(email).isPresent()) {
            bindingResult.addError(new FieldError(
                    "userDTO", "email", "Email already in use"
            ));
        }
    }

    public void validatePhoneNumber(String phoneNumber, BindingResult bindingResult) {
        boolean isValid = phoneNumber.replaceAll("[\\s+()-]", "").matches("^\\d{10}$");

        if (!isValid) {
            bindingResult.addError(new FieldError(
                    "userDTO", "phoneNumber", "Enter a valid phone number"
            ));
        }
//        if (!isValid) {
//            bindingResult.addError(new FieldError(
//                    "userUpdateDTO", "phoneNumber", "Enter a valid phone number"
//            ));
//        }
    }

    public UserDTO getUserDTO(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return convertEntityToDTO(user);
    }

    private UserDTO convertEntityToDTO(User user) {
        // to grab all the properties in User entity
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertDtoToEntity(UserUpdateDTO userUpdateDTO) {
        // to grab all the properties in User entity
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(userUpdateDTO, User.class);
    }

    private User convertDtoToEntity(UserDTO userDTO) {
        // to grab all the properties in User entity
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(userDTO, User.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

}
