import java.io.*;

public class ReportReader {

    // Read and print report file
    public void readAndPrintReport(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("\n--- Report Contents ---\n");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("\n--- End of Report ---\n");
        } catch (FileNotFoundException e) {
            System.err.println("Report file not found: " + filePath);
            throw e;
        }
    }
}
