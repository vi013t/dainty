package violet.dainty.features.recipeviewer.core.library.load.registration;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import violet.dainty.features.recipeviewer.core.common.Constants;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IStackHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandler;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandlerHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferInfo;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IUniversalRecipeTransferHandler;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeTransferRegistration;
import violet.dainty.features.recipeviewer.core.core.collect.Table;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeTransferManager;
import violet.dainty.features.recipeviewer.core.library.recipes.UniversalRecipeTransferHandlerAdapter;
import violet.dainty.features.recipeviewer.core.library.transfer.BasicRecipeTransferHandler;
import violet.dainty.features.recipeviewer.core.library.transfer.BasicRecipeTransferInfo;

public class RecipeTransferRegistration implements IRecipeTransferRegistration {
	private final Table<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> recipeTransferHandlers = Table.hashBasedTable();
	private final IStackHelper stackHelper;
	private final IRecipeTransferHandlerHelper handlerHelper;
	private final IJeiHelpers jeiHelpers;
	private final IConnectionToServer serverConnection;

	public RecipeTransferRegistration(
		IStackHelper stackHelper,
		IRecipeTransferHandlerHelper handlerHelper,
		IJeiHelpers jeiHelpers,
		IConnectionToServer serverConnection
	) {
		this.stackHelper = stackHelper;
		this.handlerHelper = handlerHelper;
		this.jeiHelpers = jeiHelpers;
		this.serverConnection = serverConnection;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Override
	public IRecipeTransferHandlerHelper getTransferHelper() {
		return handlerHelper;
	}

	@Override
	public <C extends AbstractContainerMenu, R> void addRecipeTransferHandler(Class<? extends C> containerClass, @Nullable MenuType<C> menuType, RecipeType<R> recipeType, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
		ErrorUtil.checkNotNull(containerClass, "containerClass");
		ErrorUtil.checkNotNull(recipeType, "recipeType");

		IRecipeTransferInfo<C, R> recipeTransferInfo = new BasicRecipeTransferInfo<>(containerClass, menuType, recipeType, recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount);
		addRecipeTransferHandler(recipeTransferInfo);
	}

	@Override
	public <C extends AbstractContainerMenu, R> void addRecipeTransferHandler(IRecipeTransferInfo<C, R> recipeTransferInfo) {
		ErrorUtil.checkNotNull(recipeTransferInfo, "recipeTransferInfo");

		IRecipeTransferHandler<C, R> recipeTransferHandler = new BasicRecipeTransferHandler<>(serverConnection, stackHelper, handlerHelper, recipeTransferInfo);
		addRecipeTransferHandler(recipeTransferHandler, recipeTransferInfo.getRecipeType());
	}

	@Override
	public <C extends AbstractContainerMenu, R> void addRecipeTransferHandler(IRecipeTransferHandler<C, R> recipeTransferHandler, RecipeType<R> recipeType) {
		ErrorUtil.checkNotNull(recipeTransferHandler, "recipeTransferHandler");
		ErrorUtil.checkNotNull(recipeType, "recipeType");

		Class<? extends C> containerClass = recipeTransferHandler.getContainerClass();
		this.recipeTransferHandlers.put(containerClass, recipeType, recipeTransferHandler);
	}

	@Override
	public <C extends AbstractContainerMenu> void addUniversalRecipeTransferHandler(IUniversalRecipeTransferHandler<C> universalRecipeTransferHandler) {
		ErrorUtil.checkNotNull(universalRecipeTransferHandler, "universalRecipeTransferHandler");

		Class<? extends C> containerClass = universalRecipeTransferHandler.getContainerClass();
		UniversalRecipeTransferHandlerAdapter<C, ?> adapter = new UniversalRecipeTransferHandlerAdapter<>(universalRecipeTransferHandler);
		this.recipeTransferHandlers.put(containerClass, adapter.getRecipeType(), adapter);
	}

	@SuppressWarnings("removal")
	@Override
	public <C extends AbstractContainerMenu, R> void addUniversalRecipeTransferHandler(IRecipeTransferHandler<C, R> recipeTransferHandler) {
		ErrorUtil.checkNotNull(recipeTransferHandler, "recipeTransferHandler");

		Class<? extends C> containerClass = recipeTransferHandler.getContainerClass();
		this.recipeTransferHandlers.put(containerClass, Constants.UNIVERSAL_RECIPE_TRANSFER_TYPE, recipeTransferHandler);
	}

	public IRecipeTransferManager createRecipeTransferManager() {
		return new RecipeTransferManager(recipeTransferHandlers.toImmutable());
	}
}