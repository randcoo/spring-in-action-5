package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.Ingredient;

//CRUD（创建、读取、更新、删除）操作声明了十几个方法
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}