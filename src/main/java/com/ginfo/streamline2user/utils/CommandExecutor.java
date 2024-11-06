package com.ginfo.streamline2user.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandExecutor {

    public static String getCurrentUsername() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("powershell.exe whoami");
        String username = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            username = reader.readLine();
            process.waitFor();
        }

        if (username != null) {
            String[] parts = username.split("\\\\");
            return parts.length > 1 ? parts[1] : username;
        }

        return null;
    }

    public static String getUserId(String username) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("powershell.exe query user " + username);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            String userId = null;
            Pattern pattern = Pattern.compile("\\s+(\\d+)\\s+");

            while ((line = reader.readLine()) != null) {
                if (line.contains(username)) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        userId = matcher.group(1);
                        break;
                    }
                }
            }

            process.waitFor();
            System.out.println(userId);
            return userId;
        }
    }

    public static void logoffUser(String userId) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("powershell.exe logoff " + userId);
        process.waitFor();
    }
}
