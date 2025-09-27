package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Donor")
public class Donor {
    @Id
    @NotBlank(message = "NIC is required")
    @Column(name = "NIC")
    private String nic;

    @NotBlank(message = "Full name is required")
    @Column(name = "Full_Name")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Column(name = "Date_Of_Birth")
    private LocalDate dateOfBirth;

    @Column(name = "Age")
    private Integer age;

    @NotBlank(message = "Gender is required")
    @Column(name = "Gender")
    private String gender;

    @NotBlank(message = "Blood type is required")
    @Column(name = "Blood_Type")
    private String bloodType;

    @NotBlank(message = "Contact number is required")
    @Column(name = "Contact_Number")
    private String contactNumber;

    @Email(message = "Email should be valid")
    @Column(name = "Email")
    private String email;

    @NotBlank(message = "Address is required")
    @Column(name = "Address")
    private String address;

    @NotBlank(message = "Password is required")
    @Column(name = "Password")
    private String password;

    @Transient
    private String healthStatus = "PENDING";

    @Column(name = "Created_At", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Donor() {}

    public Donor(String nic, String fullName, LocalDate dateOfBirth, Integer age, String gender,
                 String bloodType, String contactNumber, String email,
                 String address, String password) {
        this.nic = nic;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    // Getters and Setters
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}