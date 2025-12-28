package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Input validation utility class.
 */
public class InputValidator {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(AppConstants.EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(AppConstants.PHONE_REGEX);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
    
    private InputValidator() {} // Prevent instantiation
    
    // ===================== String Validators =====================
    
    /**
     * Checks if string is null or empty.
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Checks if string is not null and not empty.
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
    
    /**
     * Validates required field.
     */
    public static ValidationResult validateRequired(String value, String fieldName) {
        if (isEmpty(value)) {
            return ValidationResult.invalid(fieldName, AppConstants.MSG_REQUIRED_FIELD);
        }
        return ValidationResult.valid();
    }
    
    /**
     * Validates string length.
     */
    public static ValidationResult validateLength(String value, String fieldName, int min, int max) {
        if (isEmpty(value)) {
            return ValidationResult.valid(); // Let required validator handle this
        }
        
        int length = value.trim().length();
        if (length < min) {
            return ValidationResult.invalid(fieldName, "Must be at least " + min + " characters.");
        }
        if (length > max) {
            return ValidationResult.invalid(fieldName, "Must not exceed " + max + " characters.");
        }
        return ValidationResult.valid();
    }
    
    // ===================== Name Validators =====================
    
    /**
     * Validates a name field.
     */
    public static ValidationResult validateName(String name, String fieldName) {
        ValidationResult result = new ValidationResult();
        
        result.merge(validateRequired(name, fieldName));
        if (!result.isValid()) return result;
        
        result.merge(validateLength(name, fieldName, 
            AppConstants.MIN_NAME_LENGTH, AppConstants.MAX_NAME_LENGTH));
        
        // Check for invalid characters
        if (!name.matches("^[a-zA-ZığüşöçİĞÜŞÖÇ\\s'-]+$")) {
            result.addError(fieldName, "Contains invalid characters.");
        }
        
        return result;
    }
    
    // ===================== Email Validators =====================
    
    /**
     * Checks if email format is valid.
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates email field.
     */
    public static ValidationResult validateEmail(String email, String fieldName) {
        ValidationResult result = validateRequired(email, fieldName);
        if (!result.isValid()) return result;
        
        if (!isValidEmail(email)) {
            return ValidationResult.invalid(fieldName, AppConstants.MSG_INVALID_EMAIL);
        }
        return ValidationResult.valid();
    }
    
    // ===================== Phone Validators =====================
    
    /**
     * Checks if phone format is valid.
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validates phone field.
     */
    public static ValidationResult validatePhone(String phone, String fieldName) {
        if (isEmpty(phone)) {
            return ValidationResult.valid(); // Phone might be optional
        }
        
        if (!isValidPhone(phone)) {
            return ValidationResult.invalid(fieldName, AppConstants.MSG_INVALID_PHONE);
        }
        return ValidationResult.valid();
    }
    
    // ===================== Date Validators =====================
    
    /**
     * Checks if date format is valid.
     */
    public static boolean isValidDate(String date) {
        if (isEmpty(date)) return false;
        try {
            LocalDate.parse(date.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Validates date field.
     */
    public static ValidationResult validateDate(String date, String fieldName) {
        if (isEmpty(date)) {
            return ValidationResult.valid(); // Let required validator handle this
        }
        
        if (!isValidDate(date)) {
            return ValidationResult.invalid(fieldName, AppConstants.MSG_INVALID_DATE);
        }
        return ValidationResult.valid();
    }
    
    /**
     * Validates date is in the past.
     */
    public static ValidationResult validatePastDate(String date, String fieldName) {
        ValidationResult result = validateDate(date, fieldName);
        if (!result.isValid()) return result;
        
        LocalDate parsedDate = LocalDate.parse(date.trim(), DATE_FORMATTER);
        if (parsedDate.isAfter(LocalDate.now())) {
            return ValidationResult.invalid(fieldName, "Date must be in the past.");
        }
        return ValidationResult.valid();
    }
    
    /**
     * Validates date is in the future.
     */
    public static ValidationResult validateFutureDate(String date, String fieldName) {
        ValidationResult result = validateDate(date, fieldName);
        if (!result.isValid()) return result;
        
        LocalDate parsedDate = LocalDate.parse(date.trim(), DATE_FORMATTER);
        if (parsedDate.isBefore(LocalDate.now())) {
            return ValidationResult.invalid(fieldName, "Date must be in the future.");
        }
        return ValidationResult.valid();
    }
    
    // ===================== Number Validators =====================
    
    /**
     * Checks if string is a valid integer.
     */
    public static boolean isValidInteger(String value) {
        if (isEmpty(value)) return false;
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates integer field within range.
     */
    public static ValidationResult validateIntegerRange(String value, String fieldName, int min, int max) {
        if (isEmpty(value)) {
            return ValidationResult.valid();
        }
        
        if (!isValidInteger(value)) {
            return ValidationResult.invalid(fieldName, "Must be a valid number.");
        }
        
        int number = Integer.parseInt(value.trim());
        if (number < min || number > max) {
            return ValidationResult.invalid(fieldName, "Must be between " + min + " and " + max + ".");
        }
        return ValidationResult.valid();
    }
    
    /**
     * Validates positive number.
     */
    public static ValidationResult validatePositiveNumber(String value, String fieldName) {
        if (isEmpty(value)) {
            return ValidationResult.valid();
        }
        
        try {
            double number = Double.parseDouble(value.trim());
            if (number <= 0) {
                return ValidationResult.invalid(fieldName, "Must be a positive number.");
            }
        } catch (NumberFormatException e) {
            return ValidationResult.invalid(fieldName, "Must be a valid number.");
        }
        return ValidationResult.valid();
    }
    
    // ===================== Entity Validators =====================
    
    /**
     * Validates student input fields.
     */
    public static ValidationResult validateStudent(String firstName, String lastName, 
                                                   String email, String phone, String dateOfBirth) {
        ValidationResult result = new ValidationResult();
        
        result.merge(validateName(firstName, "First Name"));
        result.merge(validateName(lastName, "Last Name"));
        result.merge(validateEmail(email, "Email"));
        result.merge(validatePhone(phone, "Phone"));
        
        if (isNotEmpty(dateOfBirth)) {
            result.merge(validatePastDate(dateOfBirth, "Date of Birth"));
        }
        
        return result;
    }
    
    /**
     * Validates instructor input fields.
     */
    public static ValidationResult validateInstructor(String firstName, String lastName, 
                                                      String email, String phone, String specialization) {
        ValidationResult result = new ValidationResult();
        
        result.merge(validateName(firstName, "First Name"));
        result.merge(validateName(lastName, "Last Name"));
        result.merge(validateEmail(email, "Email"));
        result.merge(validatePhone(phone, "Phone"));
        result.merge(validateRequired(specialization, "Specialization"));
        
        return result;
    }
    
    /**
     * Validates course input fields.
     */
    public static ValidationResult validateCourse(String name, String fee, 
                                                  String startDate, String endDate) {
        ValidationResult result = new ValidationResult();
        
        result.merge(validateRequired(name, "Course Name"));
        result.merge(validateLength(name, "Course Name", 3, 100));
        result.merge(validatePositiveNumber(fee, "Fee"));
        result.merge(validateDate(startDate, "Start Date"));
        result.merge(validateDate(endDate, "End Date"));
        
        // Validate end date is after start date
        if (isValidDate(startDate) && isValidDate(endDate)) {
            LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);
            if (end.isBefore(start)) {
                result.addError("End Date", "Must be after start date.");
            }
        }
        
        return result;
    }
}
