// Klasa mająca na celu wykorzystywanie liczb pierwszych jako elementów wielkości tablic
public class LinearProbingHashSTPrime<Integer, String> extends LinearProbingHashST{

    public LinearProbingHashSTPrime(int cap) {
        // Wywołanie metody z klasy nadrzędnej
        super(cap);
    }

    // Wyszukiwanie liczby pierwszej znajdujacej się po podanej liczbie
    private int nextPrime(int input) {
        int counter;
        input++;
        while (true) {
            counter = 0;
            for (int i = 1; i <= Math.sqrt(input); i++) {
                if (input % i == 0) counter++;
                if (counter > 1) break;
            }
            if (counter > 1) {
                input++;
                continue;
            }
            break;
        }
        return input;
    }

    // Przesłanianie metody resize własną, która będzie wykorzystywała liczby pierwsze do zmieniania rozmiaru tablic
    @Override
    protected void resize(int newCap){
        // Tworzenie chwilowego obiektu na do którego będą przepisywane elementy
        LinearProbingHashST<Integer, String> tempHashST;
        tempHashST = new LinearProbingHashST<>(nextPrime(newCap));

        // Sprawdany jest każdy index obecnej tablicy, jeśli nie jest pusty, to element zostaje przenioesony
        for (int i = 0; i < capacity; i++)
            if (keys[i] != null)
                tempHashST.put((Integer) keys[i], (String) values[i]);

        // Przypisywanie uzyskanych tablic i wartości o obecnego obiektu
        keys = tempHashST.keys;
        values = tempHashST.values;
        capacity = tempHashST.capacity;
    }
}
