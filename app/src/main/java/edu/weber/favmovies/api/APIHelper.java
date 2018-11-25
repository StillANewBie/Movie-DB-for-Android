package edu.weber.favmovies.api;

import edu.weber.favmovies.db.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIHelper {

    public static List<Movie> getSearchResults(String name, String year) {
        URL url = null;
        try {
            url = new URL("https://www.omdbapi.com/?s=" + name + "&y=" + year
                    + "&apikey=" + Authorization.AUTH_TOKEN);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Authorization", "" + Authorization.AUTH_TOKEN);
        String rawJSON = "";

        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int status = 0;
        try {
            status = connection.getResponseCode();


        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection
                        .getInputStream()));
                rawJSON = br.readLine();

                System.out.println(rawJSON);

                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        JsonObject body = gson.fromJson(rawJSON, JsonObject.class);
        List<Movie> movies = new ArrayList<>();

        for (JsonElement jo : body.get("Search").getAsJsonArray()) {
            movies.add(gson.fromJson(jo, Movie.class));
        }

        return movies;
    }

    public static void main(String[] args) throws IOException {

        System.out.println(getSearchResults("batman", "2008"));
        System.out.println();
    }
}
