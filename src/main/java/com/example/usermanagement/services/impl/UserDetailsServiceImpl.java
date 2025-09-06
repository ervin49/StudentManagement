package com.example.usermanagement.services.impl;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findStudentByEmail(username);
        if (student == null)
            throw new UsernameNotFoundException("User with name '" + username + "' not found");

        return new UserDetailsImpl(student);
    }
}
