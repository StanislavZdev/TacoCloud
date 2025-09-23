package com.example.tacocloud.tacos.data;


import java.util.Optional;
import com.example.tacocloud.tacos.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
