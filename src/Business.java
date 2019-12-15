import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business {
    //each business in the dataset has an ID, a name, an address, a list of reviews (String), a number of reviews,
    //and finally, an tf-idf score associated to each word that appears in its reviews
    //tf-idf is the frequency of a word in a business's reviews divided by the number of other businesses whose reviews
    //contain at least one mention of the word
    //we will use these tf-idf scores to generate accurate tags that reflect each business
    String businessID;
    String businessName;
    String businessAddress;
    String reviews;
    double fr;
    int numwords;
    double tfidf;
    Map<String, Integer> tfMap = new HashMap<>();
    int reviewCharCount;
    Map<String, Double> freqratio;
    Map<String, Double> tfidfmap;

    public Business(String businessID, String businessName, String businessAddress, String reviews, int reviewCharCount,
                    Map<String, Double> idfmap) {
        this.businessID = businessID;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.reviews = reviews;
        this.reviewCharCount = reviewCharCount;
        this.tfidfmap = idfmap;
    }

    public void setFreqratio(Map<String, Double> d) {
        freqratio = d;
    }
    public void setNumWords(int n) {numwords = n;}
    public void setTfMap(Map<String, Integer> tfMap) {
        this.tfMap = tfMap;
    }
    public Map<String, Integer> getTfMap() {
        return tfMap;
    }

    //converts the map of words with their tfidf scores to a string
    public String maptostring(Map<String, Double> tfidfmap) {
        String s = "";
        int x = 1;
        for (Map.Entry<String,Double> entry : tfidfmap.entrySet()) {
            s = s + "(" + entry.getKey() + ", " + entry.getValue() + "), ";
            if (x % 6 == 0) {
                s=s+"\n";
            }
            x++;
        }
        return s;
    }

    public void settfidfmap(Map<String, Double> tfidfmap) {
        this.tfidfmap = tfidfmap;
    }

    public void assignFr(String query) {
        List<String> keyWords = Arrays.asList(query.split(" "));
        for (String s: freqratio.keySet()) {
            for (String keyWord: keyWords) {
                if (s.equals(keyWord)) {
                    fr = fr + freqratio.get(keyWord);
                }
            }
        }
    }

    public void assignTfidf() {
        double sumTfidf = 0;
        for (String s: tfidfmap.keySet()) {
            sumTfidf = sumTfidf + tfidfmap.get(s);
        }
        tfidf = sumTfidf;
    }


    public String toString() {
        return "-------------------------------------------------------------------------------\n"
                + "Business ID: " + businessID + "\n"
                + "Business Name: " + businessName + "\n"
                + "Business Address: " + businessAddress + "\n"
                + "Character Count: " + reviewCharCount + "\n"
                + "Key Words: " + maptostring(tfidfmap);
    }



}
