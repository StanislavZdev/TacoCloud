package com.example.tacocloud.tacos.web;


import com.example.tacocloud.tacos.TacoOrder;
import com.example.tacocloud.tacos.User;
import com.example.tacocloud.tacos.data.OrderRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;


@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")

public class OrderController {
    private final OrderRepository orderRepo;
    private final OrderProps props;

    public OrderController(OrderRepository orderRepo, OrderProps props) {
        this.orderRepo = orderRepo;
        this.props = props;
    }

    @GetMapping("/current")
    public String orderForm() {

        return "orderForm";
    }
    @GetMapping
    public String ordersForUser(
            @AuthenticationPrincipal User user, Model model) {
        Pageable pageable = (Pageable) PageRequest.of(0, props.getPageSize());
        model.addAttribute("orders", orderRepo.findByUserOrderByPlacedAtDesc(user, pageable));
        return "orderList";
    }


    @PostMapping
    public String processOrder(
            @Valid TacoOrder order,
            Errors errors,
            SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
        if(errors.hasErrors()) {
            return "orderForm";
        }

        order.setUser(user);
       

        log.info("Order submitted: {}", order);
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
