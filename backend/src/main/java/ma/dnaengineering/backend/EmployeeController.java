package ma.dnaengineering.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/uploadEmployees")
    public List<Employee> uploadEmployees(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return employeeService.readEmployeesFromCSV(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @PostMapping("/average-salaries")
    public Map<String, Double> getAverageSalaries(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            List<Employee> employees = employeeService.readEmployeesFromCSV(file.getInputStream());
            return employeeService.calculateAverageSalaryByJobTitle(employees);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}
