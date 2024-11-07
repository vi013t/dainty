package violet.dainty.features.recipeviewer.addons.resources.api.conditionals;

public class ExtendedConditional extends Conditional {
    Conditional conditional;
    String value;

    public ExtendedConditional(Conditional conditional, String value) {
        this.conditional = conditional;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(conditional.toString(), value);
    }
}
