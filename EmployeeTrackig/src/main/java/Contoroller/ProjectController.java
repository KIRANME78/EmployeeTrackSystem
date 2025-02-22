package Contoroller;

import com.example.employee_tracking_system.Entity.Project;
import com.example.employee_tracking_system.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id, Authentication authentication) {
        if (hasAccess(authentication, id)) {
            Optional<Project> project = projectService.findById(id);
            return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(403).build(); // Forbidden
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.save(project);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Optional<Project> optionalProject = projectService.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            project.setDepartment(projectDetails.getDepartment());
            projectService.save(project);
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean hasAccess(Authentication authentication, Long id) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                || customProjectAccessCheck(authentication, id);
    }

    private boolean customProjectAccessCheck(Authentication authentication, Long id) {
        // Implement custom logic to check if the authenticated user can access the given project ID
        return true; // Placeholder, implement actual logic
    }
}
