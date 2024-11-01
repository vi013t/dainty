/**
 * <h2>Right-Click Crop Harvest</h2>
 * 
 * Code for the "right-click crop harvest" feature. This feature, if enabled, allows players to right-click mature crops to automatically
 * harvest and replant them. The entry point for this feature is the {@link violet.dainty.features.cropharvest.CropHarvestEventHandler CropHarvestEventHandler},
 * which uses its {@link violet.dainty.features.cropharvest.CropHarvestEventHandler#rightClickHarvest(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock) rightClickHarvest}
 * method to listen for players right-clicking on blocks and handle crop-harvesting if appropriate. The event handler is automatically registered on the Neoforge event bus with
 * {@link net.neoforged.fml.common.EventBusSubscriber @EventBusSubscriber}, so this feature is full self-contained and doesn't need any registering or accessing from outside of the feature package.
 * 
 * <br/><br/>
 * 
 * <h2>Configuration</h2>
 * 
 * <ul>
 * 	<li>{@code enableRightClickCropHarvest: boolean = true} - Whether to enable right-click crop harvesting.</li>
 * </ul>
 */
@javax.annotation.ParametersAreNonnullByDefault
package violet.dainty.features.cropharvest;
