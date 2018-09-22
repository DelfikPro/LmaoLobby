package pro.delfik.lobby;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lmao.outward.Generate;
import pro.delfik.lmao.outward.item.ItemBuilder;

public class Items {
	public static final ItemStack SERVER_SELECTOR = new ItemBuilder(Material.COMPASS)
			.withDisplayName("§f>> §aВыбор сервера §f<<").build();

	public static final ItemStack WATER_BUCKET  = new ItemBuilder(Material.WATER_BUCKET)
			.withDisplayName("§b§lДистилированная вода")
			.addLore("§7§o - Согласно исследованию британских учёных,",
					"§7§o §7§o §7§o Поглощает урон гораздо лучше обычной воды").build();

	public static final ItemStack WEB = new ItemBuilder(Material.WEB)
			.withDisplayName("§f§lВсемирная паутина")
			.addLore("§7§o - Излюбленный аксессуар бвшеров").build();

	public static final ItemStack LADDER = new ItemBuilder(Material.LADDER)
			.withDisplayName("§a§lВертолётная лестница")
			.addLore("§7§o - Используется спецагентами.").build();

	public static final ItemStack SLIME_BLOCK = new ItemBuilder(Material.SLIME_BLOCK)
			.withDisplayName("§a§lЗелёная субстанция")
			.withLore("§7§o - Оно шевелится!").build();

	public static final ItemStack SANDSTONE_BLOCKS = new ItemBuilder(Material.SANDSTONE)
			.withDisplayName("§e§lКубики ^^")
			.withAmount(64).withData(2).build();
	
	public static final ItemStack NULL = new ItemStack(Material.AIR);
}
