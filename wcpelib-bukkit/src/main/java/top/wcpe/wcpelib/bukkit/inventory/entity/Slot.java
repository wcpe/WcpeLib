package top.wcpe.wcpelib.bukkit.inventory.entity;

import java.util.*;

import lombok.Data;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.SlotEventFunctional;
import top.wcpe.wcpelib.bukkit.utils.NameBinaryTagUtil;
import top.wcpe.wcpelib.bukkit.version.VersionInfo;
import top.wcpe.wcpelib.bukkit.version.VersionManager;
import top.wcpe.wcpelib.bukkit.version.adapter.itemstack.ItemStackManager;

/**
 * {@link InventoryPlus}中的格子
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:20:07
 */
@Data
public class Slot<E extends SlotExtend> {


    @Override
    public Slot clone() {
        return new Slot.Builder<>(getItemStack()).onClick(onClick).slotExtend(getSlotExtend()).build();
    }

    private NameBinaryTagUtil nameBinaryTagUtil;

    public ItemStack getItemStack() {
        ItemStack itemStack;

        if (VersionManager.getVersionInfo().getVersionNumber() >= VersionInfo.V1_13_2.getVersionNumber()) {
            itemStack = new ItemStack(this.type);
            itemStack.setAmount(this.amount);
            itemStack.setDurability((short) this.durability);
        } else {
            itemStack = new ItemStack(this.type, this.amount, (short) this.durability, (byte) this.data);
        }
        if (nameBinaryTagUtil != null) {
            itemStack = nameBinaryTagUtil.writeToItemStack(itemStack);
            if (itemStack == null) {
                return null;
            }
        }
        itemStack.addUnsafeEnchantments(this.enchantments);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            ItemStackManager.getItemMetaAdapter().setUnbreakable(itemMeta, this.unbreakable);
            itemMeta.setDisplayName(this.name);
            itemMeta.setLore(this.lores);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        nameBinaryTagUtil = nameBinaryTagUtil.readByItem(itemStack);
        this.type = itemStack.getType();
        this.data = itemStack.getData().getData();
        this.durability = itemStack.getDurability();
        this.amount = itemStack.getAmount();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            this.name = itemMeta.getDisplayName();
            this.lores = itemMeta.getLore();
            this.enchantments = itemMeta.getEnchants();
            this.unbreakable = ItemStackManager.getItemMetaAdapter().isUnbreakable(itemMeta);
        }
    }

    @Getter
    @Setter
    private Material type;
    @Getter
    @Setter
    private int data;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> lores;
    @Getter
    @Setter
    private int durability;
    @Getter
    @Setter
    private int amount;
    @Getter
    @Setter
    private Map<Enchantment, Integer> enchantments;
    @Getter
    @Setter
    private boolean unbreakable;
    @Getter
    @Setter
    private SlotEventFunctional onClick;

    private final Object slotExtend;

    private Slot(Builder<E> builder) {
        this.type = builder.type;
        this.data = builder.data;
        this.name = builder.name;
        this.lores = builder.lores;
        this.durability = builder.durability;
        this.amount = builder.amount;
        this.enchantments = builder.enchantments;
        this.unbreakable = builder.unbreakable;
        this.onClick = builder.onClick;
        this.slotExtend = builder.slotExtend;
        this.nameBinaryTagUtil = builder.nameBinaryTagUtil;
    }


    @SuppressWarnings("unchecked")
    public E getSlotExtend() {
        return slotExtend == null ? null : (E) slotExtend;
    }

    public static class Builder<E extends SlotExtend> {
        private Material type = Material.AIR;
        private int data = 0;
        private String name;
        private List<String> lores = new ArrayList<>();
        private int durability = 0;
        private int amount = 1;
        private Map<Enchantment, Integer> enchantments = new HashMap<>();
        private boolean unbreakable = false;
        private SlotEventFunctional onClick;
        private NameBinaryTagUtil nameBinaryTagUtil = new NameBinaryTagUtil();

        private E slotExtend;

        public Builder() {
        }

        public Builder(ItemStack item) {
            if (item == null) {
                return;
            }
            nameBinaryTagUtil = nameBinaryTagUtil.readByItem(item);
            this.type = item.getType();
            this.data = item.getData().getData();
            this.durability = item.getDurability();
            this.amount = item.getAmount();
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                this.name = itemMeta.getDisplayName();
                this.lores = itemMeta.getLore();
                this.enchantments = itemMeta.getEnchants();
                this.unbreakable = ItemStackManager.getItemMetaAdapter().isUnbreakable(itemMeta);
            }
        }

        public Builder<E> name(String name) {
            this.name = name;
            return this;
        }


        public Builder<E> lore(List<String> lores) {
            if (lores == null) {
                return this;
            }
            this.lores = lores;
            return this;
        }

        public Builder<E> lore(String... lore) {
            if (lore == null) {
                return this;
            }
            this.lores = Arrays.asList(lore);
            return this;
        }

        public Builder<E> type(Material material) {
            this.type = material;
            return this;
        }

        @Deprecated
        public Builder<E> type(int id, byte data) {
            this.type = Material.getMaterial(id);
            this.data = data;
            return this;
        }

        public Builder<E> durability(short durability) {
            this.durability = durability;
            return this;
        }

        public Builder<E> amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder<E> addEnchantments(Enchantment enchantment, int level) {
            this.enchantments.put(enchantment, level);
            return this;
        }

        public Builder<E> onClick(SlotEventFunctional onClick) {
            this.onClick = onClick;
            return this;
        }

        public Builder<E> slotExtend(E slotExtend) {
            this.slotExtend = slotExtend;
            return this;
        }

        public Builder<E> unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        public Slot<E> build() {
            return new Slot<E>(this);
        }
    }
}
