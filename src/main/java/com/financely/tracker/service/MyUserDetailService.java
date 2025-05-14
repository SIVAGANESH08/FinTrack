package com.financely.tracker.service;

import com.financely.tracker.entity.MyUser;
import com.financely.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.findByUsername(username);
        if(user.isPresent()){
            MyUser userObj = user.get();
            System.out.println("Authorities: " + userObj.getRole());
            return User.builder().
                    username(userObj.getUsername()).
                    password(userObj.getPassword()).
                    roles(getRoles(userObj)).build();
        }else{
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(MyUser user){
        if(user.getRole() == null){
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
