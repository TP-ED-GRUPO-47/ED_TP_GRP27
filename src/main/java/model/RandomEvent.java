package model;

import java.util.Random;

/**
 * Evento aleatório que ocorre ao atravessar um corredor.
 * Pode conceder itens, causar dano, etc.
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public class RandomEvent {
    private final String description;
    private final Item item;
    private final Effect directEffect;
    private final Random random = new Random();

    public RandomEvent(String description, Item item, Effect directEffect) {
        this.description = description;
        this.item = item;
        this.directEffect = directEffect;
    }

    /**
     * Aplica o evento ao jogador (placeholder para integração futura).
     */
    public void trigger() {
        System.out.println("EVENTO: " + description);
        if (random.nextBoolean() && item != null) {
            System.out.println("Encontraste: " + item);
        }
        if (directEffect != null) {
            System.out.println("Efeito: " + directEffect);
        }
    }
}