package violet.dainty.features.blocktooltips.util;

import java.util.Map;
import java.util.Optional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import violet.dainty.features.blocktooltips.api.TraceableException;
import violet.dainty.features.blocktooltips.api.callback.JadeItemModNameCallback;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;

public class ModIdentification implements ResourceManagerReloadListener {

	public static final ModIdentification INSTANCE = new ModIdentification();
	private static final Map<String, Optional<String>> NAMES = Maps.newConcurrentMap();

	public static void invalidateCache() {
		NAMES.clear();
	}

	public static Optional<String> getModName(String namespace) {
		return NAMES.computeIfAbsent(namespace, $ -> {
			Optional<String> optional = ClientProxy.getModName($);
			if (optional.isPresent()) {
				return optional;
			}
			String key = "dainty.modName." + $;
			if (I18n.exists(key)) {
				return Optional.of(I18n.get(key));
			} else {
				return Optional.empty();
			}
		});
	}

	public static String getModName(ResourceLocation id) {
		return getModName(id.getNamespace()).orElse(id.getNamespace());
	}

	public static String getModName(Block block) {
		ResourceLocation id;
		try {
			id = CommonProxy.getId(block);
		} catch (Throwable e) {
			throw TraceableException.create(e, BuiltInRegistries.BLOCK.getKey(block).getNamespace());
		}
		return getModName(id);
	}

	public static String getModName(ItemStack stack) {
		String id;
		try {
			for (JadeItemModNameCallback callback : WailaClientRegistration.instance().itemModNameCallback.callbacks()) {
				String s = callback.gatherItemModName(stack);
				if (!Strings.isNullOrEmpty(s)) {
					return s;
				}
			}
			id = CommonProxy.getModIdFromItem(stack);
		} catch (Throwable e) {
			throw TraceableException.create(e, BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace());
		}
		return getModName(id).orElse(id);
	}

	public static String getModName(Entity entity) {
		if (entity instanceof Painting painting) {
			return getModName(painting.getVariant().unwrapKey().orElseThrow().location());
		}
		if (entity instanceof ItemEntity itemEntity) {
			return getModName(itemEntity.getItem());
		}
		if (entity instanceof FallingBlockEntity fallingBlock) {
			return getModName(fallingBlock.getBlockState().getBlock());
		}
		if (entity instanceof Villager villager) {
			return getModName(BuiltInRegistries.VILLAGER_PROFESSION.getKey(villager.getVillagerData().getProfession()));
		}
		ResourceLocation id;
		try {
			id = CommonProxy.getId(entity.getType());
		} catch (Throwable e) {
			throw TraceableException.create(e, BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace());
		}
		return getModName(id);
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		invalidateCache();
	}

}
