package top.wcpe.wcpelib.nukkit.gui.inter;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;

@FunctionalInterface
public interface SimpleCommitFunctionalInterface {
   void commit(Player p, FormResponseSimple responseSimple);
}
