package top.wcpe.wcpelib.nukkit.gui.inter;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;

@FunctionalInterface
public interface ModelCommitFunctionalInterface {
   void commit(Player p, FormResponseModal responseModal);
}
