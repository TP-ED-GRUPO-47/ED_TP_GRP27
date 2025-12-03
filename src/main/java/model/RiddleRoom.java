package model;

public class RiddleRoom extends Room {
    private Riddle riddle;
    private boolean isSolved;

    public RiddleRoom(String id, String description, Riddle riddle) {
        super(id, description);
        this.riddle = riddle;
        this.isSolved = false;
    }

    public Riddle getRiddle() { return riddle; }
    public boolean isSolved() { return isSolved; }
    public void setSolved(boolean solved) { this.isSolved = solved; }

    @Override
    public void onEnter() {
        System.out.println(">> [ENIGMA] " + getDescription());
        if (isSolved) {
            System.out.println("O guardião deixa-te passar.");
        } else {
            System.out.println("Tens de responder para passar!");
        }
    }
}