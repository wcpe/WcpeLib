package top.wcpe.wcpelib.nukkit.gui.form;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import lombok.Getter;
import top.wcpe.wcpelib.nukkit.gui.inter.ClickFunctionalInterface;

/**
 * 按钮
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-06 11:30
 */
public class WindowButton extends ElementButton {
    @Getter
    private ClickFunctionalInterface clickFunctionalInterface;

    public WindowButton(String text,ClickFunctionalInterface clickFunctionalInterface) {
        super(text);
        this.clickFunctionalInterface = clickFunctionalInterface;
    }

    public WindowButton(String text, ElementButtonImageData image,ClickFunctionalInterface clickFunctionalInterface) {
        super(text, image);
        this.clickFunctionalInterface = clickFunctionalInterface;
    }
}
