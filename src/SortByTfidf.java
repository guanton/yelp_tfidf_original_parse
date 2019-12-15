import java.util.Comparator;

public class SortByTfidf implements Comparator<Business>{
    public int compare(Business a, Business b) {
        if (a.tfidf > b.tfidf) {
            return 1;
        }
        if (a.tfidf < b.tfidf) {
            return -1;
        }
        return 0;
    }
}
