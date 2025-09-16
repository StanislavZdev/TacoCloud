package com.example.tacocloud.web;

import com.example.tacocloud.general.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.tacocloud.general.Ingredient.Type;
import com.example.tacocloud.general.*;


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
    // авто привязка запроса http в обьект
    @ModelAttribute
    // Model model контейнер для передачи, который хранит атрибуты как ключ-значения, которые рендерятся в шаблоне
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLT O", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Chadder", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }
    // авто привязка запроса http в обьект
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        log.info("Подготовка заказа");
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        log.info("Возвращаем тако");
        return new Taco();
    }
    @GetMapping
    public String showDesignForm() {
        log.info("Показываем форму");
        return "design";
    }
    @PostMapping
    public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }
    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
         log.info("Фильтруем по типу ингридиентов и собираем в список");
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
