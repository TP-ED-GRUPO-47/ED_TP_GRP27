import model.GameEngine;

public class Main {
    public static void main(String[] args) {
        // Cria o motor de jogo
        GameEngine game = new GameEngine();

        // Inicializa (carrega mapa e jogador)
        game.initGame("mapa_v1.json");

        // Arranca o loop principal
        game.start();
    }
}