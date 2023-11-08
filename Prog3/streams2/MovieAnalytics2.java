import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

public class MovieAnalytics2 {
    private static ArrayList<Movie> movies;

    MovieAnalytics2() {

    }

    public void populateWithData(String fileName) throws IOException {
        try(var br = new BufferedReader(new FileReader(fileName))) {
            List<Movie> movieList = br.lines()
                    .map(line -> line.split(";"))
                    .map(words -> new Movie(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]), words[3],
                            Double.parseDouble(words[4]), words[5]))
                    .toList();
            movies = new ArrayList<>(movieList);
        }
    }

    public void printCountByDirector(int n) {
        HashMap<String, Integer> map = new HashMap<>();
        LinkedHashMap<String, Integer> reverseSortedMap;

        Map<String, List<Movie>> moviesByDirector = movies
                .stream()
                .collect(
                Collectors.groupingBy(Movie::getDirector)
        );

        moviesByDirector.forEach((s,l) -> map.put(s, l.size()));

        reverseSortedMap = map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        reverseSortedMap.forEach((key, value) -> System.out.println(key + ": " + value + " movies"));
    }

    public void printAverageDurationByGenre() {
        HashMap<String, Float> map = new HashMap<>();
        LinkedHashMap<String, Float> sorted = new LinkedHashMap<>();

        Map<String, List<Movie>> moviesByGenre = movies
                .stream()
                .collect(
                Collectors.groupingBy(Movie::getGenre)
        );

        DoubleAdder adder = new DoubleAdder();
        moviesByGenre.forEach((s, l) ->
                    {l.forEach(m -> adder.add(m.getDuration()));
                    map.put(s, adder.floatValue()/l.size());
                    adder.reset();});

        sorted = map.entrySet().stream()
                .sorted(Map.Entry.<String, Float>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        sorted.forEach((key, value) -> System.out.printf("%s: %.2f\n", key, value));
    }

    public void printAverageScoreByGenre() {
        HashMap<String, Float> map = new HashMap<>();
        LinkedHashMap<String, Float> sorted = new LinkedHashMap<>();

        Map<String, List<Movie>> moviesByGenre = movies
                .stream()
                .collect(
                Collectors.groupingBy(Movie::getGenre)
        );

        DoubleAdder adder = new DoubleAdder();
        moviesByGenre.forEach((s, l) ->
        {l.forEach(m -> adder.add(m.getScore()));
            map.put(s, adder.floatValue()/l.size());
            adder.reset();});

        sorted = map.entrySet().stream()
                        .sorted(Map.Entry.<String, Float>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        sorted.forEach((key, value) -> System.out.printf("%s: %.2f\n", key, value));
    }
}
