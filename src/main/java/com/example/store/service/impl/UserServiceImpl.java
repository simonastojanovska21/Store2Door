package com.example.store.service.impl;

import com.example.store.model.User;
import com.example.store.model.enums.Role;
import com.example.store.model.exceptions.InvalidUsernameException;
import com.example.store.model.exceptions.InvalidUsernameOrPasswordException;
import com.example.store.model.exceptions.PasswordsDoNotMatchException;
import com.example.store.repository.UserRepository;
import com.example.store.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password,String repeatedPassword, String name, String surname, String address, String city, String telephone, String image, Role role) {

        if(username==null || password==null || username.isEmpty() || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if(!password.equals(repeatedPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new InvalidUsernameException();
        String encrypted=this.passwordEncoder.encode(password);
        User user=new User(username,encrypted,name,surname,address,city,telephone,image,role);
        return this.userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameException::new);
        UserDetails userDetails=new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList()));
        return userDetails;
    }
}
