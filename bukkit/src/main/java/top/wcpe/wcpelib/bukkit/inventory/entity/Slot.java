package top.wcpe.wcpelib.bukkit.inventory.entity;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import org.bukkit.material.MaterialData;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.SlotEventFunctional;

/**
 * {@link InventoryPlus}中的格子
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:20:07
 */
public class Slot<E extends SlotExtend> {
    @Getter
    private ItemStack itemStack;
    @Getter
    private final SlotEventFunctional onClick;
    @Getter
    private final boolean unbreakable;
    private final Object slotExtend;

    private Slot(Builder<E> builder) {
        this.itemStack = builder.itemStack;
        this.onClick = builder.onClick;
        this.unbreakable = builder.unbreakable;
        this.slotExtend = builder.slotExtend;
    }


    @SuppressWarnings("unchecked")
    public E getSlotExtend() {
        return slotExtend == null ? null : (E) slotExtend;
    }

    public static class Builder<E extends SlotExtend> {
        private ItemStack itemStack = new ItemStack(Material.AIR);
        private SlotEventFunctional onClick;

        private E slotExtend;
        private boolean unbreakable = false;

        public Builder() {
        }

        public Builder(ItemStack item) {
            this.itemStack = item;
        }

        public Builder<E> name(String name) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            this.itemStack.setItemMeta(itemMeta);
            return this;
        }


        public Builder<E> lore(List<String> lores) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            itemMeta.setLore(lores);
            this.itemStack.setItemMeta(itemMeta);
            return this;
        }
        public Builder<E> lore(String... lore) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            itemMeta.setLore(Arrays.asList(lore));
            this.itemStack.setItemMeta(itemMeta);
            return this;
        }

        public Builder<E> type(Material type) {
            this.itemStack.setType(type);
            return this;
        }

        @Deprecated
        public Builder<E> type(int id, byte data) {
            this.itemStack.setTypeId(id);
            this.itemStack.setData(new MaterialData(id, data));
            return this;
        }

        public Builder<E> durability(short durability) {
            this.itemStack.setDurability(durability);
            return this;
        }

        public Builder<E> amount(int amount) {
            this.itemStack.setAmount(amount);
            return this;
        }

        public Builder<E> addEnchantments(Enchantment enchantment, int level) {
            this.itemStack.getEnchantments().put(enchantment, level);
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
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            itemMeta.setUnbreakable(unbreakable);
            this.itemStack.setItemMeta(itemMeta);
            return this;
        }

        public Slot<E> build() {
            return new Slot<E>(this);
        }
    }
}
