import java.io.*;
import java.util.*;

public class TerminalLogger {
    private static final String LOG_FILE = "logs/terminal_log.txt";
    private static final String SUMMARY_FILE = "logs/summary.txt";

    public static void main(String[] args) {
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_FILE))) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Custom Terminal (type 'exit' to quit):");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();

                // Exit condition
                if (command.equalsIgnoreCase("exit")) {
                    break;
                }

                // Log the command
                logWriter.write("Command: " + command + "\n");

                // Execute the command
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder();
                    processBuilder.command("bash", "-c", command);
                    Process process = processBuilder.start();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    String line;
                    StringBuilder output = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    while ((line = errorReader.readLine()) != null) {
                        output.append(line).append("\n");
                    }

                    // Log the output
                    logWriter.write("Output:\n" + output + "\n");
                    logWriter.flush();

                    // Print the output to the terminal
                    System.out.println(output);
                } catch (IOException e) {
                    System.out.println("Error executing command: " + e.getMessage());
                }
            }

            // Process the log file to generate a summary using AI/ML
            generateSummaryWithAI();

            System.out.println("Session ended. Summary saved to " + SUMMARY_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateSummaryWithAI() {
        try {
            System.out.println("Calling Python summarizer...");
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "summarize.py", LOG_FILE, SUMMARY_FILE);
            processBuilder.redirectErrorStream(true); // Combine stdout and stderr
            Process process = processBuilder.start();
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Python] " + line);
            }
    
            int exitCode = process.waitFor();
            System.out.println("Python summarizer exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.out.println("Error generating summary with AI: " + e.getMessage());
        }
    }
    
}