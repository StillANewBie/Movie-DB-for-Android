package edu.weber.favmovies.api;

import android.util.Log;

import edu.weber.favmovies.api.Authorization;
import edu.weber.favmovies.db.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class APIDemo1 {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.omdbapi.com/?t=batman&apikey=" + Authorization.AUTH_TOKEN);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "" + Authorization.AUTH_TOKEN);

        connection.connect();

        int status = connection.getResponseCode();
        String rawJSON = "";

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection
                        .getInputStream()));
                rawJSON = br.readLine();

                System.out.println(rawJSON);

                break;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Movie movie;

        JsonObject body = gson.fromJson(rawJSON, JsonObject.class);
        movie = gson.fromJson(rawJSON, Movie.class);

        System.out.println(movie);
    }
}
