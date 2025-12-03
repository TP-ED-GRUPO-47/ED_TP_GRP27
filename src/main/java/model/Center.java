package model;

public class Center extends Room {

    public Center(String id, String description) {
        super(id, description);
    }

    @Override
    public void onEnter() {
        System.out.println("ENCONTRASTE O TESOURO! " + getDescription());
    }
}