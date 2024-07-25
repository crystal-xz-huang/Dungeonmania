package dungeonmania;

public interface Observer {
    public void update(Game game);

    public boolean isValid();
}
