package fsd01.carrental.service;

import fsd01.carrental.entity.User;
import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exists."));

        User userRequest = convertDtoToEntity(userDTO);
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        return saveUser(user);
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

        if(!isValid) {
            bindingResult.addError(new FieldError(
                    "userDTO", "phoneNumber", "Enter a valid phone number"
            ));
        }
    }

    private UserDTO convertEntityToDTO(User user) {
        // to grab all the properties in User entity
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDTO userDTO = new UserDTO();
        userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    private User convertDtoToEntity(UserDTO userDTO) {
        // to grab all the properties in User entity
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        User user = new User();
        user = modelMapper.map(userDTO, User.class);
        return user;
    }

    public void fetchUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }


}
