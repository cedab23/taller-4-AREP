package arep1.taller1;

public class Horse {
    private String jockey;
    private String horse;
    private String strategy;

    public String getJockey() {
        return jockey;
    }

    public void setJockey(String jockey) {
        this.jockey = jockey;
    }

    public String getHorse() {
        return horse;
    }

    public void setHorse(String horse) {
        this.horse = horse;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Horse(String jockey, String horse, String strategy) {
        this.jockey = jockey;
        this.horse = horse;
        this.strategy = strategy;
    }

    public Horse() {
    }

}
