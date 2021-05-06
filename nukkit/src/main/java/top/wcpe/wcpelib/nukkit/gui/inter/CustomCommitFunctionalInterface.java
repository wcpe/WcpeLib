package top.wcpe.wcpelib.nukkit.gui.inter;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;

@FunctionalInterface
public interface CustomCommitFunctionalInterface {
   void commit(Player p, FormResponseCustom responseCustom);
}
