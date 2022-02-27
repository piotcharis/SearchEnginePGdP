package pgdp.searchengine.util;

public class WordCount {
    private String word;
    private int count;
    private double weight;
    private double normalizedWeight;

    public WordCount(String word) {
        this.word = word;
        this.count = 0;
        this.weight = 0;
        this.normalizedWeight = 0;
    }

    public WordCount(String word, int count) {
        this.word = word;
        this.count = count > 0 ? count : 0;
        this.weight = count > 0 ? 1 : 0;
        this.normalizedWeight = count > 0 ? 1 : 0;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getNormalizedWeight() {
        return normalizedWeight;
    }

    public void setNormalizedWeight(double normalizedWeight) {
        this.normalizedWeight = normalizedWeight;
    }

    @Override
    public String toString() {
        return "WordCount [word=" + word + ", count=" + count + ", weight=" + weight + ", normalizedWeight="
                + normalizedWeight + "]";
    }

}
