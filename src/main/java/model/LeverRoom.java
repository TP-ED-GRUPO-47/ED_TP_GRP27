package model;

import java.util.Random;

/**
 * Sala com uma alavanca que provoca efeitos imprevisíveis no jogo.
 * <p>
 * Ao interagir, pode abrir caminhos secretos, dar itens, ou ativar armadilhas.
 * Cumpre o requisito do enunciado: "alavancas" e "efeitos inesperados".
 * </p>
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public class LeverRoom extends Room {

    private final Random random = new Random();
    private boolean activated = false;

    public LeverRoom(String id, String description) {
        super(id, description);
    }

    @Override
    public void onEnter() {
        System.out.println(">> [ALAVANCA] " + getDescription());

        if (activated) {
            System.out.println("A alavanca já foi usada. Nada acontece.");
            return;
        }

        System.out.println("Vês uma alavanca antiga. Queres puxar? (sim/não)");
        activateLever();
    }

    /**
     * Ativa a alavanca e gera um efeito aleatório.
     */
    private void activateLever() {
        activated = true;
        int chance = random.nextInt(100);

        if (chance < 40) {
            System.out.println("A alavanca abre uma passagem secreta! (futuro: adiciona corredor)");
        } else if (chance < 70) {
            System.out.println("Uma armadilha! Perdes 20 de poder!");
            // player.reducePower(20);
        } else {
            System.out.println("Nada acontece... apenas um clique.");
        }
    }
}