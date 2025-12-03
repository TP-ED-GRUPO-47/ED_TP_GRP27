package model;

public class RoomStandard extends Room {

    public RoomStandard(String id, String description) {
        super(id, description);
    }

    @Override
    public void onEnter() {
        System.out.println("Entraste numa sala normal: " + getDescription());
    }
}