package ma.dnaengineering.backend;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadEmployeesFromCSV() {
        String csvData = "id,employeeName,jobTitle,salary\n1,amine,elkaaba,50000.0\n2,zouhair,mohamed,60000.0";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());

        when(employeeRepository.saveAll(anyList())).thenReturn(null);

        List<Employee> expectedEmployees = Arrays.asList(
                new Employee(1, "amine", "elkaaba", 50000.0),
                new Employee(2, "zouhair", "mohamed", 60000.0)
        );

        List<Employee> actualEmployees = employeeService.readEmployeesFromCSV(inputStream);

        assertEquals(expectedEmployees.size(), actualEmployees.size());
    }



    @Test
    public void testCalculateAverageSalaryByJobTitle() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "amine", "Developer", 50000.0),
                new Employee(2, "nada", "Manager", 60000.0),
                new Employee(3, "zouhair", "Developer", 55000.0),
                new Employee(4, "fatima", "Manager", 62000.0)
        );

        Map<String, Double> averageSalaryByJobTitle = employeeService.calculateAverageSalaryByJobTitle(employees);

        assertEquals(52500.0, averageSalaryByJobTitle.get("Developer"));
        assertEquals(61000.0, averageSalaryByJobTitle.get("Manager"));
    }


    @Test
    public void testUploadEmployeesWithEmptyFile() {
        // Create an empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        List<Employee> result = employeeService.readEmployeesFromCSV(emptyInputStream);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCalculateAverageSalaryWithNoEmployees() {
        // Create an empty employee list
        List<Employee> emptyEmployeeList = Collections.emptyList();
        Map<String, Double> averageSalaryByJobTitle = employeeService.calculateAverageSalaryByJobTitle(emptyEmployeeList);

        assertEquals(0, averageSalaryByJobTitle.size());
    }

}
