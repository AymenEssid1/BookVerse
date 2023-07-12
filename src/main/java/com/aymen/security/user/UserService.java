package com.aymen.security.user;


import com.aymen.security.purchase.cart.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public final UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void deleteUserById(Integer userId) {
       cartRepository.deleteById(cartRepository.getCartByUserId(userId).getId());
        userRepository.deleteById(userId);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
