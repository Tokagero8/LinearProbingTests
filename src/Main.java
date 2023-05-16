public class Main {
    public static void main(String[] args) {
        // Ilość danych która ma zostać przetestowana
        int[] testData = {100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000};

        // Pętla wykonująca testy dla każdej wielkości danych.
        for (int i = 0; i < testData.length; i++) {
            LinearProbingHashSTTest.identicalKeysTest(testData[i], 1, 16);
            //LinearProbingHashSTTest.incrementingKeysTest(testData[i], 1, 16);
            LinearProbingHashSTTest.everySecondKeysTest(testData[i], 1, 16);
            LinearProbingHashSTTest.everySecondKeysTest(testData[i], 2, 16);
            LinearProbingHashSTTest.primeKeysTest(testData[i], 16);
            LinearProbingHashSTTest.uniformRandomKeysTest(testData[i], 16);
            LinearProbingHashSTTest.gaussianRandomKeysTest(testData[i], 16);
        }
    }
}