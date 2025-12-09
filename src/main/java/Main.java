import ui.TextUI;

/**
 * Ponto de entrada da aplicação Labirinto da Glória.
 * <p>
 * Esta classe é responsável apenas por instanciar a interface de utilizador (TextUI)
 * e iniciar o ciclo de vida da aplicação.
 * </p>
 *
 * @author Grupo 27
 * @version 2025/2026
 */
public class Main {

    /**
     * Método principal que arranca o programa.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        TextUI textUI = new TextUI();

        textUI.run();
    }
}