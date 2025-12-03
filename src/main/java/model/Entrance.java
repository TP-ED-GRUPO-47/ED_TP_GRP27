package model;

public class Entrance extends Room {

    public Entrance(String id, String description) {
        super(id, description);
    }

    @Override
    public void onEnter() {
        System.out.println("Estás na entrada: " + getDescription());
        System.out.println("A aventura começa aqui!");
    }
}