package com.project.bookapp.dto.user;

import com.project.bookapp.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords do not match")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
     private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must contain at least one uppercase letter,"
                    + " one lowercase letter, and one digit,"
                    + " with a minimum length of 8 characters.")
     private String password;
    @NotBlank
     private String repeatPassword;
    @NotBlank
    @Length(min = 2, message = "Please enter the correct name")
     private String firstName;
    @NotBlank
    @Length(min = 2, message = "Please enter the correct last name")
     private String lastName;
    private String shippingAddress;
}
