/**
 * <h1>Mixins</h1>
 * 
 * <a href="https://github.com/SpongePowered/Mixin">Mixins</a>, provided by <a href="https://github.com/SpongePowered">SpongePowered</a>, 
 * are an extension to Java as a language using assembly and reflection to inject listeners and transformers onto classes at runtime. Less 
 * formally, it allows hooking into methods, for example, that Neoforge doesn't provide an event for. 
 * 
 * <br/><br/>
 * 
 * All mixins must be stored in this package; Mixins that aren't will (informally) cause undefined behavior. Thus, this package contains code
 * for features that aren't grouped with the code in the {@code violet.dainty.features} package. Features should specify in their {@code package-info.java} file
 * if they use mixins so that it's clear where all of the feature-related code is.
 * 
 * <h2>Implementing a Mixin</h2>
 * 
 * For example, the leaf decay feature needs a way to constantly check leaf blocks to see if they should be decaying. There is no event fired 
 * from Neoforge every block-tick or anything like that. However, when we look at Minecraft's {@link net.minecraft.world.level.block.LeavesBlock LeavesBlock}
 * class, we can see the {@link net.minecraft.world.level.block.state.BlockBehaviour#tick tick()} method, which runs every tick for every individual leaf block
 * state and position. So, we need a way to "hook" into this method&mdash;in other words, to just run some of <i>our own</i> code when the method is called.
 * 
 * <br/><br/>
 * 
 * This is when we want to use a mixin! We create our mixin {@link violet.dainty.mixins.leafdecay.LeafMixin}, which uses 
 * {@link org.spongepowered.asm.mixin.Mixin @Mixin(LeavesBlock.class)} to indicate that we want this class to hook into methods in the {@code LeavesBlock} class.
 * 
 * <br/><br/>
 * 
 * Then, we create a method that has the code we want to run. This method should take the same parameters as the method we're hooking into&mdash;In this case,
 * that's a {@link net.minecraft.world.level.block.state.BlockState BlockState}, a {@link net.minecraft.server.level.ServerLevel ServerLevel}, a
 * {@link net.minecraft.core.BlockPos BlockPos}, and a {@link net.minecraft.util.RandomSource RandomSource}. Finally, our last parameter should
 * always be a {@link org.spongepowered.asm.mixin.injection.callback.CallbackInfo CallbackInfo} object. This object provides information about the original method
 * call. Our method might look like this:
 * 
 * <br/><br/>
 * 
 * {@code private void onLeafBlockTick(BlockState state, ServerLevel level, BlockPos position, RandomSource random, CallbackInfo callbackInfo)}
 * 
 * <br/><br/>
 * 
 * Now, we need to tell the Mixin library that we want to inject this code onto the {@code tick} method. We do this with the 
 * {@link org.spongepowered.asm.mixin.injection.Inject @Inject} annotation on the method. This annotation takes a couple of arguments; Firstly, we need to pass
 * the {@code method} argument with a string that represents the method we want to hook into. That might originally look something like this:
 * 
 * <br/><br/>
 * 
 * {@code tick()}
 * 
 * <br/><br/>
 * 
 * However, in Java, functions can be overloaded, meaning we can have multiple methods with the same name that have different parameters. So, we need to specify
 * the parameter types of our function as well, separated by semicolons. What we have now might look a little like this:
 * 
 * <br/><br/>
 * 
 * {@code tick(BlockState;ServerLevel;BlockPos;RandomSource;)}
 * 
 * <br/><br/>
 * 
 * We also need to specify the return type. This will come directly after the parameters. For void, we simply use a {@code V}:
 * 
 * <br/><br/>
 * 
 * {@code tick(BlockState;ServerLevel;BlockPos;RandomSource;)V}
 * 
 * <br/><br/>
 * 
 * However, what if there are multiple classes called {@code BlockState}? Or {@code ServerLevel}? Once again we need to be extremely specific&mdash;So we have to
 * qualify each parameter type with it's full path:
 * 
 * <br/><br/>
 * 
 * {@code tick(net/minecraft/world/level/block/state/BlockState;net/minecraft/server/level/ServerLevel;net/minecraft/core/BlockPos;net/minecraft/util/RandomSource;)V}
 * 
 * <br/><br/>
 * 
 * Note the use of forward slashes as opposed to periods or anything else; That's just how Java internally formats methods. This is <i>almost</i> right, but due to some
 * internal java quirks, we need to add a capital {@code L} to the beginning of each parameter type. Our final method name looks like this:
 * 
 * <br/><br/>
 * 
 * {@code tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V}
 * 
 * <br/><br/>
 * 
 * {@code @Inject} also needs an {@code at} parameter; If you don't know what this is, just use {@code @At("HEAD")}.
 * 
 * <br/><br/>
 * 
 * We can also specify that we want our original method to be cancellable with {@code cancellable = true}. This will make it so that we can "cancel" the original method
 * call so that the code in the <i>original</i> method is never run; Only <i>our</i> code. Be careful with this, it can easily break vanilla functionality. We do
 * this with our {@code CallbackInfo} object, by calling {@code callbackInfo.cancel()}.
 * 
 * <br/><br/>
 * 
 * Our final class might look like {@link violet.dainty.mixins.leafdecay.LeafMixin}. For more examples of mixins, see {@code violet.dainty.mixins.carryon}.
 * 
 * <br/><br/>
 * 
 * <strong>Mixins must also be listed in {@code /src/main/resources/dainty.mixins.json}<strong>. This file tells the mixins library where to find mixins to register.
 */
@javax.annotation.ParametersAreNonnullByDefault
package violet.dainty.mixins;