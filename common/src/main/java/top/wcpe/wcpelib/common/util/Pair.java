package top.wcpe.wcpelib.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 由 WCPE 在 2022/8/24 9:03 创建
 * <p>
 * Created by WCPE on 2022/8/24 9:03
 * <p>
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 *
 * @author : WCPE
 * @since : v1.1.0-alpha-dev-1
 */
@Data
@AllArgsConstructor
public class Pair<A, B> implements Serializable {
    public A first;
    public B second;

    /**
     * get a new Pair
     *
     * @param a first
     * @param b second
     * @return new Pair
     */
    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    /**
     * copy a new Pair
     *
     * @param pair old pair
     * @return a new Pair
     */
    public static <A, B> Pair<A, B> of(Pair<A, B> pair) {
        return new Pair<>(pair.getFirst(), pair.getSecond());
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
