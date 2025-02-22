package Service;

import com.example.employee_tracking_system.Entity.Project;
import com.example.employee_tracking_system.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Project entity.
 */
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Retrieves all projects.
     * @return List of projects.
     */
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    /**
     * Retrieves a project by ID.
     * @param id Project ID.
     * @return Optional containing the project if found, or empty if not found.
     */
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Saves a project.
     * @param project Project to save.
     * @return Saved project.
     */
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Deletes a project by ID.
     * @param id Project ID.
     */
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }
}
