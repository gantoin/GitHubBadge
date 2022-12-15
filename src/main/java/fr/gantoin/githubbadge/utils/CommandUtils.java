package fr.gantoin.githubbadge.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CommandUtils {

    public static void executeOneLineCommand(String command) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while executing command: " + command, e);
        }
    }


    public static void executeCommand(String resultPath, List<String> commands) {
        commands.add(resultPath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(commands);
            Process process = processBuilder.start();
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            if (error.ready()) {
                String line;
                while ((line = error.readLine()) != null) {
                    System.out.println("error: " + line);
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while executing command", e);
        }
    }
}
