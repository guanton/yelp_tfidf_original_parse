import com.google.common.collect.MinMaxPriorityQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;



public class YelpAnalysis {


    //a dictionary containing every word that makes an appearance in the reviews contained in the dataset along with
    //the number of restaurants in the dataset whose reviews contain the word
    private Map<String, Integer> dictionary = new HashMap<>();
    //different comparators to sort businesses by
    SortByReviewCharCount srcc = new SortByReviewCharCount();
    SortByTfidf stfidf = new SortByTfidf();
    SortByFreqRatio sbfr = new SortByFreqRatio();
    //a priority Queue representing every business in the .txt file
    private MinMaxPriorityQueue<Business> businesses;
    private Queue<Business> checkBusinesses = new PriorityQueue<>(srcc);
    private boolean freqmode;
    private Set<Business> businessSet = new HashSet<>();


    //search
    public static void main(String[] args) {
        YelpAnalysis yp = new YelpAnalysis();
        yp.init(false);
        String query = "pizza hut";
        yp.txtToString(query);
        yp.secondPass(query);
        for (Business b: yp.businesses) {
            System.out.println(b);
        }
    }

    public void init(boolean mode){
        freqmode = mode;
        if (freqmode) {
            businesses = MinMaxPriorityQueue.orderedBy(sbfr.reversed()).maximumSize(10).create();
        } else {
            businesses = MinMaxPriorityQueue.orderedBy(stfidf.reversed()).maximumSize(10).create();
        }
    }

    public void secondPass(String query) {
        for (Business b: businessSet) {
            b.settfidfmap(tfidfcalculator(b, query));
            b.assignTfidf();
            businesses.offer(b);
        }
    }

    public MinMaxPriorityQueue<Business> getBusinesses() {
        return businesses;
    }

    public Set<Business> getBusinessSet() {
        return businessSet;
    }

    //this method constructs one String for each Business in the .txt file (dataset), and then
    //sends each String as a parameter to the method strToBusiness to construct a list of Businesses
    public void txtToString(String query) {
        InputStream in = null;
        StringBuilder sb = new StringBuilder();
        try {
            //set the input stream to the file containing the dataset
            //C:\Users\Pengfei\Desktop\yelpDatasetParsed_medium.txt
            in = new FileInputStream("yelpDatasetParsed_full.txt");
            in = new BufferedInputStream(in);
            while (true) {
                int res = in.read();
                if (res == -1) {
                    break;
                }
                char result = (char) res;  //reads the next character in the file

                // if there are no more characters in the file, then result will be -1, and we exit the loop
                //since the format of the text in the file is {, Business info, }, {, Business info, }, ...
                // we can build a string representing each business by looking inside the brackets

                if (result == '{') {
                    continue;
                }
                if (result == '}') {
                    //construct a business object
                    Business b = strToBusiness(sb.toString());
                    if (freqmode) {
                        b.setFreqratio(freqratio(b));
                        b.assignFr(query);
                    }
                    b.setTfMap(tfcalculator(b, query));
                    businessSet.add(b);
                     //construct a business with sb
                    sb = new StringBuilder();
                    continue;
                } else {
                    sb.append(result);
                }

            }
        } catch (IOException e) {
            System.out.println("couldn't find file");
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                System.out.println("could't close file");
            }
        }
    }

    //this is a helper method that converts a String that represents all of a Business' data into a Business object
    public Business strToBusiness(String sb) {
        List<String> BusFieldStrings = Arrays.asList(sb.split(","));
        Business b = null;
        //fields that we will fill:
        String businessID = "";
        String businessName = "";
        String businessAddress = "";
        String reviews = "";
        int reviewCharCount = 0;
        for (int x = 0; x < BusFieldStrings.size(); x++) {
            if (x == 0) {
                //note: trim just removes whitespace (spaces) that comes before or after the string
                businessID = BusFieldStrings.get(x).trim();
            } else if (x == 1) {
                businessName = BusFieldStrings.get(x).trim();
            } else if (x == 2) {
                businessAddress = BusFieldStrings.get(x).trim();
            } else {
                reviews = BusFieldStrings.get(x).trim();
                reviewCharCount = charCount(reviews);
                //updates dictionary
                b = new Business(businessID, businessName, businessAddress, reviews, reviewCharCount, null);
                b.setNumWords(this.dictionaryHelper(reviews));
            }
        }
        return b;
    }

    //helper function that counts how many characters there are in a business' reviews
    public int charCount(String reviews) {
        return reviews.length();
    }

    //updates the dictionary with a new batch of reviews
    public int dictionaryHelper(String reviews) {
        //split the reviews into a list of individual words
        List<String> wordsInReviews = Arrays.asList(reviews.split(" "));
        int numWords = wordsInReviews.size();
        // this removes duplicate words in the batch of reviews (we don't want to double count words from one batch
        // of reviews)
        List<String> wordsInReviews_noDups = new ArrayList<>();
        for (String s : wordsInReviews) {
            if (!wordsInReviews_noDups.contains(s.toLowerCase())) {
                wordsInReviews_noDups.add(s.toLowerCase());
            }
        }
        //now we go through the list of unique words and adjust the dictionary accordingly
        for (String s : wordsInReviews_noDups) {
            //if the dictionary doesn't already have the word, then add it to the dictionary with value 1
            if (!dictionary.containsKey(s.toLowerCase())) {
                dictionary.put(s.toLowerCase(), new Integer(1));
            } else {
                //otherwise, increment the value associated to the word by 1
                dictionary.put(s.toLowerCase(), new Integer(dictionary.get(s).intValue() + 1));
            }
        }
        return numWords;
    }

    public Map<String, Integer> tfcalculator(Business b, String query) {
        Map<String, Integer> tfMap = new HashMap<>();
        List<String> wordsInReviews = Arrays.asList(b.reviews.split(" "));
        List<String> keyWords = Arrays.asList(query.split(" "));
        for (String keyWord: keyWords) {
            int tf = 0;
            for (String s: wordsInReviews) {
                if (s.equals(keyWord)) {
                    tf++;
                }
                tfMap.put(keyWord, tf);
            }
        }
        return tfMap;
    }

    public Map<String, Double> tfidfcalculator(Business b, String query) {
        Map<String, Double> tfidfMap = new HashMap<>();
        List<String> keyWords = Arrays.asList(query.split(" "));
        for (String keyWord: keyWords) {
            double unroundedtfidf = (double) b.getTfMap().get(keyWord) / (double) dictionary.get(keyWord);
            double tfidf = Math.round(unroundedtfidf*100.0)/100.0;
            tfidfMap.put(keyWord, tfidf);
        }
        return tfidfMap;
    }



    public Map<String, Integer> generateWordFreqMap(List<String> wordsInReviews) {
        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String s: wordsInReviews) {
            if (!wordFrequencies.containsKey(s)) {
                wordFrequencies.put(s, new Integer(1));
            } else {
                wordFrequencies.put(s, new Integer(wordFrequencies.get(s).intValue()+1));
            }
        }
        return wordFrequencies;
    }

    public Map<String, Double> freqratio(Business b) {
        //store all the words in the reviews in a list
        List<String> wordsInReviews = Arrays.asList(b.reviews.split(" "));
        //first, create a map that correlates each word to the number of times it appears in a business' reviews
        Map<String, Integer> wordFrequencies = generateWordFreqMap(wordsInReviews);
        Map<String, Double> ratioMap = new HashMap<>();
        for (String s: wordFrequencies.keySet()){
            Double d = wordFrequencies.get(s)/(double) b.numwords;
            ratioMap.put(s, d);
        }
        return ratioMap;
    }




}









