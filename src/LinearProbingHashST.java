public class LinearProbingHashST<Key, Value> {

    // Liczba par klucz-wartość w tablicy haszującej
    protected int size;

    // Rozmiar tablicy haszującej
    protected int capacity;

    // Tablica kluczy
    protected Key[] keys;

    // Tablica wartości
    protected Value[] values;

    //// Stałe służące do kontroli rozmiaru tablici
    // Stała określająca zapełnienie tablicy przy którym trzeba ją powiększyć.
    private static final double RESIZE_THRESHOLD = 0.5;

    // Stała określająca zapełnienie tablicy przy którym trzeba ją zmniejszyć
    private static final double SHRINK_THRESHOLD = 0.125;

    // Stała określająca o jaką wielokrotność ma zostać powiększona lub zmniejszona tablica.
    private static final int RESIZE_FACTOR = 2;

    // Konstruktor klasy
    public LinearProbingHashST(int cap) {

        // Przypisanie zadanej wielkości tablicy do zmiennej.
        capacity = cap;

        // Tworzenie tablic kluczy i wartości o zadanej wielkości
        keys = (Key[]) new Object[capacity];
        values = (Value[]) new Object[capacity];
    }

    // Metoda hashująca klucze
    private int hash(Key key) {
        /*
        Testowane dane posiadają klucze typu 'Integer'
        W takim wypadku metoda hashCode zwaraca wartość.

        public final class Integer extends Number implements Comparable<Integer> {
        // ...
            private final int value;
        // ...
            @Override
            public int hashCode() {
                return Integer.hashCode(value);
            }
        // ...
            public static int hashCode(int value) {
                return value;
            }
            // ...
        }
         */
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    // Metoda do zmiany rozmiaru tablicy
    protected void resize(int newCap) {
        // Tworzenie chwilowego obiektu na do którego będą przepisywane elementy
        LinearProbingHashST<Key, Value> tempHashST;
        tempHashST = new LinearProbingHashST<Key, Value>(newCap);

        // Sprawdany jest każdy index obecnej tablicy, jeśli nie jest pusty, to element zostaje przenioesony
        for (int i = 0; i < capacity; i++)
            if (keys[i] != null)
                tempHashST.put(keys[i], values[i]);

        // Przypisywanie uzyskanych tablic i wartości o obecnego obiektu
        keys = tempHashST.keys;
        values = tempHashST.values;
        capacity = tempHashST.capacity;
    }

    // Metoda umieszczające pary klucz i wartość w tablicach
    public void put(Key key, Value val) {
        // Sprawdzenie, czy tablica jest wystarczająco zapełniona, aby ją powiększyć
        if (size >= capacity * RESIZE_THRESHOLD) resize(capacity * RESIZE_FACTOR);

        // Odnajdywanie pierwszego wolnego miejsca (nulla) zaczynając od wyliczonego hasha
        // Mamy pewność, że jest miejsce w tablicy i kiedyś znajdziemy nulla, bo wcześniej powiększaliśmy tablicy
        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % capacity)
            // Jeśli klucz się powtórzy, zmieniana jest tylko znajdująca się z nią wartość
            if (keys[i].equals(key)) { values[i] = val; return; }

        // Przypisywanie elemntów do tablicy wraz z powiększeniem jej zapełnienia
        keys[i] = key;
        values[i] = val;
        size++;
    }

    // Metoda uzyskująca wartość na podstawie danego klucza
    public Value get(Key key) {
        // Wyszukiwanie klucza rozpoczynając od pozycji wyliczonego hasha. Jeśli potkany zostanie null, oznacza to, że klucz nie występuje
        for (int i = hash(key); keys[i] != null; i = (i + 1) % capacity)
            if (keys[i].equals(key))
                return values[i];
        return null;
    }

    // Metoda usuwająca dany klucz i wartość
    public void delete(Key key)
    {
        // Sprawdzenie czy klucz znajduje się w tablicy
        if (!contains(key)) return;

        // Uzyskiwanie hasha z klucza i szukanie go w tablici
        int i = hash(key);
        while (!key.equals(keys[i]))
            i = (i + 1) % capacity;

        // Przypisywanie uzyskanemu elementowi wartości null
        keys[i] = null;
        values[i] = null;

        // Po usunięciu elementu należy zahashować ponownie wszystkie klucze które znajdowały się po nim w klastrze
        i = (i + 1) % capacity;
        while (keys[i] != null)
        {
            Key keyToRedo = keys[i];
            Value valToRedo = values[i];
            keys[i] = null;
            values[i] = null;
            size--;
            put(keyToRedo, valToRedo);
            i = (i + 1) % capacity;
        }
        size--;

        // Wpradzenie, czy tablica posiada wystarczająco mało elementów, aby ją zmniejszyć
        if (size > 0 && size <= capacity * SHRINK_THRESHOLD) resize(capacity / RESIZE_FACTOR);
    }

    // Metoda sprawdzająca, czy w tablicach znajduja się dany klucz
    public boolean contains(Key key) {
        // Jeśli podany klucz jest nullem, zwracany jest fasle
        if (key == null) return false;
        // Sprawdzanie elementów tablicy aż do odnalezienia pasującego klucza, lub nulla.
        for (int i = hash(key); keys[i] != null; i = (i + 1) % capacity)
            if (keys[i].equals(key))
                return true;
        return false;
    }

}