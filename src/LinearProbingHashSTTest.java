import java.util.Random;
import java.util.List;
import java.util.ArrayList;

// Klasa zajmująca się testowaniem
public class LinearProbingHashSTTest {

    // Metoda sprawdzająca czy liczba jest liczbą pierwszą
    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num % 2 == 0 && num != 2) return false;
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }

    // Metoda wyszukująca kolejny liczb pierwszych
    private static int nextPrime(int start) {
        int next = start;
        while (!isPrime(next)) {
            next++;
        }
        return next;
    }

    // Metoda przeprowadzająca testy na podstawie podanych parametrów
    private static void performOperations(String testName, List<Integer> keys, int initialSize, Boolean isPrimeTable) {
        // Początkowa inicjalizacja klasy odpowiedzialnej za haszowanie z próbkowaniem liniowym
        LinearProbingHashST symbolTable;

        // Początkowa inicjalizacja całkowitej wartości czasu wykonywania
        long totalPutTime = 0;
        long totalGetTime = 0;
        long totalDeleteTime = 0;

        // Pętla wykonująca dane operacja 10 razy
        for (int i = 0; i < 10; i++){

            // Sprawdzenie czy testowany przypadek wykorzystuje tablice zwykłe czy z liczbami pierwszymi
            if(!isPrimeTable){
                symbolTable = new LinearProbingHashST<>(initialSize);
            }else{
                symbolTable = new LinearProbingHashSTPrime(initialSize);
            }

            // Rozpoczęcie mierzenia czasu
            long startTime = System.nanoTime();

            // Wykonywanie operacji put dla każdego klucza.
            for (Integer key : keys) {
                symbolTable.put(key, "value");
            }
            long endTime = System.nanoTime();

            // Dodawanie uzyskanego czasu
            totalPutTime += (endTime - startTime);

            startTime = System.nanoTime();
            for (Integer key: keys) {
                symbolTable.get(key);
            }
            endTime = System.nanoTime();
            totalGetTime += (endTime - startTime);

            startTime = System.nanoTime();
            for(Integer key: keys) {
                symbolTable.delete(key);
            }
            endTime = System.nanoTime();
            totalDeleteTime += (endTime - startTime);
        }

        // Wypisanie wyników końcowych z uśrednionym czasem.
        System.out.println(String.format("%s; put; %d; %d; ", testName, keys.size(), totalPutTime / 10));
        System.out.println(String.format("%s; get; %d; %d; ", testName, keys.size(), totalGetTime / 10));
        System.out.println(String.format("%s; delete; %d; %d; ", testName, keys.size(), totalDeleteTime / 10));

    }

    // Metoda przeprowadzająca testy. Przekazuje czy testowane klucze korzystają z tablicy z liczbami pierwszymi
    private static void performTest(String testName, List<Integer> keys, int initialSize) {
        performOperations(testName, keys, initialSize, false);
        performOperations(testName + " (Prime size table)", keys, initialSize, true);
    }


    // Generowanie i testowanie kluczy o identycznych wartościach
    public static void identicalKeysTest(int numberOfKeys, Integer keysValue, int initialSize) {
        List<Integer> keys = new ArrayList<>(numberOfKeys);
        for (int i = 0; i < numberOfKeys; i++) {
            keys.add(keysValue);
        }
        performTest("Identical", keys, initialSize);
    }

    // Generowanie i testowanie kluczy rosnących
    public static void incrementingKeysTest(int numberOfKeys, Integer firstKeyValue, int initialSize) {
        List<Integer> keys = new ArrayList<>(numberOfKeys);
        for (int i = firstKeyValue; i < numberOfKeys + firstKeyValue; i++) {
            keys.add(i);
        }
        performTest("Incrementing", keys, initialSize);
    }

    // Generowanie i terowanie kluczy co drugi, czyli albo parzystych albo nieparzystych
    public static void everySecondKeysTest(int numberOfKeys, Integer firstKeyValue, int initialSize) {
        List<Integer> keys = new ArrayList<>(numberOfKeys);
        for (int i = firstKeyValue; i < (numberOfKeys*2) + firstKeyValue; i += 2) {
            keys.add(i);
        }
        String label = (firstKeyValue % 2 == 0) ? "Every second (Even)" : "Every second (Odd)";
        performTest(label, keys, initialSize);
    }

    // Generowanie i testowanie kluczy które są liczbami pierwszymi
    public static void primeKeysTest(int numberOfKeys, int initialSize) {
        List<Integer> keys = new ArrayList<>(numberOfKeys);
        int prime = 2;
        for (int i = 0; i < numberOfKeys; i++) {
            keys.add(prime);
            prime = nextPrime(prime + 1);
        }
        performTest("Prime", keys, initialSize);
    }

    // Generowanie i testowanie kluczy o równomiernym rozkładzie
    public static void uniformRandomKeysTest(int numberOfKeys, int initialSize) {
        Random rand = new Random();
        List<Integer> keys = new ArrayList<>(numberOfKeys);
        for (int i = 0; i < numberOfKeys; i++) {
            keys.add(rand.nextInt(numberOfKeys));
        }
        performTest("Uniform random", keys, initialSize);
    }

    // Generowanie i testowanie kluczy o rozkładzie normalnym
    public static void gaussianRandomKeysTest(int numberOfKeys, int initialSize) {
        Random rand = new Random();
        List<Integer> keys = new ArrayList<>(numberOfKeys);
        for (int i = 0; i < numberOfKeys; i++) {
            keys.add((int) (rand.nextGaussian() * numberOfKeys));
        }
        performTest("Gaussian random", keys, initialSize);
    }
}
