package model;

/**
 * Representa um corredor que liga duas salas.
 * Pode ter um custo (peso) e um evento aleatório ao atravessar.
 * Usado como aresta no grafo NetworkList.
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public class Corridor {

    private final Room source;
    private final Room target;
    private final double weight;
    private final RandomEvent event;

    /**
     * Constrói um corredor com evento opcional.
     *
     * @param source Origem
     * @param target Destino
     * @param weight Peso/custo
     * @param event  Evento aleatório (pode ser null)
     */
    public Corridor(Room source, Room target, double weight, RandomEvent event) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.event = event;
    }

    public Room getSource() { return source; }
    public Room getTarget() { return target; }
    public double getWeight() { return weight; }
    public RandomEvent getEvent() { return event; }

    /**
     * Executa o evento se existir (chamado ao atravessar).
     */
    public void triggerEvent() {
        if (event != null) {
            event.trigger();
        }
    }

    @Override
    public String toString() {
        return source.getId() + " --(" + weight + ")--> " + target.getId() +
               (event != null ? " [EVENTO]" : "");
    }
}