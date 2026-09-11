import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ReportWriter {

    // Write department report
    public void writeDepartmentReport(String dept, List<Employee> employees, String filePath) throws IOException {
        createDirectoryIfNotExists(filePath);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            LocalDate today = LocalDate.now();
            int headcount = employees.size();
            double totalBill = employees.stream().mapToDouble(Employee::getSalary).sum();
            double avgSalary = headcount > 0 ? totalBill / headcount : 0;

            writer.write("============================================\n");
            writer.write(String.format(" DEPARTMENT REPORT: %s\n", dept));
            writer.write(String.format(" Generated: %s\n", today));
            writer.write("============================================\n");
            writer.write(" ID       NAME              DESIGNATION       SALARY\n");
            writer.write("--------------------------------------------\n");

            for (Employee emp : employees) {
                writer.write(String.format(" %-8s %-17s %-18s %,d\n",
                        emp.getEmployeeId(),
                        emp.getName(),
                        emp.getDesignation(),
                        (long) emp.getSalary()));
            }

            writer.write("--------------------------------------------\n");
            writer.write(String.format(" Headcount : %d\n", headcount));
            writer.write(String.format(" Total Bill : %,d\n", (long) totalBill));
            writer.write(String.format(" Avg Salary : %,.0f\n", avgSalary));
            writer.write("============================================\n");
        }
    }

    // Write full report grouped by department
    public void writeFullReport(List<Employee> employees, String filePath) throws IOException {
        createDirectoryIfNotExists(filePath);
        
        Map<String, List<Employee>> deptMap = employees.stream()
                .collect(java.util.stream.Collectors.groupingBy(Employee::getDepartment));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            LocalDate today = LocalDate.now();

            writer.write("==========================================\n");
            writer.write(" FULL EMPLOYEE REPORT\n");
            writer.write(String.format(" Generated: %s\n", today));
            writer.write("==========================================\n\n");

            for (String dept : deptMap.keySet()) {
                List<Employee> deptEmployees = deptMap.get(dept);
                double totalBill = deptEmployees.stream().mapToDouble(Employee::getSalary).sum();
                double avgSalary = deptEmployees.size() > 0 ? totalBill / deptEmployees.size() : 0;

                writer.write("\n" + dept + " DEPARTMENT:\n");
                writer.write("--------------------------------------------\n");
                writer.write(" ID       NAME              DESIGNATION       SALARY\n");
                writer.write("--------------------------------------------\n");

                for (Employee emp : deptEmployees) {
                    writer.write(String.format(" %-8s %-17s %-18s %,d\n",
                            emp.getEmployeeId(),
                            emp.getName(),
                            emp.getDesignation(),
                            (long) emp.getSalary()));
                }

                writer.write(String.format(" Headcount: %d | Total Bill: %,d | Avg: %,.0f\n",
                        deptEmployees.size(), (long) totalBill, avgSalary));
                writer.write("--------------------------------------------\n");
            }
        }
    }

    // Write summary report
    public void writeSummaryReport(EmployeeProcessor processor, String filePath) throws IOException {
        createDirectoryIfNotExists(filePath);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            LocalDate today = LocalDate.now();
            int totalHeadcount = processor.getAllEmployees().size();
            OptionalDouble avgSalary = processor.getAverageSalary();
            Optional<Employee> highestPaid = processor.getHighestPaid();
            Map<String, List<Employee>> deptMap = processor.groupByDepartment();

            writer.write("==========================================\n");
            writer.write(" SUMMARY REPORT\n");
            writer.write(String.format(" Generated: %s\n", today));
            writer.write("==========================================\n\n");

            writer.write(String.format(" Total Headcount: %d\n", totalHeadcount));
            
            if (avgSalary.isPresent()) {
                writer.write(String.format(" Average Salary: %,.2f\n", avgSalary.getAsDouble()));
            }

            if (highestPaid.isPresent()) {
                Employee emp = highestPaid.get();
                writer.write(String.format(" Highest Paid: %s (%s) - %,.0f\n", 
                        emp.getName(), emp.getEmployeeId(), emp.getSalary()));
            }

            writer.write("\n Department-wise Salary Bill:\n");
            writer.write("--------------------------------------------\n");
            for (String dept : deptMap.keySet()) {
                double bill = processor.getSalaryBill(dept);
                int count = deptMap.get(dept).size();
                writer.write(String.format(" %-20s: %,d (Headcount: %d)\n", dept, (long) bill, count));
            }
            writer.write("==========================================\n");
        }
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
