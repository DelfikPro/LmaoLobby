package pro.delfik.lobby;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lmao.outward.Generate;

public class Items {
	public static final ItemStack SERVER_SELECTOR = Generate.itemstack(Material.COMPASS, 1, 0, "§f>> §aВыбор сервера §f<<");
	public static final ItemStack WATER_BUCKET  = Generate.itemstack(Material.WATER_BUCKET, 1, 0, "§b§lДистилированная вода",
			"§7§o - Согласно исследованию британских учёных,", "§7§o §7§o §7§o Поглощает урон гораздо лучше обычной воды");
	public static final ItemStack WEB = Generate.itemstack(Material.WEB, 1, 0, "§f§lВсемирная паутина", "§7§o - Излюбленный аксессуар бвшеров");
	public static final ItemStack LADDER = Generate.itemstack(Material.LADDER, 1, 0, "§a§lВертолётная лестница", "§7§o - Используется спецагентами.");
	public static final ItemStack SLIME_BLOCK = Generate.itemstack(Material.SLIME_BLOCK, 1, 0, "§a§lЗелёная субстанция", "§7§o - Оно шевелится!");
	public static final ItemStack SANDSTONE_BLOCKS = Generate.itemstack(Material.SANDSTONE, 64, 2, "§e§lКубики ^^");
	
	public static final ItemStack NULL = new ItemStack(Material.AIR);
}
