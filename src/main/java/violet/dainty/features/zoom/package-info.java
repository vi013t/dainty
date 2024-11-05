/**
 * <h1>Zoom</h1>
 * 
 * The zoom feature. This package contains the core code for implementing the zoom feature. The zoom
 * feature allows players to "zoom in" as if using a spyglass, simply by holding a keybinding.
 * 
 * <br/><br/>
 * 
 * The logic of this feature is handled in {@link violet.dainty.features.zoom.ZoomEventHandler the
 * zoom event handler}. This event handler attaches and reads {@link violet.dainty.features.zoom.ZoomData
 * ZoomData} when certain events happen, such as when the player presses the keybinding.
 */
@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
package violet.dainty.features.zoom;
