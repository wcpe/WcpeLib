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

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
