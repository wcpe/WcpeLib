package top.wcpe.wcpelib.nukkit.gui.form;

import cn.nukkit.form.window.FormWindowModal;
import lombok.Getter;
import top.wcpe.wcpelib.nukkit.gui.FromWindowListner;
import top.wcpe.wcpelib.nukkit.gui.inter.CustomCommitFunctionalInterface;
import top.wcpe.wcpelib.nukkit.gui.inter.ModelCommitFunctionalInterface;
import top.wcpe.wcpelib.nukkit.gui.inter.WcpeLibWindow;

/**
 * 用于确认的对话框
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-06 11:13
 */
public class ModelWindow extends FormWindowModal implements WcpeLibWindow {
    @Getter
    private String ID;
    @Getter
    private ModelCommitFunctionalInterface commitFunctionalInterface;

    public ModelWindow(String ID, String title, String content, String trueButtonText, String falseButtonText, ModelCommitFunctionalInterface commitFunctionalInterface) {
        super(title, content, trueButtonText, falseButtonText);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getModelWindowHashMap().put(ID,this);
    }


}
