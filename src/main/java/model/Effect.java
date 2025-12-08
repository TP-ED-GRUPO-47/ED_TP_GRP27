package model;

/**
 * Representa um efeito que pode ser aplicado a um jogador.
 * Usado por itens e eventos aleatórios.
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public enum Effect {
    HEAL(20),
    DAMAGE(25),
    BONUS_POWER(15),
    TRAP(-30);

    private final int value;

    Effect(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}