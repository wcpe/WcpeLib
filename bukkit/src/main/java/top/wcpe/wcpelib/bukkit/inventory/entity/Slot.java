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
import top.wcpe.wcpelib.bukkit.utils.NetMinecraftServerUtil;

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

    private final Object slotExtend;

    private Slot(Builder<E> builder) {
        if (NetMinecraftServerUtil.getServerVersionNum() >= 1130) {
            this.itemStack = new ItemStack(builder.type);
            this.itemStack.setAmount(builder.amount);
            this.itemStack.setDurability((short) builder.durability);
        } else {
            this.itemStack = new ItemStack(builder.type.getId(), builder.amount, (short) builder.durability, (byte) builder.data);
        }
        this.itemStack.addUnsafeEnchantments(builder.enchantments);
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setUnbreakable(builder.unbreakable);
        itemMeta.setDisplayName(builder.name);
        itemMeta.setLore(builder.lores);
        this.itemStack.setItemMeta(itemMeta);
        this.onClick = builder.onClick;
        this.slotExtend = builder.slotExtend;
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

        private E slotExtend;

        public Builder() {
        }

        public Builder(ItemStack item) {
            this.type = item.getType();
            this.data = item.getData().getData();
            this.durability = item.getDurability();
            this.amount = item.getAmount();
            ItemMeta itemMeta = item.getItemMeta();
            this.name = itemMeta.getDisplayName();
            this.lores = itemMeta.getLore();
            this.enchantments = itemMeta.getEnchants();
            this.unbreakable = itemMeta.isUnbreakable();
        }

        public Builder<E> name(String name) {
            this.name = name;
            return this;
        }


        public Builder<E> lore(List<String> lores) {
            if (lores == null) return this;
            this.lores = lores;
            return this;
        }

        public Builder<E> lore(String... lore) {
            if (lore == null) return this;
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
