package top.wcpe.wcpelib.bukkit.inventory.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
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
	private final String name;
	@Getter
	private final List<String> lore;
	@Getter
	private final int id;
	@Getter
	private final byte data;
	@Getter
	private final short durability;
	@Getter
	private final int amount;
	@Getter
	private final Map<Enchantment, Integer> enchantments;
	@Getter
	private final SlotEventFunctional onClick;
	@Getter
	private final boolean unbreakable;
	private final Object slotExtend;

	private Slot(Builder<E> builder) {
		this.name = builder.name;
		this.lore = builder.lore;
		this.id = builder.id;
		this.data = builder.data;
		this.durability = builder.durability;
		this.amount = builder.amount;
		this.enchantments = builder.enchantments;
		this.onClick = builder.onClick;
		this.unbreakable = builder.unbreakable;
		this.slotExtend = builder.slotExtend;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(id, amount, durability, data);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		itemMeta.setUnbreakable(unbreakable);
		enchantments.entrySet().forEach(entry -> itemMeta.getEnchants().put(entry.getKey(), entry.getValue()));
		item.setItemMeta(itemMeta);
		return item;
	}

	@SuppressWarnings("unchecked")
	public E getSlotExtend() {
		return slotExtend == null ? null : (E) slotExtend;
	}

	public static class Builder<E extends SlotExtend> {
		private String name = " ";
		private List<String> lore = new ArrayList<String>();
		private int id = 0;
		private byte data = 0;
		private short durability = 0;
		private int amount = 1;
		private Map<Enchantment, Integer> enchantments = new HashMap<>();
		private SlotEventFunctional onClick;

		private E slotExtend;
		private boolean unbreakable = false;

		public Builder() {
		}

		@SuppressWarnings("deprecation")
		public Builder(ItemStack item) {
			if (item != null) {
				this.id = item.getTypeId();
				this.data = item.getData().getData();
				ItemMeta itemMeta = item.getItemMeta();
				if (itemMeta != null) {
					if (itemMeta.hasDisplayName())
						this.name = itemMeta.getDisplayName();
					if (itemMeta.hasLore())
						this.lore = itemMeta.getLore();
					this.unbreakable = itemMeta.isUnbreakable();
				}
				this.amount = item.getAmount();
				this.durability = item.getDurability();
				this.enchantments = item.getEnchantments();
			}
		}

		public Builder<E> name(String name) {
			this.name = name;
			return this;
		}

		public Builder<E> lore(String... lores) {
			for (String s : lores) {
				this.lore.add(s);
			}
			return this;
		}

		public Builder<E> lore(List<String> lores) {
			for (String s : lores) {
				this.lore.add(s);
			}
			return this;
		}

		public Builder<E> id(int id) {
			this.id = id;
			return this;
		}

		public Builder<E> data(byte data) {
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
