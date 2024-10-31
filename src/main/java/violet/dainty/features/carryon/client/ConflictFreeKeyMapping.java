package violet.dainty.features.carryon.client;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.InputConstants.Type;

import net.minecraft.client.KeyMapping;

public class ConflictFreeKeyMapping extends KeyMapping {

	public ConflictFreeKeyMapping(String $$0, int $$1, String $$2) {
		super($$0, $$1, $$2);
	}

	public ConflictFreeKeyMapping(String $$0, Type $$1, int $$2, String $$3) {
		super($$0, $$1, $$2, $$3);
	}

	@Override
	public boolean same(@Nonnull KeyMapping $$0) {
		return false;
	}
}