import java.util.Comparator;

public class SortByReviewCharCount implements Comparator<Business>{
    public int compare(Business a, Business b) {
        return a.reviewCharCount - b.reviewCharCount;
    }
}
