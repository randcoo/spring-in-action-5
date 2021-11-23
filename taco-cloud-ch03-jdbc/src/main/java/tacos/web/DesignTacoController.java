package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tacos.Ingredient.Type.*;

@Slf4j
@Controller
@RequestMapping("/design")
// @SessionAttributes 注解指定了任何模型对象，比如应该保存在会话中的 order 属性，并且可以跨多个请求使用
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private TacoRepository designRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo,
                                TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    /**
     * @ModelAttribute 注解确保在模型中能够创建 Order 对象
     * 但是与 session 中的 Taco 对象不同，这里需要在多个请求间显示订单，因此可以创建多个 Taco 并将它们添加到订单中
     * @return
     */
    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
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
    public String processDesign(@Valid Taco design, Errors errors,@ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }

}
