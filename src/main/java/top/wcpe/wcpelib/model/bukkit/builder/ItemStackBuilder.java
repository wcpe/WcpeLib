package top.wcpe.wcpelib.model.bukkit.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

public class ItemStackBuilder {

    private String type;
    private String name;
    private List<String> lore;
    private Byte data;
    private int amount = 1;


    public ItemStack build() {
        ItemStack item = new ItemStack(Material.getMaterial(type));
//        if (data!=null)
//            Material material = Material.ACACIA_DOOR;

//            item.setData(new MaterialData().setData(1););


        return item;
    }

}