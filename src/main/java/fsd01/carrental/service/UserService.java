package fsd01.carrental.service;

import fsd01.carrental.entity.User;
import fsd01.carrental.object.UserDTO;
import fsd01.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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


    public boolean userEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber
                .replaceAll("[\\s()-]", "")
                .matches("^\\d{10}$");
    }

    @Transactional
    public User saveUser(User user) {
        // save user in db
        return userRepository.save(user);
    }

    public User registerUser(UserDTO userDTO) {
        // encode user password
        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        User user = new User();

        // map userDTO to user
        modelMapper.map(userDTO, user);
        return saveUser(user);
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
