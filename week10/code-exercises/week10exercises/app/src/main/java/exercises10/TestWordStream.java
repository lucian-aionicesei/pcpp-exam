//Exercise 10.?
//JSt vers Oct 23, 2023

package exercises10;

// gradle -PmainClass=exercises10.TestWordStream run

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import benchmarking.Timer;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "src/main/resources/english-words.txt";
    // String url = "https://staunstrups.dk/jst/english-words.txt";
    System.out.println("Words count: " + readWords(filename).count()); // Words
    // count: 235,886
    // System.out.println("Words count: " + readWordsFromUrl(url).count());

    // 10.2.2 print the first 100 words
    // readWords(filename).limit(100).forEach(System.out::println);

    // 10.2.3 print all words with length >= 22
    // readWords(filename).filter(x -> x.length() >=
    // 22).forEach(System.out::println);

    // 10.2.4 print some word with length >= 22
    // readWords(filename).filter(x -> x.length() >=
    // 22).limit(1).forEach(System.out::println);
    // // A different approach
    // readWords(filename)
    // .filter(x -> x.length() >= 22)
    // .findFirst()
    // .ifPresent(System.out::println);

    // 10.2.5 ﬁnd all palindromes and print them (wrapped in a timer)
    // Timer t = new Timer();
    // readWords(filename).filter(i ->
    // isPalindrome(i)).forEach(System.out::println);
    // double time = t.check() * 1e9;
    // System.out.printf("%6.1f ns%n", time);

    // 10.2.6 ﬁnd all palindromes parallelly and print them (wrapped in a timer)
    // Timer parallelT = new Timer();
    // readWords(filename).parallel().filter(i ->
    // isPalindrome(i)).forEach(System.out::println);
    // double timeParallel = parallelT.check() * 1e9;
    // System.out.printf("%6.1f ns%n", timeParallel);

    // 10.2.8
    // DoubleStream ds =
    // DoubleStream.of(readWords(filename).mapToDouble(String::length).toArray());
    // DoubleSummaryStatistics stats = ds.summaryStatistics();
    // // A different approach
    // DoubleSummaryStatistics stats = readWords(filename)
    // .mapToDouble(String::length)
    // .summaryStatistics();

    // System.out.printf("min=%g, max=%g, mean=%g",
    // stats.getMin(), stats.getMax(), stats.getAverage());
  }

  public static Stream<String> readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      return reader.lines();
    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }

  private static Stream<String> readWordsFromUrl(String theUrl) {
    try {
      URL url = new URL(theUrl);
      URLConnection urlConnection = url.openConnection();

      // wrapping the urlconnection in a bufferedreader
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      return bufferedReader.lines();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Stream.<String>empty();
  }

  public static boolean isPalindrome(String str) {
    // Initializing an empty string to store the reverse of the original str
    String rev = "";

    // Initializing a new boolean variable for the answer
    boolean answer = false;

    for (int i = str.length() - 1; i >= 0; i--) {
      rev = rev + str.charAt(i);
    }

    // Checking if both the strings are equal
    if (str.equals(rev)) {
      answer = true;
    }
    return answer;
  }

  public static Map<Character, Integer> letters(String s) {
    Map<Character, Integer> res = new TreeMap<>();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (res.containsKey(c)) {
        res.put(c, res.get(c) + 1);
      } else {
        res.put(c, 1);
      }
    }
    return res;
  }
}
