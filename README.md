# yelp_tfidf
tf-idf (term frequency–inverse document frequency) is a widely used metric for large datasets in order to determine the most relevant keywords. 
My program takes in a dataset from Yelp, and initially, my project determined the top 30 keywords pertaining to each business in the dataset by using the tf-idf metric. I began by sorting the businesses with respect to the length of their reviews (in characters) and printing them out. 

In Dec 2020, I started working on making it into a simple search engine that accepts a query and that outputs the 10 most relevant restaurants pertaining to the keywords in the query. I have implemented two approaches for the information retrieval: tf-idf and frequency ratios (I divide the number of times a keyword shows up in a business' reviews and divide it by the total number of words in the reviews). My backend code is in the YelpAnalysis class, and I have a basic functioning GUI that can be launched from the SearchEngine class.

IDEs such as intelliJ restrict the file size of attached .txt files, so the attached .txt file is relatively small (20mb), but it is possible to set the file stream to a larger parsed Yelp dataset by referring to its file path. They can be obtained here: https://www.yelp.com/dataset/download

My code is fully functional but it would be a much quicker search engine if I used JSON to store my values so that I don't need to parse the entire .txt file every time. This will be my next step.
To make this project, I made use of:

- Comparators
- Standard parsing techniques
- Linked Hash Maps, regular Hash Maps, and map conversions
- PriorityQueues and Guava's MinMaxPriorityQueue
- Input Streams from a file

