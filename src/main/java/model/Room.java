package model;

/**
 * Classe abstrata que representa uma sala genérica no labirinto.
 * Define as propriedades básicas como ID e descrição.
 */
public abstract class Room {
    private String id;
    private String description;

    public Room(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Método que define o comportamento quando um jogador entra na sala.
     * Cada tipo de sala (Enigma, Tesouro, Normal) terá uma lógica diferente.
     *
     * @param player O jogador que entrou na sala (podes usar Object se ainda não tiveres Player)
     */
    // public abstract void onEnter(Player player);

    public abstract void onEnter();

    @Override
    public String toString() {
        return "[" + id + ": " + description + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        // Compara apenas por ID para facilitar as pesquisas no Grafo
        if (obj instanceof Room) {
            return this.id.equals(((Room) obj).id);
        }
        return false;
    }
}