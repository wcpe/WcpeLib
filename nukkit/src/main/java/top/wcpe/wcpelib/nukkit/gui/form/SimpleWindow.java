package top.wcpe.wcpelib.nukkit.gui.form;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import lombok.Getter;
import top.wcpe.wcpelib.nukkit.gui.FromWindowListner;
import top.wcpe.wcpelib.nukkit.gui.inter.SimpleCommitFunctionalInterface;
import top.wcpe.wcpelib.nukkit.gui.inter.WcpeLibWindow;

import java.util.List;

/**
 * 按钮窗口
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-06 11:17
 */
public class SimpleWindow extends FormWindowSimple implements WcpeLibWindow {
    @Getter
    private String ID;
    @Getter
    private SimpleCommitFunctionalInterface commitFunctionalInterface;

    public SimpleWindow(String ID, String title, String content, SimpleCommitFunctionalInterface commitFunctionalInterface) {
        super(title, content);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getSimpleWindowHashMap().put(ID,this);
    }

    public SimpleWindow(String ID, String title, String content, List<ElementButton> buttons, SimpleCommitFunctionalInterface commitFunctionalInterface) {
        super(title, content, buttons);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getSimpleWindowHashMap().put(ID,this);
    }
}
