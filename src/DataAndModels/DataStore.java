package com.example.catsonaquest.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Android version of DataStore
 * Uses SharedPreferences instead of .txt files for mobile persistence.
 */
public class DataStore {
    private static final String PREF_NAME = "CatsQuestPrefs";
    private static final String KEY_USERS = "users_list";
    private static final String KEY_LISTINGS = "listings_list";

    private static ArrayList<com.google.firebase.firestore.auth.User> users = new ArrayList<>();
    private static ArrayList<JobListing> listings = new ArrayList<>();
    private static com.google.firebase.firestore.auth.User currentUser = null;

    // We need the "Context" to save data in Android
    public static void init(Context context) {
        loadData(context);
        if (users.isEmpty()) {
            seedData(context);
        }
    }

    public static com.google.firebase.firestore.auth.User login(String email, String password) {
        for (com.google.firebase.firestore.auth.User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                currentUser = u;
                return u;
            }
        }
        return null;
    }

    // --- SAVING DATA (The Android Way) ---
    public static void saveUsers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(users);
        editor.putString(KEY_USERS, json);
        editor.apply();
    }

    private static void loadData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String userJson = prefs.getString(KEY_USERS, null);
        Type type = new com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken<ArrayList<com.google.firebase.firestore.auth.User>>() {}.getType();
        if (userJson != null) {
            users = gson.fromJson(userJson, type);
        }
    }

    private static void seedData(Context context) {
        users.add(new com.google.firebase.firestore.auth.User("Juan Dela Cruz", "password123", "juan.delacruz@g.msuiit.edu.ph", "STUDENT"));
        saveUsers(context);
    }

    public static com.google.firebase.firestore.auth.User getCurrentUser() { return currentUser; }
}