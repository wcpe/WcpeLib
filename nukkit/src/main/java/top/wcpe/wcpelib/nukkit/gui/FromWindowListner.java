package top.wcpe.wcpelib.nukkit.gui;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import lombok.Getter;
import top.wcpe.wcpelib.nukkit.WcpeLib;
import top.wcpe.wcpelib.nukkit.gui.form.CustomWindow;
import top.wcpe.wcpelib.nukkit.gui.form.ModelWindow;
import top.wcpe.wcpelib.nukkit.gui.form.SimpleWindow;
import top.wcpe.wcpelib.nukkit.gui.form.WindowButton;
import top.wcpe.wcpelib.nukkit.gui.inter.WcpeLibWindow;

import java.util.HashMap;

/**
 * nukkit 窗口监听
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-06 10:30
 */
public class FromWindowListner implements Listener {
    @Getter
    private static HashMap<String, CustomWindow> customWindowHashMap = new HashMap<>();
    @Getter
    private static HashMap<String, ModelWindow> modelWindowHashMap = new HashMap<>();
    @Getter
    private static HashMap<String, SimpleWindow> simpleWindowHashMap = new HashMap<>();
    static {
        Server.getInstance().getPluginManager().registerEvents(new FromWindowListner(), WcpeLib.getInstance());
    }

    @EventHandler
    public void resp(PlayerFormRespondedEvent e) {
        if (e.getWindow() instanceof WcpeLibWindow) {
            WcpeLibWindow wcpeLibWindow = (WcpeLibWindow) e.getWindow();
            if (e.getResponse() instanceof FormResponseCustom) {
                CustomWindow customWindow = customWindowHashMap.get(wcpeLibWindow.getID());
                customWindow.getCommitFunctionalInterface().commit(e.getPlayer(), (FormResponseCustom) e.getResponse());
                return;
            }
            if (e.getResponse() instanceof FormResponseModal) {
                ModelWindow modelWindow = modelWindowHashMap.get(wcpeLibWindow.getID());
                modelWindow.getCommitFunctionalInterface().commit(e.getPlayer(), (FormResponseModal) e.getResponse());
                return;
            }
            if (e.getResponse() instanceof FormResponseSimple) {
                SimpleWindow simpleWindow = simpleWindowHashMap.get(wcpeLibWindow.getID());
                simpleWindow.getCommitFunctionalInterface().commit(e.getPlayer(), (FormResponseSimple) e.getResponse());
                for (ElementButton button : simpleWindow.getButtons()) {
                    if(button instanceof WindowButton){
                        ((WindowButton)button).getClickFunctionalInterface().onClick(e.getPlayer());
                        return;
                    }
                }
                return;
            }
        }
    }
}
