package violet.dainty.features.recipeviewer.core.common.input.keys;

public interface IJeiKeyMappingBuilder {
	IJeiKeyMappingBuilder setContext(JeiKeyConflictContext context);
	IJeiKeyMappingBuilder setModifier(JeiKeyModifier modifier);

	IJeiKeyMappingInternal buildMouseLeft();
	IJeiKeyMappingInternal buildMouseRight();
	IJeiKeyMappingInternal buildMouseMiddle();
	IJeiKeyMappingInternal buildKeyboardKey(int key);
	IJeiKeyMappingInternal buildUnbound();
}
