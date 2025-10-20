package com.example.tacocloud.tacos.data;


import com.example.tacocloud.tacos.TacoOrder;
import com.example.tacocloud.tacos.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import java.util.Date;
import java.util.List;

public interface OrderRepository
        extends CrudRepository<TacoOrder, String> {

    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(
            String deliveryZip, Date startDate, Date endDate);

    Page<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
