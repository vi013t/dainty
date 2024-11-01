/**
 * <h1>Features</h1>
 * 
 * Code for individual features for the Dainty mod. Dainty is organized by feature, giving each feature an individual folder/package in the code structure.
 * Each of these feature packages are contained within this parent {@code features} package.
 * 
 * <br/><br/>
 * 
 * <h2>Mixins</h2>
 * 
 * Mixins must be in their own package that must not contain non-mixin classes. For this reason, mixins are located outside the {@code features} package in the
 * {@code violet.dainty.mixins} package. This can be confusing because it separates a single feature into multiple places; For this reason, individual features
 * will specify in their {@code package-info.java} if they have mixins or other code located outside of the main feature package.
 */
package violet.dainty.features;
