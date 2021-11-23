package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tacos.Ingredient.Type.*;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", WRAP),
                new Ingredient("COTO", "Corn Tortilla", WRAP),
                new Ingredient("GRBF", "Ground Beef", PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );
        Type[] types = values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
        model.addAttribute("design", new Taco());
        return "design";

    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }


    /**
     * @param design
     * @param errors
     * @return
     * @Valid 注释告诉 Spring MVC 在提交的 Taco 对象绑定到提交的表单数据之后，以及调用 processDesign() 方法之前，
     * 对提交的 Taco 对象执行验证。如果存在任何验证错误，这些错误的详细信息将在传递到 processDesign() 的错误对象中捕获。
     * processDesign() 的前几行查询 Errors 对象，询问它的 hasErrors() 方法是否存在任何验证错误。
     * 如果有，该方法结束时不处理 Taco，并返回 “design” 视图名，以便重新显示表单。
     */
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors) {
        if (errors.hasErrors()) {
            return "design";
        }
        // Save the taco design...
        // We'll do this in chapter 3
        log.info("Processing design: " + design);
        return "redirect:/orders/current";
    }

}
