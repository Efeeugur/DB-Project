package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the result of a validation operation.
 */
public class ValidationResult {
    
    private final Map<String, List<String>> errors;
    
    public ValidationResult() {
        this.errors = new HashMap<>();
    }
    
    public void addError(String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
    
    public boolean isValid() {
        return errors.isEmpty();
    }
    
    public Map<String, List<String>> getErrors() {
        return errors;
    }
    
    public String getFirstError() {
        if (errors.isEmpty()) return null;
        Map.Entry<String, List<String>> first = errors.entrySet().iterator().next();
        return first.getKey() + ": " + first.getValue().get(0);
    }
    
    public String getAllErrors() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
            for (String error : entry.getValue()) {
                sb.append("• ").append(entry.getKey()).append(": ").append(error).append("\n");
            }
        }
        return sb.toString().trim();
    }
    
    public void merge(ValidationResult other) {
        for (Map.Entry<String, List<String>> entry : other.errors.entrySet()) {
            for (String error : entry.getValue()) {
                addError(entry.getKey(), error);
            }
        }
    }
    
    public static ValidationResult valid() {
        return new ValidationResult();
    }
    
    public static ValidationResult invalid(String field, String message) {
        ValidationResult result = new ValidationResult();
        result.addError(field, message);
        return result;
    }
    
    public String getErrorsAsHtml() {
        StringBuilder sb = new StringBuilder("<html>");
        for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
            for (String error : entry.getValue()) {
                sb.append("• ").append(entry.getKey()).append(": ").append(error).append("<br>");
            }
        }
        sb.append("</html>");
        return sb.toString();
    }
}
