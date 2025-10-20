package com.example.tacocloud.tacos.data;

import com.example.tacocloud.tacos.Taco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends JpaRepository<Taco, Long> {
}
