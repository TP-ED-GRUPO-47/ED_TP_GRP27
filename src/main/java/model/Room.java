package model;

import java.util.Objects;

/**
 * Classe abstrata que representa uma sala genérica no labirinto.
 * Todas as salas possuem um ID único e uma descrição.
 * <p>
 * O método {@code onEnter()} é chamado automaticamente quando um jogador entra na sala.
 * O {@code equals()} e {@code hashCode()} são baseados no ID para permitir uso correto
 * em estruturas como {@code MyHashMap} e grafos.
 * </p>
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public abstract class Room {

    /** Identificador único da sala */
    private final String id;

    /** Descrição textual da sala */
    private final String description;

    /**
     * Construtor da sala.
     *
     * @param id Identificador único da sala (ex: "E1", "R1")
     * @param description Descrição visível ao jogador
     */
    public Room(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Método chamado automaticamente quando um jogador entra na sala.
     * Cada tipo de sala (Entrada, Enigma, Tesouro, etc.) define o seu comportamento.
     */
    public abstract void onEnter();

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" + id + ": " + description + "]";
    }
}