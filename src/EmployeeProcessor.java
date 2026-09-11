import java.util.*;
import java.util.stream.Collectors;

public class EmployeeProcessor {
    private List<Employee> employees;

    public EmployeeProcessor(List<Employee> employees) {
        this.employees = employees;
    }

    // Get only active employees using Stream API
    public List<Employee> getActiveEmployees() {
        return employees.stream()
                .filter(Employee::isActive)
                .collect(Collectors.toList());
    }

    // Filter employees by department using Stream API
    public List<Employee> getByDepartment(String dept) {
        return employees.stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(dept))
                .collect(Collectors.toList());
    }

    // Get average salary as OptionalDouble
    public OptionalDouble getAverageSalary() {
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .average();
    }

    // Get employee with highest salary
    public Optional<Employee> getHighestPaid() {
        return employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

    // Get total salary bill for a department
    public double getSalaryBill(String dept) {
        return employees.stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(dept))
                .mapToDouble(Employee::getSalary)
                .sum();
    }

    // Group employees by department
    public Map<String, List<Employee>> groupByDepartment() {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    // Get employees eligible for promotion (>3 years experience and salary < 60000)
    public List<Employee> getEligibleForPromotion() {
        return employees.stream()
                .filter(e -> e.getYearsOfExperience() > 3)
                .filter(e -> e.getSalary() < 60000)
                .sorted(Comparator.comparingInt(Employee::getYearsOfExperience).reversed())
                .collect(Collectors.toList());
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employees;
    }
}
