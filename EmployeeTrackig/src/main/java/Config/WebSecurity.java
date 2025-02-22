package Config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {

    public boolean checkEmployeeId(Authentication authentication, Long id) {
        // Implement logic to check if the authenticated user can access the given employee ID
        return true; // Placeholder, implement actual logic
    }
}
