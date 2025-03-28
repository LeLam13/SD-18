package com.example.demo.Service.impl;

import com.example.demo.entity.taikhoan;
import com.example.demo.entity.vaitro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    com.example.demo.repo.taikhoanRepo taikhoanRepo;
    PasswordEncoder pe = new BCryptPasswordEncoder();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        taikhoan taikhoan =
                taikhoanRepo.findById(username).orElse(null);
        if(taikhoan != null){
            vaitro vaitro = taikhoan.getVaiTro();
            return User
                    .withUsername(username)
                    .password(taikhoan.getPassword())
                    .roles(vaitro.getMa())
                    .build();
        }

        throw new UsernameNotFoundException("No user with username:" + username);
    }
}
