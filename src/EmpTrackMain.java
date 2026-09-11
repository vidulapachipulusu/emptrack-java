import java.io.*;
import java.util.*;

public class EmpTrackMain {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("       EMPTRACK - Employee Report System");
        System.out.println("===========================================");

        // Create sample employees
        List<Employee> employees = createSampleEmployees();

        // Display all employees
        System.out.println("\n[*] Sample Employees Created:");
        employees.forEach(emp -> System.out.println("    " + emp.getEmployeeId() + " | " + emp.getName() + " | " + emp.getDepartment()));

        // TASK 1: Stream-Based Processing
        performStreamTasks(employees);

        // TASK 2: File Handling and Report Writing
        performReportTasks(employees);

        // TASK 3: Serialization and JSON
        performSerializationTasks(employees);

        System.out.println("\n==========================================");
        System.out.println("       All Tasks Completed Successfully!");
        System.out.println("==========================================");
    }

    private static List<Employee> createSampleEmployees() {
        List<Employee> employees = new ArrayList<>();

        // Engineering Department
        employees.add(new Employee("EMP-001", "Rahul Sharma", "Engineering", "Senior Developer", 85000, 6, true));
        employees.add(new Employee("EMP-002", "Ananya Iyer", "Engineering", "Junior Developer", 42000, 2, true));
        employees.add(new Employee("EMP-003", "Vikram Patel", "Engineering", "DevOps Engineer", 78000, 5, true));
        employees.add(new Employee("EMP-004", "Priya Singh", "Engineering", "QA Engineer", 55000, 4, false));

        // HR Department
        employees.add(new Employee("EMP-005", "Neha Gupta", "HR", "HR Manager", 65000, 7, true));
        employees.add(new Employee("EMP-006", "Arjun Reddy", "HR", "Recruiter", 48000, 3, true));
        employees.add(new Employee("EMP-007", "Divya Kumar", "HR", "HR Executive", 38000, 1, true));

        // Finance Department
        employees.add(new Employee("EMP-008", "Amit Verma", "Finance", "Finance Manager", 72000, 8, true));
        employees.add(new Employee("EMP-009", "Sneha Choudhary", "Finance", "Accountant", 45000, 4, true));
        employees.add(new Employee("EMP-010", "Rohan Mishra", "Finance", "Junior Accountant", 35000, 2, false));
        employees.add(new Employee("EMP-011", "Kavya Desai", "Engineering", "Tech Lead", 95000, 9, true));

        return employees;
    }

    private static void performStreamTasks(List<Employee> employees) {
        System.out.println("\n========== TASK 1: Stream-Based Processing ==========");
        EmployeeProcessor processor = new EmployeeProcessor(employees);

        // Get active employees
        System.out.println("\n[1] Active Employees:");
        processor.getActiveEmployees().forEach(e -> System.out.println("    " + e.getEmployeeId() + " - " + e.getName()));

        // Get by department
        System.out.println("\n[2] Engineering Department Employees:");
        processor.getByDepartment("Engineering")
                .forEach(e -> System.out.println("    " + e.getName() + " (" + e.getDesignation() + ")"));

        // Get average salary
        System.out.println("\n[3] Average Salary:");
        OptionalDouble avgSal = processor.getAverageSalary();
        if (avgSal.isPresent()) {
            System.out.println("    " + String.format("%.2f", avgSal.getAsDouble()));
        }

        // Get highest paid
        System.out.println("\n[4] Highest Paid Employee:");
        Optional<Employee> highest = processor.getHighestPaid();
        if (highest.isPresent()) {
            Employee emp = highest.get();
            System.out.println("    " + emp.getName() + " - " + emp.getSalary());
        }

        // Get salary bill by department
        System.out.println("\n[5] Department Salary Bills:");
        processor.groupByDepartment().keySet().forEach(dept -> {
            double bill = processor.getSalaryBill(dept);
            System.out.println("    " + dept + ": " + String.format("%.0f", bill));
        });

        // Get eligible for promotion
        System.out.println("\n[6] Employees Eligible for Promotion (>3 yrs exp, <60k salary):");
        processor.getEligibleForPromotion().forEach(e -> 
            System.out.println("    " + e.getName() + " (" + e.getYearsOfExperience() + " years, " + e.getSalary() + ")"));

        // Answer questions about Stream API
        System.out.println("\n[?] Stream API Questions:");
        System.out.println("    Q1: OptionalDouble vs Optional<Double>");
        System.out.println("        - OptionalDouble is a specialized container for primitive double values");
        System.out.println("        - Avoids autoboxing overhead (Double object wrapping)");
        System.out.println("        - mapToDouble().average() returns OptionalDouble for performance");
        System.out.println("    Q2: Order of filter() and sorted() affects performance");
        System.out.println("        - Filter BEFORE sort: processes fewer elements (better performance)");
        System.out.println("        - Sorted stream has same result but filters after sorting waste CPU");
    }

    private static void performReportTasks(List<Employee> employees) {
        System.out.println("\n========== TASK 2: File Handling & Reports ==========");
        ReportWriter writer = new ReportWriter();
        ReportReader reader = new ReportReader();

        try {
            String deptReportPath = "reports/engineering_report.txt";
            String fullReportPath = "reports/full_report.txt";
            String summaryReportPath = "reports/summary_report.txt";

            EmployeeProcessor processor = new EmployeeProcessor(employees);
            List<Employee> engEmployees = processor.getByDepartment("Engineering");

            // Write department report
            System.out.println("\n[1] Writing Department Report...");
            writer.writeDepartmentReport("Engineering", engEmployees, deptReportPath);
            System.out.println("    ✓ Written to: " + deptReportPath);

            // Write full report
            System.out.println("\n[2] Writing Full Report...");
            writer.writeFullReport(employees, fullReportPath);
            System.out.println("    ✓ Written to: " + fullReportPath);

            // Write summary report
            System.out.println("\n[3] Writing Summary Report...");
            writer.writeSummaryReport(processor, summaryReportPath);
            System.out.println("    ✓ Written to: " + summaryReportPath);

            // Read and display department report
            System.out.println("\n[4] Reading Department Report:");
            reader.readAndPrintReport(deptReportPath);

            // Answer questions about BufferedWriter
            System.out.println("\n[?] File Handling Questions:");
            System.out.println("    Q1: Why is BufferedWriter needed?");
            System.out.println("        - Buffering reduces I/O system calls from ~500 to ~1-2 for 500 records");
            System.out.println("        - Direct FileWriter makes individual disk writes (slow)");
            System.out.println("        - BufferedWriter accumulates writes in memory, flushes in batches");
            System.out.println("    Q2: File overwrite vs append behavior");
            System.out.println("        - new FileWriter(path) = OVERWRITE existing content");
            System.out.println("        - new FileWriter(path, true) = APPEND to existing content");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void performSerializationTasks(List<Employee> employees) {
        System.out.println("\n========== TASK 3: Serialization & JSON ==========");
        EmployeeBackup backup = new EmployeeBackup();
        JsonExporter exporter = new JsonExporter();
        JsonImporter importer = new JsonImporter();

        try {
            String backupPath = "backups/employees.ser";
            String jsonPath = "backups/employees.json";

            // Serialize to binary
            System.out.println("\n[1] Serializing to Binary...");
            backup.serializeEmployees(employees, backupPath);

            // Deserialize from binary
            System.out.println("\n[2] Deserializing from Binary...");
            List<Employee> restoredEmployees = backup.deserializeEmployees(backupPath);

            // Verify backup
            System.out.println("\n[3] Verifying Backup...");
            backup.verifyBackup(employees, restoredEmployees);

            // Check transient field
            System.out.println("\n[4] Checking Transient Fields...");
            Employee testEmp = employees.get(0);
            testEmp.setAuthToken("session-token-xyz");
            System.out.println("    Before serialization - authToken: " + testEmp.getAuthToken());
            
            List<Employee> temp = new ArrayList<>();
            temp.add(testEmp);
            backup.serializeEmployees(temp, "backups/test_transient.ser");
            List<Employee> restored = backup.deserializeEmployees("backups/test_transient.ser");
            System.out.println("    After deserialization - authToken: " + restored.get(0).getAuthToken());
            System.out.println("    ✓ Transient field correctly set to null");

            // Export to JSON
            System.out.println("\n[5] Exporting to JSON...");
            exporter.exportToJsonFile(employees, jsonPath);

            // Import from JSON
            System.out.println("\n[6] Importing from JSON...");
            List<Employee> importedEmployees = importer.importFromJsonFile(jsonPath);
            System.out.println("    ✓ Imported " + importedEmployees.size() + " employees");

            // Answer questions about serialization
            System.out.println("\n[?] Serialization Questions:");
            System.out.println("    Q1: transient int retryCount = 3 after deserialization?");
            System.out.println("        - Answer: 0 (default value for int)");
            System.out.println("        - Transient fields are NOT serialized, so deserialized value is default");
            System.out.println("        - Initial assignment (=3) is ignored during deserialization");
            System.out.println("    Q2: Adding new field after serialization?");
            System.out.println("        - Throws InvalidClassException if serialVersionUID unchanged");
            System.out.println("        - Solution: increment serialVersionUID to signal schema change");
            System.out.println("    Q3: Manual JSON parsing vs Libraries");
            System.out.println("        - Manual parsing breaks if names contain commas or special chars");
            System.out.println("        - Jackson/Gson handle escaping, quotes, unicode automatically");
            System.out.println("        - Rule: Use libraries for production, manual parsing only for learning");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
