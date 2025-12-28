package util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InputValidator.
 */
class InputValidatorTest {
    
    // ==================== isEmpty / isNotEmpty ====================
    
    @Test
    @DisplayName("isEmpty should return true for null")
    void testIsEmptyNull() {
        assertTrue(InputValidator.isEmpty(null));
    }
    
    @Test
    @DisplayName("isEmpty should return true for empty string")
    void testIsEmptyEmptyString() {
        assertTrue(InputValidator.isEmpty(""));
    }
    
    @Test
    @DisplayName("isEmpty should return true for whitespace")
    void testIsEmptyWhitespace() {
        assertTrue(InputValidator.isEmpty("   "));
    }
    
    @Test
    @DisplayName("isEmpty should return false for valid string")
    void testIsEmptyValidString() {
        assertFalse(InputValidator.isEmpty("test"));
    }
    
    // ==================== Email Validation ====================
    
    @Test
    @DisplayName("Valid email formats should pass")
    void testValidEmails() {
        assertTrue(InputValidator.isValidEmail("test@example.com"));
        assertTrue(InputValidator.isValidEmail("user.name@domain.org"));
        assertTrue(InputValidator.isValidEmail("user+tag@email.co.uk"));
    }
    
    @Test
    @DisplayName("Invalid email formats should fail")
    void testInvalidEmails() {
        assertFalse(InputValidator.isValidEmail("invalid"));
        assertFalse(InputValidator.isValidEmail("missing@domain"));
        assertFalse(InputValidator.isValidEmail("@nodomain.com"));
        assertFalse(InputValidator.isValidEmail("spaces in@email.com"));
        assertFalse(InputValidator.isValidEmail(null));
        assertFalse(InputValidator.isValidEmail(""));
    }
    
    @Test
    @DisplayName("validateEmail should return errors for invalid email")
    void testValidateEmailInvalid() {
        var result = InputValidator.validateEmail("invalid", "Email");
        assertFalse(result.isValid());
        assertTrue(result.getFirstError().contains("Email"));
    }
    
    // ==================== Phone Validation ====================
    
    @Test
    @DisplayName("Valid phone formats should pass")
    void testValidPhones() {
        assertTrue(InputValidator.isValidPhone("1234567890"));
        assertTrue(InputValidator.isValidPhone("+90 555 123 4567"));
        assertTrue(InputValidator.isValidPhone("(555) 123-4567"));
    }
    
    @Test
    @DisplayName("Invalid phone formats should fail")
    void testInvalidPhones() {
        assertFalse(InputValidator.isValidPhone("123")); // Too short
        assertFalse(InputValidator.isValidPhone("abcdefghij")); // Letters
        assertFalse(InputValidator.isValidPhone(null));
    }
    
    // ==================== Date Validation ====================
    
    @Test
    @DisplayName("Valid date formats should pass")
    void testValidDates() {
        assertTrue(InputValidator.isValidDate("2023-01-15"));
        assertTrue(InputValidator.isValidDate("1990-12-31"));
        assertTrue(InputValidator.isValidDate("2024-02-29")); // Leap year
    }
    
    @Test
    @DisplayName("Invalid date formats should fail")
    void testInvalidDates() {
        assertFalse(InputValidator.isValidDate("15-01-2023")); // Wrong format
        assertFalse(InputValidator.isValidDate("2023/01/15")); // Wrong separator
        assertFalse(InputValidator.isValidDate("invalid"));
        assertFalse(InputValidator.isValidDate("2023-13-01")); // Invalid month
        assertFalse(InputValidator.isValidDate("2023-01-32")); // Invalid day
    }
    
    // ==================== Name Validation ====================
    
    @Test
    @DisplayName("Valid names should pass")
    void testValidNames() {
        var result = InputValidator.validateName("John", "First Name");
        assertTrue(result.isValid());
        
        result = InputValidator.validateName("Mary Jane", "First Name");
        assertTrue(result.isValid());
        
        result = InputValidator.validateName("O'Brien", "Last Name");
        assertTrue(result.isValid());
    }
    
    @Test
    @DisplayName("Invalid names should fail")
    void testInvalidNames() {
        // Too short
        var result = InputValidator.validateName("A", "First Name");
        assertFalse(result.isValid());
        
        // Empty
        result = InputValidator.validateName("", "First Name");
        assertFalse(result.isValid());
        
        // Contains numbers
        result = InputValidator.validateName("John123", "First Name");
        assertFalse(result.isValid());
    }
    
    // ==================== Length Validation ====================
    
    @Test
    @DisplayName("validateLength should pass for valid lengths")
    void testValidateLengthValid() {
        var result = InputValidator.validateLength("Hello", "Field", 2, 10);
        assertTrue(result.isValid());
    }
    
    @Test
    @DisplayName("validateLength should fail for too short")
    void testValidateLengthTooShort() {
        var result = InputValidator.validateLength("Hi", "Field", 5, 10);
        assertFalse(result.isValid());
    }
    
    @Test
    @DisplayName("validateLength should fail for too long")
    void testValidateLengthTooLong() {
        var result = InputValidator.validateLength("This is a very long string", "Field", 2, 10);
        assertFalse(result.isValid());
    }
    
    // ==================== Number Validation ====================
    
    @Test
    @DisplayName("Valid integers should pass")
    void testValidIntegers() {
        assertTrue(InputValidator.isValidInteger("123"));
        assertTrue(InputValidator.isValidInteger("-456"));
        assertTrue(InputValidator.isValidInteger("0"));
    }
    
    @Test
    @DisplayName("Invalid integers should fail")
    void testInvalidIntegers() {
        assertFalse(InputValidator.isValidInteger("12.34"));
        assertFalse(InputValidator.isValidInteger("abc"));
        assertFalse(InputValidator.isValidInteger(null));
    }
    
    @Test
    @DisplayName("validatePositiveNumber should pass for positive numbers")
    void testValidatePositiveNumberValid() {
        var result = InputValidator.validatePositiveNumber("100", "Amount");
        assertTrue(result.isValid());
        
        result = InputValidator.validatePositiveNumber("99.99", "Price");
        assertTrue(result.isValid());
    }
    
    @Test
    @DisplayName("validatePositiveNumber should fail for non-positive numbers")
    void testValidatePositiveNumberInvalid() {
        var result = InputValidator.validatePositiveNumber("0", "Amount");
        assertFalse(result.isValid());
        
        result = InputValidator.validatePositiveNumber("-10", "Amount");
        assertFalse(result.isValid());
    }
    
    // ==================== Entity Validation ====================
    
    @Test
    @DisplayName("validateStudent should pass for valid input")
    void testValidateStudentValid() {
        var result = InputValidator.validateStudent(
            "John", "Doe", "john@email.com", "1234567890", "1990-01-15"
        );
        assertTrue(result.isValid());
    }
    
    @Test
    @DisplayName("validateStudent should fail for invalid input")
    void testValidateStudentInvalid() {
        // Invalid email
        var result = InputValidator.validateStudent(
            "John", "Doe", "invalid-email", "1234567890", "1990-01-15"
        );
        assertFalse(result.isValid());
        assertTrue(result.getErrorCount() > 0);
    }
    
    @Test
    @DisplayName("validateCourse should pass for valid input")
    void testValidateCourseValid() {
        var result = InputValidator.validateCourse(
            "Painting 101", "199.99", "2024-01-01", "2024-06-30"
        );
        assertTrue(result.isValid());
    }
    
    @Test
    @DisplayName("validateCourse should fail if end date before start date")
    void testValidateCourseInvalidDates() {
        var result = InputValidator.validateCourse(
            "Painting 101", "199.99", "2024-06-30", "2024-01-01"
        );
        assertFalse(result.isValid());
        assertTrue(result.getErrorsAsString().contains("End Date"));
    }
    
    // ==================== ValidationResult ====================
    
    @Test
    @DisplayName("ValidationResult merge should combine errors")
    void testValidationResultMerge() {
        var result1 = ValidationResult.invalid("Field1", "Error 1");
        var result2 = ValidationResult.invalid("Field2", "Error 2");
        
        result1.merge(result2);
        
        assertFalse(result1.isValid());
        assertEquals(2, result1.getErrorCount());
    }
    
    @Test
    @DisplayName("ValidationResult getErrorsAsHtml should format correctly")
    void testValidationResultHtml() {
        var result = new ValidationResult();
        result.addError("Error 1");
        result.addError("Error 2");
        
        String html = result.getErrorsAsHtml();
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("<li>Error 1</li>"));
        assertTrue(html.contains("<li>Error 2</li>"));
    }
}
