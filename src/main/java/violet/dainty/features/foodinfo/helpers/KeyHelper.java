package violet.dainty.features.foodinfo.helpers;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;

public class KeyHelper
{
	public static boolean isCtrlKeyDown()
	{
		long handle = Minecraft.getInstance().getWindow().getWindow();
		// prioritize CONTROL, but allow OPTION as well on Mac (note: GuiScreen's isCtrlKeyDown only checks for the OPTION key on Mac)
		boolean isCtrlKeyDown = InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL);
		if (!isCtrlKeyDown && Minecraft.ON_OSX)
			isCtrlKeyDown = InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SUPER);

		return isCtrlKeyDown;
	}

	public static boolean isShiftKeyDown()
	{
		long handle = Minecraft.getInstance().getWindow().getWindow();
		return InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
	}
}