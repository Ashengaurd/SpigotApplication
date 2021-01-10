package me.ashenguard.api.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebReader {
    private String url;
    private int retry = 5;
    private boolean logging = false;

    public WebReader(String url) {
        this.url = url;
    }
    public WebReader(String url, int retry) {
        this.url = url;
        this.retry = retry;
    }
    public WebReader(String url, boolean logging) {
        this.url = url;
        this.logging = logging;
    }
    public WebReader(String url, int retry, boolean logging) {
        this.url = url;
        this.retry = retry;
        this.logging = logging;
    }

    private static boolean logLineEnded = true;
    private static List<String> logs = new ArrayList<>();
    public static List<String> getLogs() {
        return getLogs(false);
    }
    public static List<String> getLogs(boolean clean) {
        List<String> logs = new ArrayList<>(WebReader.logs);
        if (clean) WebReader.logs = new ArrayList<>();
        return logs;
    }

    private void log(String string, boolean endLine) {
        if (logLineEnded) logs.add(string);
        else logs.set(logs.size() - 1, logs.get(logs.size() - 1) + string);
        logLineEnded = endLine;

        if (!logging) return;
        if (endLine) System.out.println(string);
        else System.out.print(string);
    }
    private void log(String string) {
        log(string, true);
    }

    public List<String> readLines() {
        try {
            log(String.format("Initializing connection to %s", url));
            URLConnection con = new URL(url).openConnection();
            for (int i = 0; i < retry; i++) {
                log(String.format("Connection attempt %d: ", i + 1), false);
                String redirect = con.getHeaderField("Location");
                if (redirect != null) {
                    log(String.format("Failed, %d Attempts remained(Redirected to %s)", 4 - i, redirect));
                    con = new URL(redirect).openConnection();
                }
                else break;
            }
            if (con.getHeaderField("Location") != null) {
                log("Failed, No attempts remained.");
                log("Connection closed");
                return new ArrayList<>();
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(con.getInputStream()));
            log("Successful.");
            return buffer.lines().collect(Collectors.toList());
        } catch (IOException exception) {
            log(String.format("Connection lost due %s", exception.getMessage()));
            return new ArrayList<>();
        }
    }

    public String read() {
        List<String> lines = readLines();
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(line.replace("\n", "")).append("\n");
        }

        return result.toString();
    }

    public JSONObject readJSON() {
        String data = read();
        try {
            return new JSONObject(data);
        } catch (JSONException exception) {
            log(String.format("JSON failed due %s", exception.getMessage()));
            return null;
        }
    }
}
