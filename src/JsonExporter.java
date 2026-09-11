import java.io.*;
import java.util.List;

public class JsonExporter {

    // Convert employee list to JSON string manually
    public String toJsonString(List<Employee> employees) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            json.append("  {\n");
            json.append(String.format("    \"employeeId\": \"%s\",\n", emp.getEmployeeId()));
            json.append(String.format("    \"name\": \"%s\",\n", escapeJson(emp.getName())));
            json.append(String.format("    \"department\": \"%s\",\n", emp.getDepartment()));
            json.append(String.format("    \"designation\": \"%s\",\n", escapeJson(emp.getDesignation())));
            json.append(String.format("    \"salary\": %d,\n", (long) emp.getSalary()));
            json.append(String.format("    \"yearsOfExperience\": %d,\n", emp.getYearsOfExperience()));
            json.append(String.format("    \"isActive\": %s\n", emp.isActive()));
            json.append("  }");

            if (i < employees.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("]\n");
        return json.toString();
    }

    // Export to JSON file
    public void exportToJsonFile(List<Employee> employees, String filePath) throws IOException {
        createDirectoryIfNotExists(filePath);
        
        String jsonContent = toJsonString(employees);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonContent);
            System.out.println("Employees exported to JSON: " + filePath);
        }
    }

    // Escape special characters for JSON
    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    // Create directory if it doesn't exist
    private void createDirectoryIfNotExists(String filePath) {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
    }
}
