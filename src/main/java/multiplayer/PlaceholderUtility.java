package multiplayer;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Klasa nawiązująca połączenie z serwerem.
 */
public class PlaceholderUtility {

    /**
     * Bazowy adres URL serwera.
     */
    private static final String BaseUrl = "https://bitter-jellyfish-100.loca.lt/";

    /**
     * Prawdziwe, jeśli połączenie zostało nawiązane.
     */
    private static boolean initialized = false;
    /**
     * Obiekt klasy Retrofit.
     */
    private static Retrofit retrofitInstance;

    /**
     * Obiekt klasy przechowującej metody HTTP.
     */
    private static JsonPlaceholderAPI placeholderInstance;

    /**
     * Statyczny obiekt klasy JFrame przeznaczony na komunikat o błędzie
     */
    private static JFrame frame;

    /**
     * Metoda inicjująca połączenie z serwerem.
     */
    public static void initialize() {
        if (initialized) return;

        retrofitInstance = createRetrofitInstance();
        placeholderInstance = retrofitInstance.create(JsonPlaceholderAPI.class);
        initialized = true;
    }

    // --------------- Retrofit --------------------
    /**
     * Metoda tworząca klienta połączenia z serwerem.
     * @return Zwraca klienta połączenia.
     */
    private static Retrofit createRetrofitInstance() {
        if (!initialized) {
            return new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder().serializeNulls().create()))
                    .build();
        }
        else return retrofitInstance;
    }

    /**
     * Getter klienta połączenia.
     * @return Klient połączenia.
     */
    public static Retrofit getRetrofitInstance() {
        return retrofitInstance;
    }

    // -------------- Placeholder -------------------
    /**
     * Getter interfejsu z metodami HTTP.
     * @return Interfejs z metodami HTTP.
     */
    public static JsonPlaceholderAPI getPlaceholderInstance() {
        return placeholderInstance;
    }

    /**
     * Metoda sprwdzająca połączenie z internetem.
     * @return Zraca prawdę, gry udało sie nazwiązać połączenie internetowe.
     */
    public static boolean hasInternetAccess() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            System.out.println("Internet is not connected");
            frame = new JFrame();
            JOptionPane.showMessageDialog(
                    frame,
                    "No internet connection.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
}
