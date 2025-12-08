package model;

/**
 * Item que pode ser encontrado em eventos aleatórios.
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public class Item {
    private final String name;
    private final Effect effect;

    public Item(String name, Effect effect) {
        this.name = name;
        this.effect = effect;
    }

    public String getName() { return name; }
    public Effect getEffect() { return effect; }

    @Override
    public String toString() {
        return name + " (" + effect + ")";
    }
}