# EmpTrack - Employee Report Generation System

A console-based employee data processing and report generation tool built with Java, demonstrating Stream API, file handling, and serialization.

## Project Overview

EmpTrack processes employee data from multiple departments, generates formatted reports, handles file I/O, and manages data backup/restore using Java's core APIs.

## Features

### Task 1: Stream-Based Employee Processing
- **EmployeeProcessor** uses Stream API exclusively (no loops)
  - Filter active employees
  - Group by department
  - Calculate average salary (OptionalDouble)
  - Find highest paid employee
  - Department salary bills
  - Promotion eligibility (>3 years experience, <60k salary)

### Task 2: File Handling & Report Writing
- **ReportWriter** generates formatted reports
  - Department-specific reports
  - Full company report (grouped by department)
  - Summary report with statistics
- **ReportReader** displays reports to console
- Automatic directory creation
- IOException handling

### Task 3: Serialization & JSON
- **EmployeeBackup** for binary serialization
  - Serialize/deserialize employee lists
  - Backup verification
  - Transient field handling (authToken)
- **JsonExporter/JsonImporter** for JSON format
  - Manual JSON parsing (no external libraries)
  - Proper escaping of special characters
  - File I/O with buffering

## Project Structure

```
emptrack-java/
├── src/
│   ├── Employee.java              # Employee model with serialization
│   ├── EmployeeProcessor.java     # Stream API operations
│   ├── ReportWriter.java          # Report generation
│   ├── ReportReader.java          # Report reading
│   ├── EmployeeBackup.java        # Binary serialization
│   ├── JsonExporter.java          # JSON export
│   ├── JsonImporter.java          # JSON import
│   └── EmpTrackMain.java          # Main application
├── reports/                       # Generated report files
├── backups/                       # Serialized data files
└── README.md
```

## Key Concepts Covered

### Stream API
- Filter, map, collect operations
- Comparators for sorting
- Collectors (groupingBy, toList)
- OptionalDouble vs Optional<Double>
- Stream performance (filter before sort)

### File I/O
- BufferedWriter/BufferedReader efficiency
- FileWriter modes (overwrite vs append)
- Directory creation
- IOException handling

### Serialization
- Implementing Serializable interface
- serialVersionUID management
- Transient fields
- ObjectInputStream/ObjectOutputStream
- Deserialization verification

### JSON Handling
- Manual parsing without libraries
- String escaping and unescaping
- Custom JSON formatting
- StringBuilder for performance

## Compilation & Execution

```bash
# Compile all files
javac src/*.java

# Run the application
java -cp src EmpTrackMain
```

## Sample Employee Data

The application creates 11 employees across 3 departments:
- **Engineering (5)**: Developers, QA, DevOps
- **HR (3)**: Manager, Recruiter, Executive
- **Finance (3)**: Manager, Accountants

Mix of active/inactive employees with varying salaries and experience levels.

## Output Files

### Reports (text format)
- `reports/engineering_report.txt` - Department report
- `reports/full_report.txt` - All departments grouped
- `reports/summary_report.txt` - Summary statistics

### Backups (binary & JSON)
- `backups/employees.ser` - Binary serialized data
- `backups/employees.json` - JSON format

## Learning Outcomes

✓ Master Java Streams with no imperative loops
✓ Understand OptionalDouble vs Optional<Double>
✓ Implement buffered I/O for performance
✓ Manage serialization with transient fields
✓ Parse JSON manually with proper escaping
✓ Answer real-world interview questions about each concept

## Interview Questions Answered

1. **Why OptionalDouble instead of Optional<Double>?**
   - Avoids autoboxing overhead for primitive values
   - Performance optimization for numeric streams

2. **Does filter/sort order affect results and performance?**
   - Results: Same (order doesn't change output)
   - Performance: Filter FIRST (reduces sort dataset)

3. **Why BufferWriter instead of FileWriter directly?**
   - Reduces disk I/O from ~500 calls to ~1-2 for 500 records
   - Batches writes in memory before flushing

4. **What happens with transient fields after deserialization?**
   - Set to default values (null for objects, 0 for primitives)
   - Initial assignments ignored

5. **Why use libraries for JSON vs manual parsing?**
   - Libraries handle escaping, unicode, edge cases automatically
   - Manual parsing fails with special characters in data
   - Rule: Manual only for learning, libraries for production

## Author

Built as an educational project demonstrating core Java concepts.
