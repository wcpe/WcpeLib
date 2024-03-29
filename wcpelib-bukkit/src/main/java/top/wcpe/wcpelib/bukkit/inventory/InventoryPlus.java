package top.wcpe.wcpelib.bukkit.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import top.wcpe.wcpelib.bukkit.WcpeLib;
import top.wcpe.wcpelib.bukkit.inventory.entity.Slot;
import top.wcpe.wcpelib.bukkit.inventory.listener.InventoryListener;
import top.wcpe.wcpelib.bukkit.inventory.listener.WcpeLibInventoryHolder;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * {@link Inventory}增强版 233
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:12:48
 */
public class InventoryPlus {
    static {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), WcpeLib.getInstance());
    }

    private final Inventory rawInventory;
    @Getter
    private final HashMap<Integer, Slot<?>> slotMap;
    @Getter
    private final String title;
    @Getter
    private final int row;
    @Getter
    private final boolean disDoubleClick;
    @Getter
    private final boolean disDrag;
    @Getter
    private final boolean disClickPlayerGui;
    @Getter
    private final boolean disClickNullSlot;
    @Getter
    @Setter
    private InventoryPlus lastInventory;
    @Getter
    @Setter
    private InventoryPlus nextInventory;
    @Getter
    @Setter
    private InventoryOpenEventFunctional onOpen;
    @Getter
    @Setter
    private InventoryDragEventFunctional onDrag;
    @Getter
    @Setter
    private InventoryClickEventFunctional onClick;
    @Getter
    @Setter
    private InventoryCloseEventFunctional onClose;

    @Getter
    @Setter
    private boolean close = true;

    @Getter
    @Setter
    private List<Integer> isLockSlot = new ArrayList<>();

    private InventoryPlus(Builder builder) {
        this.nextInventory = builder.nextInventory;
        this.lastInventory = builder.lastInventory;
        this.title = builder.title;
        this.row = builder.row;
        Inventory inv = Bukkit.createInventory(new WcpeLibInventoryHolder(this), this.row * 9, this.title);
        this.slotMap = builder.slotMap;
        this.rawInventory = inv;
        this.disDoubleClick = builder.disDoubleClick;
        this.disClickNullSlot = builder.disClickNullSlot;
        this.disDrag = builder.disDrag;
        this.disClickPlayerGui = builder.disClickPlayerGui;
        this.onOpen = builder.onOpen;
        this.onDrag = builder.onDrag;
        this.onClick = builder.onClick;
        this.onClose = builder.onClose;
    }

    public InventoryPlus setSlot(int index, Slot<?> slot) {
        slotMap.put(index, slot);
        this.refreshInventory(index);
        return this;
    }

    public void clearInventory(int[] slots) {
        for (int i : slots) {
            this.rawInventory.clear(i);
            this.slotMap.remove(i);
        }

    }

    public Stream<? extends Player> getOpenThisInventoryPlayers() {
        return Bukkit.getServer().getOnlinePlayers().stream().filter(p -> {
            InventoryView openInventory = p.getOpenInventory();
            if (openInventory != null) {
                Inventory topInventory = openInventory.getTopInventory();
                if (topInventory != null) {
                    if (topInventory.getHolder() instanceof WcpeLibInventoryHolder) {
                        InventoryPlus inventoryPlus = ((WcpeLibInventoryHolder) topInventory.getHolder()).getInventoryPlus();
                        return inventoryPlus != null;
                    }
                }
            }
            return false;
        });
    }

    public void openThisInventory(Player p) {
        p.openInventory(getRawInventory());
    }

    public void customRefreshRun(RefreshInventoryPlusFunctional run) {
        run.run(getOpenThisInventoryPlayers());
    }

    public void refreshInventoryAndOpen() {
        getOpenThisInventoryPlayers().forEach(this::openThisInventory);
    }

    public void refreshInventory() {
        for (Entry<Integer, Slot<?>> entry : this.slotMap.entrySet()) {
            if (entry.getValue() == null)
                this.rawInventory.setItem(entry.getKey(), null);
            else
                this.rawInventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }

    public void refreshInventory(int i) {
        this.rawInventory.setItem(i, getSlotMap().get(i).getItemStack());
    }

    public Inventory getRawInventory() {
        refreshInventory();
        return this.rawInventory;
    }

    public static class Builder {
        private final HashMap<Integer, Slot<?>> slotMap = new HashMap<>();
        private int row = 6;
        private String title = " ";
        private InventoryPlus lastInventory;
        private InventoryPlus nextInventory;

        private boolean disDoubleClick;
        private boolean disClickNullSlot;
        private boolean disDrag;
        private boolean disClickPlayerGui;

        private InventoryOpenEventFunctional onOpen;
        private InventoryDragEventFunctional onDrag;
        private InventoryClickEventFunctional onClick;
        private InventoryCloseEventFunctional onClose;

        public Builder() {

        }

        public Builder row(int row) {
            this.row = row;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder setSlot(int index, Slot<?> slot) {
            slotMap.put(index, slot);
            return this;
        }

        public Builder disDoubleClick(boolean disDoubleClick) {
            this.disDoubleClick = disDoubleClick;
            return this;
        }

        public Builder disClickNullSlot(boolean disClickNullSlot) {
            this.disClickNullSlot = disClickNullSlot;
            return this;
        }

        public Builder disClickPlayerGui(boolean disClickPlayerGui) {
            this.disClickPlayerGui = disClickPlayerGui;
            return this;
        }

        public Builder disDrag(boolean disDrag) {
            this.disDrag = disDrag;
            return this;
        }

        public Builder onOpen(InventoryOpenEventFunctional onOpen) {
            this.onOpen = onOpen;
            return this;
        }

        public Builder onDrag(InventoryDragEventFunctional onDrag) {
            this.onDrag = onDrag;
            return this;
        }

        public Builder onClick(InventoryClickEventFunctional onClick) {
            this.onClick = onClick;
            return this;
        }

        public Builder onClose(InventoryCloseEventFunctional onClose) {
            this.onClose = onClose;
            return this;
        }

        public InventoryPlus build() {
            return new InventoryPlus(this);
        }
    }

}