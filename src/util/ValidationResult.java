package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a validation operation.
 */
public class ValidationResult {
    
    private boolean valid;
    private final List<String> errors;
    
    public ValidationResult() {
        this.valid = true;
        this.errors = new ArrayList<>();
    }
    
    /**
     * Adds an error and marks result as invalid.
     */
    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }
    
    /**
     * Adds an error for a specific field.
     */
    public void addError(String field, String message) {
        this.valid = false;
        this.errors.add(field + ": " + message);
    }
    
    /**
     * Returns whether validation passed.
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Returns all validation errors.
     */
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    /**
     * Returns first error or null if none.
     */
    public String getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }
    
    /**
     * Returns all errors as a single string.
     */
    public String getErrorsAsString() {
        return String.join("\n", errors);
    }
    
    /**
     * Returns errors as HTML for display.
     */
    public String getErrorsAsHtml() {
        if (errors.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<html><ul>");
        for (String error : errors) {
            sb.append("<li>").append(error).append("</li>");
        }
        sb.append("</ul></html>");
        return sb.toString();
    }
    
    /**
     * Merges another validation result into this one.
     */
    public void merge(ValidationResult other) {
        if (other != null && !other.isValid()) {
            this.valid = false;
            this.errors.addAll(other.getErrors());
        }
    }
    
    /**
     * Returns error count.
     */
    public int getErrorCount() {
        return errors.size();
    }
    
    /**
     * Creates a valid result.
     */
    public static ValidationResult valid() {
        return new ValidationResult();
    }
    
    /**
     * Creates an invalid result with single error.
     */
    public static ValidationResult invalid(String error) {
        ValidationResult result = new ValidationResult();
        result.addError(error);
        return result;
    }
    
    /**
     * Creates an invalid result with field error.
     */
    public static ValidationResult invalid(String field, String message) {
        ValidationResult result = new ValidationResult();
        result.addError(field, message);
        return result;
    }
}
