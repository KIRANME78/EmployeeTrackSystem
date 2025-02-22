package Contoroller;

import com.example.employee_tracking_system.Config.WebSecurity;
import com.example.employee_tracking_system.Entity.Employee;
import com.example.employee_tracking_system.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final WebSecurity webSecurity; // Add WebSecurity field

    @Autowired
    public EmployeeController(EmployeeService employeeService, WebSecurity webSecurity) {
        this.employeeService = employeeService;
        this.webSecurity = webSecurity; // Initialize WebSecurity
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id, Authentication authentication) {
        // Check access using WebSecurity
        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) ||
                webSecurity.checkEmployeeId(authentication, id)) {
            Employee employee = employeeService.findById(id);
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.status(403).build(); // Forbidden
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.save(employee);
        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee updatedEmployee = employeeService.update(id, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
