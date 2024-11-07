package violet.dainty.features.recipeviewer.core.library.plugins.jei.tags;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRenderHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawablesView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.VerticalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollGridWidget;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.util.ResourceLocationUtil;

public class TagInfoRecipeCategory<R extends ITagInfoRecipe, T extends RecipeType<R>> extends AbstractRecipeCategory<R> {
	private static final int WIDTH = 142;
	private static final int HEIGHT = 110;

	public TagInfoRecipeCategory(IGuiHelper guiHelper, T recipeType, ResourceLocation registryLocation) {
		super(
			recipeType,
			createTitle(registryLocation),
			guiHelper.createDrawableItemLike(Items.NAME_TAG),
			WIDTH,
			HEIGHT
		);
	}

	private static Component createTitle(ResourceLocation registryLocation) {
		String registryName = ResourceLocationUtil.sanitizePath(registryLocation.getPath());
		String registryNameTranslationKey = "gui.dainty.category.tagInformation." + registryName;

		Language language = Language.getInstance();
		if (language.has(registryNameTranslationKey)) {
			return Component.translatable(registryNameTranslationKey);
		}

		return Component.translatable("gui.dainty.category.tagInformation", StringUtils.capitalize(registryLocation.getPath()));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, R recipe, IFocusGroup focuses) {
		builder.addInputSlot()
			.addTypedIngredients(recipe.getTypedIngredients())
			.setStandardSlotBackground();

		for (ITypedIngredient<?> stack : recipe.getTypedIngredients()) {
			builder.addOutputSlot()
				.addTypedIngredient(stack);
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, R recipe, IFocusGroup focuses) {
		TagKey<?> tag = recipe.getTag();

		IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
		Component tagName = renderHelper.getName(tag);
		List<FormattedText> text = List.of(
			tagName,
			Component.literal(tag.location().toString()).withStyle(ChatFormatting.GRAY)
		);
		builder.addText(text, getWidth() - 22, 20)
			.setPosition(22, 0)
			.setColor(0xFF505050)
			.setLineSpacing(0)
			.setTextAlignment(VerticalAlignment.CENTER)
			.setTextAlignment(HorizontalAlignment.CENTER);

		IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
		List<IRecipeSlotDrawable> outputSlots = recipeSlots.getSlots(RecipeIngredientRole.OUTPUT);

		IScrollGridWidget scrollGridWidget = builder.addScrollGridWidget(outputSlots, 7, 5);
		scrollGridWidget.setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

		IRecipeSlotDrawable inputSlot = recipeSlots.getSlots(RecipeIngredientRole.INPUT)
			.getFirst();
		inputSlot.setPosition(scrollGridWidget.getScreenRectangle().position().x() + 1, 1);
	}

	@Override
	public ResourceLocation getRegistryName(R recipe) {
		return recipe.getTag().location();
	}
}
