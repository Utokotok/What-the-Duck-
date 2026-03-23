package com.groupone.wtd.Utils;

import com.badlogic.gdx.Gdx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {

    private static final String FILE_PATH = "leaderboard.json";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    // ── Data record ──────────────────────────────────────────────────────────

    public static class Entry {
        public final int    score;
        public final String gameMode;
        public final String date;
        public final String time;
        public final String name;

        public Entry(int score, String gameMode, String date, String time, String name) {
            this.score    = score;
            this.gameMode = gameMode;
            this.date     = date;
            this.time     = time;
            this.name     = name;
        }
    }

    // ── Write ─────────────────────────────────────────────────────────────────

    /** Appends a new score entry (with current date/time) to leaderboard.json. */
    public static void saveScore(int score, String gameMode, String name) {
        LocalDateTime now  = LocalDateTime.now();
        String date        = now.format(DATE_FMT);
        String time        = now.format(TIME_FMT);

        List<Entry> entries = readEntries();
        entries.add(new Entry(score, gameMode, date, time, name));
        writeEntries(entries);
    }

    // ── Read + Insertion Sort ────────────────────────────────────────────────

    /**
     * Reads leaderboard.json, sorts all entries by score descending
     * using insertion sort, and returns the sorted list.
     */
    public static List<Entry> getLeaderboard() {
        List<Entry> entries = readEntries();
        insertionSort(entries);
        return entries;
    }

    // ── Insertion sort (descending by score) ─────────────────────────────────

    private static void insertionSort(List<Entry> list) {
        int n = list.size();
        for (int i = 1; i < n; i++) {
            Entry key = list.get(i);
            int   j   = i - 1;
            // Shift entries with a smaller score one position to the right
            while (j >= 0 && list.get(j).score < key.score) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // ── JSON helpers (no external library) ───────────────────────────────────

    private static List<Entry> readEntries() {
        List<Entry> entries = new ArrayList<>();
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.local(FILE_PATH);
            if (!file.exists()) return entries;

            String raw = file.readString("UTF-8").trim();
            // Strip outer [ ]
            if (raw.startsWith("[")) raw = raw.substring(1);
            if (raw.endsWith("]"))   raw = raw.substring(0, raw.length() - 1);
            raw = raw.trim();
            if (raw.isEmpty()) return entries;

            // Split on object boundaries: ...},{...
            // We split on "},\s*{" and reconstruct each object
            String[] objects = raw.split("\\},\\s*\\{");
            for (String obj : objects) {
                obj = obj.replace("{", "").replace("}", "").trim();
                // Fields: "score": 100, "gameMode": "WordHunt", "date": "...", "time": "..."
                int    score    = 0;
                String gameMode = "";
                String date     = "";
                String time     = "";
                String name     = "";

                for (String token : obj.split(",")) {
                    token = token.trim();
                    String[] kv = token.split(":", 2);
                    if (kv.length != 2) continue;
                    String key = kv[0].trim().replace("\"", "");
                    String val = kv[1].trim().replace("\"", "");
                    switch (key) {
                        case "score":    score    = Integer.parseInt(val); break;
                        case "gameMode": gameMode = val;                   break;
                        case "date":     date     = val;                   break;
                        case "time":     time     = val;                   break;
                        case "name":     name     = val;                   break;
                    }
                }
                entries.add(new Entry(score, gameMode, date, time, name));
            }
        } catch (Exception e) {
            Gdx.app.log("Leaderboard", "Error reading leaderboard: " + e.getMessage());
        }
        return entries;
    }

    private static void writeEntries(List<Entry> entries) {
        try {
            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < entries.size(); i++) {
                Entry e = entries.get(i);
                sb.append("  {")
                  .append("\"score\": ").append(e.score).append(", ")
                  .append("\"gameMode\": \"").append(e.gameMode).append("\", ")
                  .append("\"date\": \"").append(e.date).append("\", ")
                  .append("\"time\": \"").append(e.time).append("\", ")
                  .append("\"name\": \"").append(e.name).append("\"")
                  .append("}");
                if (i < entries.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("]");
            Gdx.files.local(FILE_PATH).writeString(sb.toString(), false, "UTF-8");
        } catch (Exception e) {
            Gdx.app.log("Leaderboard", "Error writing leaderboard: " + e.getMessage());
        }
    }
}
