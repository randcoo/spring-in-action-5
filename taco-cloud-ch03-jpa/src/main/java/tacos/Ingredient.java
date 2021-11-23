package tacos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Data 隐式地添加了一个必需的有参构造函数，
// 但是当使用 @NoArgsConstructor 时，该构造函数将被删除。
// 显式的 @RequiredArgsConstructor 确保除了私有无参数构造函数外，仍然有一个必需有参构造函数。
@Data
@RequiredArgsConstructor
//JPA 要求实体有一个无参构造函数，所以 Lombok 的 @NoArgsConstructor 实现了这一点
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class Ingredient {

    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}