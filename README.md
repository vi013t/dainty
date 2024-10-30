# Dainty

A Minecraft mod that adds in a bunch of small quality of life changes. Some are from other mods, some are original.

## Feature List

All features are disableable and can have their values tweaked via the mod's config. The full list of features is below:

- Removes farmland trampling
	- Removes farmland trampling entirely from both players and mobs
	- Farmland can be converted back to dirt by breaking and replacing
- Allows jumping onto fences
	- Allows players (but not mobs/animals) to jump on top of fences
	- Does not increase player's overall jump height; i.e., the player still cannot jump onto a block + slab, only fences.
- Removes phantom spawning
	- Phantoms can still be summoned but won't spawn naturally
	- Adds phantom membrane as a (configurable) 50% drop from endermen
	- Retextures phantom membrane to be fitting as an enderman drop
	- Renames phantom membrane to "Ender membrane"
- Allows picking up animals and block entities
	- Allows picking up and placing small animals (non-hostile)
	- Allows picking up and placing block entities (chests, furnaces, etc.) without disturbing their contents
- Linearizes experience
	- For example, spending 3 levels at level 1000 is the same as spending 3 levels at level 4
- Bag Item
	- Holds mass quantities of a limited amount of item types
	- Automatically picks up items that are stored
- Guaranteed Wither skulls from wither skeletons
	- Every 20 (configurable) wither skeletons killed (tracked per-player), a wither skeleton skull is guaranteed to drop
- Reinforcer item
	- Combine with any item in an anvil to make it unbreakable and unburnable
	- Late-game crafting recipe with a dragon egg, nether stars, netherite, etc.
- Warden heart drop
	- Useful late-game crafting ingredient
- Resource changes:
	- Fixes:
		- Fixes item stitching
		- Fixes Dripleaf stem clipping/stretching
		- Fixes bucket item texture inconsistencies
		- Fixes blaze mob rod mistextures
		- Fixes item-in-hand rendering
		- Fixes incorrect textures for stone/iron hoes
		- Fixes the flower color on iron golems to be accurate
		- Fixes the arrow texture to be recipe-accurate
		- Fixes the XP bottle texture to be a splash bottle
		- Fixes unupdated texture for observers
		- Fixes unupdated texture for spectator and statistic icons
		- Fixes the spyglass texture to be recipe-accurate
		- Makes pumpkin overlay translucent
	- Visual Cues:
		- Makes infested items visually distinguishable (items only; not placed blocks)
		- Makes waxed copper items visually distinguishable (items only; not placed blocks)
		- Makes sticky pistons visually distinguishable from pistons from the side
		- Shows what direction hoppers are facing from above
		- Shows what direction observers are facing from the side
		- Shows what direction dispensers and droppers are facing from the side
		- Shows what pattern is on a banner pattern on the item
		- Makes pumpkin and melon stems visually distinguishable
		- Shows visual honey stages on beehives
	- Aesthetics:
		- Gives savannas a golden grass color
		- Gives sunflowers a more blooming texture
		- Adds different textures for each dye
		- Adds colors to the tab ping indicator
		- Makes ladders 3D
		- Makes rails 3D
		- Makes bookshelves 3D
		- Makes the mace 3D
		- Makes vines 3D
    	- Makes the campfire item animated
	- Unobstruction:
		- Lowers the volume of nether portals and minecarts
		- Lowers the fire overlay for less obstruction

## Credits

- Entity/BlockEntity carrying functionality provided by [CarryOn](https://github.com/Tschipp/CarryOn/tree/1.21) under [LGPL-3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html)
- Warden heart texture provided by [Deeper and Darker](https://github.com/KyaniteMods/DeeperAndDarker/tree/neoforge-1.21) under [AGPL-3.0](https://www.gnu.org/licenses/agpl-3.0.en.html)
- Texture changes provided by [Vanilla Tweaks](https://vanillatweaks.net/) under [their custom license](https://vanillatweaks.net/terms/)
- Fence jumping functionality provided by [Jump Over Fences](https://gitlab.com/kreezxil/jump-over-fences/) under [their custom license](https://gitlab.com/kreezxil/jump-over-fences/-/blob/1.16.4/README.md?ref_type=heads&plain=1#L13)