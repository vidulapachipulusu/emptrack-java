import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonImporter {

    // Read JSON file and return as string
    public String readJsonFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    // Parse JSON string and reconstruct employee list
    public List<Employee> parseJsonString(String jsonContent) {
        List<Employee> employees = new ArrayList<>();

        // Remove brackets and split by object
        String cleaned = jsonContent.trim();
        if (cleaned.startsWith("[")) {
            cleaned = cleaned.substring(1);
        }
        if (cleaned.endsWith("]")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }

        // Split by closing and opening braces to get individual objects
        String[] objects = cleaned.split("\\}\\s*,\\s*\\{");

        for (String obj : objects) {
            obj = obj.trim();
            if (obj.startsWith("{")) {
                obj = obj.substring(1);
            }
            if (obj.endsWith("}")) {
                obj = obj.substring(0, obj.length() - 1);
            }

            try {
                Employee emp = parseEmployeeObject(obj);
                if (emp != null) {
                    employees.add(emp);
                }
            } catch (Exception e) {
                System.err.println("Error parsing employee object: " + e.getMessage());
            }
        }

        return employees;
    }

    // Parse individual employee object
    private Employee parseEmployeeObject(String objStr) {
        String employeeId = extractJsonValue(objStr, "employeeId");
        String name = extractJsonValue(objStr, "name");
        String department = extractJsonValue(objStr, "department");
        String designation = extractJsonValue(objStr, "designation");
        String salaryStr = extractJsonValue(objStr, "salary");
        String yearsStr = extractJsonValue(objStr, "yearsOfExperience");
        String isActiveStr = extractJsonValue(objStr, "isActive");

        if (employeeId == null || name == null) {
            return null;
        }

        double salary = salaryStr != null ? Double.parseDouble(salaryStr) : 0;
        int years = yearsStr != null ? Integer.parseInt(yearsStr) : 0;
        boolean isActive = isActiveStr != null && Boolean.parseBoolean(isActiveStr);

        return new Employee(employeeId, name, department, designation, salary, years, isActive);
    }

    // Extract JSON value by key
    private String extractJsonValue(String obj, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*";
        int startIdx = obj.indexOf(key);

        if (startIdx == -1) {
            return null;
        }

        int colonIdx = obj.indexOf(":", startIdx);
        if (colonIdx == -1) {
            return null;
        }

        int valueStart = colonIdx + 1;
        while (valueStart < obj.length() && (obj.charAt(valueStart) == ' ' || obj.charAt(valueStart) == '\t')) {
            valueStart++;
        }

        if (valueStart >= obj.length()) {
            return null;
        }

        char ch = obj.charAt(valueStart);
        String value;

        if (ch == '\"') {
            // String value
            int endIdx = valueStart + 1;
            while (endIdx < obj.length() && obj.charAt(endIdx) != '\"') {
                if (obj.charAt(endIdx) == '\\') {
                    endIdx++; // Skip escaped character
                }
                endIdx++;
            }
            value = obj.substring(valueStart + 1, endIdx);
            value = unescapeJson(value);
        } else {
            // Number or boolean value
            int endIdx = valueStart;
            while (endIdx < obj.length() && obj.charAt(endIdx) != ',' && obj.charAt(endIdx) != '}') {
                endIdx++;
            }
            value = obj.substring(valueStart, endIdx).trim();
        }

        return value;
    }

    // Unescape JSON strings
    private String unescapeJson(String input) {
        return input.replace("\\\"", "\"")
                   .replace("\\\\", "\\")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }

    // Import from JSON file
    public List<Employee> importFromJsonFile(String filePath) throws IOException {
        String jsonContent = readJsonFile(filePath);
        List<Employee> employees = parseJsonString(jsonContent);
        System.out.println("Employees imported from JSON: " + filePath);
        return employees;
    }
}
