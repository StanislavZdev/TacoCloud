package com.example.tacocloud.tacos.web;

import com.example.tacocloud.tacos.Ingredient;
import com.example.tacocloud.tacos.Taco;
import com.example.tacocloud.tacos.TacoOrder;
import com.example.tacocloud.tacos.data.IngredientRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.example.tacocloud.tacos.Ingredient.Type;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// неявно создает логгер(из ломбока)
@Slf4j
// обозначает, что класс - контроллер
@Controller
// мапит контоллер на http сайт и привязывет к урлу /design
@RequestMapping("/design")
// добавляет в сессию обьект модели tacoOrder, он не удаляется при переходе на другие формы, чтобы удалить - SessionStatus.setComplete().
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
    }


    // авто привязка запроса http в обьект
    @ModelAttribute
    // Model model контейнер для передачи, который хранит атрибуты как ключ-значения, которые рендерятся в шаблоне
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();

        Type[] types = Ingredient.Type.values();

        for(Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType((List<Ingredient>) ingredients, type));
        }
    }

    // авто привязка запроса http в обьект
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(
            @Valid Taco taco,
            Errors errors,
            @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {

        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
