import java.util.Comparator;

public class SortByFreqRatio implements Comparator<Business>{
    public int compare(Business a, Business b) {
        if (a.fr > b.fr) {
            return 1;
        }
        if (a.fr < b.fr) {
            return -1;
        }
        return 0;
    }
}