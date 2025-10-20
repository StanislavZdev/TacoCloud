package com.example.tacocloud.tacos.data;

import com.example.tacocloud.tacos.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
