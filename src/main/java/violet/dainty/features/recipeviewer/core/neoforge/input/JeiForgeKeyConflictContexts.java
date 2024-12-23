package violet.dainty.features.recipeviewer.core.neoforge.input;

import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public enum JeiForgeKeyConflictContexts implements IKeyConflictContext {
	JEI_GUI_HOVER {
		@Override
		public boolean isActive() {
			return KeyConflictContext.GUI.isActive();
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	},

	JEI_GUI_HOVER_CHEAT_MODE {
		@Override
		public boolean isActive() {
			return KeyConflictContext.GUI.isActive();
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	},

	JEI_GUI_HOVER_CONFIG_BUTTON {
		@Override
		public boolean isActive() {
			return KeyConflictContext.GUI.isActive();
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	},

	JEI_GUI_HOVER_SEARCH {
		@Override
		public boolean isActive() {
			return KeyConflictContext.GUI.isActive();
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	}
}
