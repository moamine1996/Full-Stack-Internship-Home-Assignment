package ma.dnaengineering.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadEmployeesWithValidFile() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "employees.csv", "text/csv", "1,amine,elkaaba,50000.0\n2,zouhair,mohamed,60000.0".getBytes());

        when(employeeService.readEmployeesFromCSV(any())).thenReturn(Arrays.asList(
                new Employee(1, "amine", "elkaaba", 50000.0),
                new Employee(2, "zouhair", "mohamed", 60000.0)
        ));

        List<Employee> result = employeeController.uploadEmployees(mockFile);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAverageSalariesWithValidFile() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "employees.csv", "text/csv", "1,amine,elkaaba,50000.0\n2,zouhair,mohamed,60000.0".getBytes());

        when(employeeService.calculateAverageSalaryByJobTitle(any())).thenReturn(
                Map.of("Engineer", 55000.0, "Manager", 60000.0)
        );

        Map<String, Double> result = employeeController.getAverageSalaries(mockFile);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}




