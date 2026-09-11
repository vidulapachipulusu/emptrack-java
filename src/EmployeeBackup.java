import java.io.*;
import java.util.List;

public class EmployeeBackup {

    // Serialize employee list to binary file
    public void serializeEmployees(List<Employee> employees, String filePath) throws IOException {
        createDirectoryIfNotExists(filePath);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(employees);
            System.out.println("Employees serialized successfully to: " + filePath);
        }
    }

    // Deserialize employee list from binary file
    @SuppressWarnings("unchecked")
    public List<Employee> deserializeEmployees(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Employee> employees = (List<Employee>) ois.readObject();
            System.out.println("Employees deserialized successfully from: " + filePath);
            return employees;
        }
    }

    // Verify restored list matches original
    public boolean verifyBackup(List<Employee> original, List<Employee> restored) {
        if (original.size() != restored.size()) {
            System.out.println("Size mismatch: Original=" + original.size() + ", Restored=" + restored.size());
            return false;
        }

        for (int i = 0; i < original.size(); i++) {
            Employee origEmp = original.get(i);
            Employee restEmp = restored.get(i);

            if (!origEmp.getEmployeeId().equals(restEmp.getEmployeeId()) ||
                !origEmp.getName().equals(restEmp.getName()) ||
                !origEmp.getDepartment().equals(restEmp.getDepartment()) ||
                origEmp.getSalary() != restEmp.getSalary() ||
                origEmp.isActive() != restEmp.isActive()) {
                System.out.println("Data mismatch at index " + i);
                return false;
            }

            // Verify transient field is null after deserialization
            if (restEmp.getAuthToken() != null) {
                System.out.println("Warning: authToken should be null after deserialization, but found: " + restEmp.getAuthToken());
                return false;
            }
        }

        System.out.println("Backup verification successful! All " + original.size() + " records match.");
        return true;
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
