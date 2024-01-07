package ma.dnaengineering.backend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> readEmployeesFromCSV(InputStream inputStream) {
        List<Employee> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] rowData = rows.get(i);
                int id = Integer.parseInt(rowData[0]);
                String employeeName = rowData[1];
                String jobTitle = rowData[2];
                double salary = Double.parseDouble(rowData[3]);
                employees.add(new Employee(id, employeeName, jobTitle, salary));
            }

            employeeRepository.saveAll(employees);
        } catch (IOException | CsvException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Map<String, Double> calculateAverageSalaryByJobTitle(List<Employee> employees) {
        Map<String, List<Double>> salaryByJobTitle = new HashMap<>();
        for (Employee employee : employees) {
            String jobTitle = employee.getJobTitle();
            double salary = employee.getSalary();
            salaryByJobTitle.computeIfAbsent(jobTitle, k -> new ArrayList<>()).add(salary);
        }
        Map<String, Double> averageSalaryByJobTitle = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : salaryByJobTitle.entrySet()) {
            String jobTitle = entry.getKey();
            List<Double> salaries = entry.getValue();
            double averageSalary = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageSalaryByJobTitle.put(jobTitle, averageSalary);
        }
        return averageSalaryByJobTitle;
    }

}