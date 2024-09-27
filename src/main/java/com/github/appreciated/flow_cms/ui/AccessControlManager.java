package com.github.appreciated.flow_cms.ui;

import com.github.appreciated.flow_cms.service.ViewConfigParser;
import com.vaadin.flow.server.VaadinSession;
import java.util.List;

public class AccessControlManager {

    private ViewConfigParser configParser;

    public AccessControlManager(ViewConfigParser configParser) {
        this.configParser = configParser;
    }

    public boolean hasAccess(String viewName, String role) {
        List<String> roles = configParser.getRolesForView(viewName);
        return roles.contains(role);
    }

    public boolean hasFieldAccess(String viewName, String fieldName, String role) {
        // Implementieren der Logik für Felder mit Zugangskontrolle (z.B. status in der task_view)
        return true;
    }

    public String getCurrentUserRole() {
        // Beispiel: Rollendaten aus der aktuellen Session abrufen
        return VaadinSession.getCurrent().getAttribute("role").toString();
    }
}
