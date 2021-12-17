package fsd01.carrental.service;

import fsd01.carrental.dtos.PasswordDTO;
import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.entity.User;
import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public void removeUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);

        log.info(">>>>>>>>>>>> Deleting a user with id: " + user.getId());
    }

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

    @Transactional
    public void updatePassword(PasswordDTO passwordDTO) {
        User user = userRepository.findById(passwordDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String encodedPassword = bCryptPasswordEncoder.encode(passwordDTO.getNewPassword());
        user.setPassword(encodedPassword);

        log.info(">>>>>> Updating user password: {}", user);
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
    }

    public void validatePassword(PasswordDTO passwordDTO, BindingResult bindingResult) {

        User user = userRepository.findById(passwordDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // check if given password is matched to record
        boolean result = bCryptPasswordEncoder.matches(passwordDTO.getPassword(), user.getPassword());
        System.out.println(result);
        if (!result) {
            bindingResult.addError(new FieldError(
                    "passwordDTO",
                    "password",
                    "Current password doesn't match to the record"
            ));
        }
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
