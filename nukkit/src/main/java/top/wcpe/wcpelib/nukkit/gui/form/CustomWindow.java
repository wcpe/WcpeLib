package top.wcpe.wcpelib.nukkit.gui.form;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowCustom;
import lombok.Getter;
import top.wcpe.wcpelib.nukkit.gui.FromWindowListner;
import top.wcpe.wcpelib.nukkit.gui.inter.CustomCommitFunctionalInterface;
import top.wcpe.wcpelib.nukkit.gui.inter.WcpeLibWindow;

import java.util.List;

/**
 * 一般用来输入配置
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-06 10:42
 */
public class CustomWindow extends FormWindowCustom implements WcpeLibWindow {
    @Getter
    private String ID;
    @Getter
    private CustomCommitFunctionalInterface commitFunctionalInterface;

    public CustomWindow(String ID, String title, CustomCommitFunctionalInterface commitFunctionalInterface) {
        super(title);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getCustomWindowHashMap().put(ID,this);
    }

    public CustomWindow(String ID, String title, List<Element> contents, CustomCommitFunctionalInterface commitFunctionalInterface) {
        super(title, contents);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getCustomWindowHashMap().put(ID,this);
    }

    public CustomWindow(String ID, String title, List<Element> contents, String icon, CustomCommitFunctionalInterface commitFunctionalInterface) {
        super(title, contents, icon);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getCustomWindowHashMap().put(ID,this);
    }

    public CustomWindow(String ID, String title, List<Element> contents, ElementButtonImageData icon, CustomCommitFunctionalInterface commitFunctionalInterface) {
        super(title, contents, icon);
        this.ID = ID;
        this.commitFunctionalInterface = commitFunctionalInterface;
        FromWindowListner.getCustomWindowHashMap().put(ID,this);
    }

    
}
