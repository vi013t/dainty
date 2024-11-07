package violet.dainty.features.recipeviewer.addons.resources.common.registry;

import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.EnchantmentEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.RegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnchantmentRegistry {
    private Set<EnchantmentEntry> enchantments;
    private static EnchantmentRegistry instance;

    public static EnchantmentRegistry getInstance() {
        if (instance == null)
            return instance = new EnchantmentRegistry();
        return instance;
    }

    public EnchantmentRegistry() {
        enchantments = new HashSet<>();
        for (Holder<Enchantment> enchantment : getEnchants()) {
            if (enchantment != null) enchantments.add(new EnchantmentEntry(enchantment));
        }
        removeAll(Settings.excludedEnchants);
    }

    public Set<EnchantmentEntry> getEnchantments(ItemStack itemStack) {
        Set<EnchantmentEntry> set = new HashSet<>();
        for (EnchantmentEntry enchantmentEntry : enchantments) {
            if (enchantmentEntry.getEnchantment().isSupportedItem(itemStack)) {
                set.add(enchantmentEntry);
            }
        }
        return set;
    }

    private void excludeFormRegistry(Holder<Enchantment> enchantment) {
        enchantments.removeIf(enchantmentEntry -> enchantmentEntry.getEnchantment().description().getString().equals(enchantment.value().description().getString()));
    }

    private void excludeFormRegistry(String sEnchantment) {
        for (Holder<Enchantment> enchantment : getEnchants())
            if (enchantment != null && enchantment.value().description().getString().toLowerCase().contains(sEnchantment.toLowerCase()))
                excludeFormRegistry(enchantment);
    }

    public void removeAll(String[] excludedEnchants) {
        for (String enchant : excludedEnchants)
            excludeFormRegistry(enchant);
    }

    private static Set<Holder.Reference<Enchantment>> getEnchants() {
        return RegistryHelper.getRegistry(Registries.ENCHANTMENT).holders().collect(Collectors.toSet());
    }
}
