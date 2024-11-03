# Dainty

A Minecraft mod that adds in a bunch of small quality of life changes. Some are from other mods, some are original.

## Features

For a full feature list, see [the feature spreadsheet](https://airtable.com/invite/l?inviteId=invQFnQWsgmLwT0gx&inviteToken=251c5169dbc215d3906cff9ebcb2aee7686c607bd901e0f47a6741e8279dd355&utm_medium=email&utm_source=product_team&utm_content=transactional-alerts).

## Credits

- Entity/BlockEntity carrying functionality provided by [CarryOn](https://github.com/Tschipp/CarryOn/tree/1.21) under [LGPL-3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html)
- Warden heart texture provided by [Deeper and Darker](https://github.com/KyaniteMods/DeeperAndDarker/tree/neoforge-1.21) under [AGPL-3.0](https://www.gnu.org/licenses/agpl-3.0.en.html)
- Texture changes provided by [Vanilla Tweaks](https://vanillatweaks.net/) under [their custom license](https://vanillatweaks.net/terms/)
- Fence jumping functionality provided by [Jump Over Fences](https://gitlab.com/kreezxil/jump-over-fences/) under [their custom license](https://gitlab.com/kreezxil/jump-over-fences/-/blob/1.16.4/README.md?ref_type=heads&plain=1#L13)
- Right-click crop harvest functionality provided by [Right Click Harvest](https://github.com/JamCoreModding/right-click-harvest/tree/main?tab=MIT-1-ov-file) under [MIT](https://opensource.org/license/mit)
- Biome compass functionality provided by [Nature's Compass](https://github.com/MattCzyr/NaturesCompass/tree/neoforge-1.21.1) under [Attribution-NonCommercial-ShareAlike 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en)
- Structure functionality provided by [Explorer's Compass](https://github.com/MattCzyr/ExplorersCompass/tree/neoforge-1.21.1) under [Attribution-NonCommercial-ShareAlike 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en)
- Block tooltip functionality provided by [Jade](https://github.com/Snownee/Jade/tree/1.21-neoforge) under [Attribution-NonCommercial-ShareAlike 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en)

## Contributing

Dainty is accepting contributions. The mod is well-documented; Start by checking out `package-info.java` files if you're unsure where to begin. Please read on for project conventions.

### Conventions

Below are some conventions to follow when contributing:

- Document like crazy. Seriously, you can't over-document. Every field, every method, every class, every package, if you can. Don't just say what it is, but say how it does what it does, where it's used, why it does something a certain way, etc. Remember that code is harder to read than to write, and you're writing for people who've never seen your code before. Particularly document the less-obvious things; It's more important to document some obscure reflection-based obfuscated method call than a field called `isAlive` on an entity. Things like hacky workarounds or strange behavior exploits should be noted, because they're often confusing to understand or read in code.
- Follow the existing file organizational structure. It should be pretty obvious and intuitive, but if not, read the `package-info.java` files for more information on the project's structure.