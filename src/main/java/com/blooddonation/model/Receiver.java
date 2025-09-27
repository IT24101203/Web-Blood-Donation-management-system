package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Receiver")
public class Receiver {
    @Id
    @NotBlank(message = "CNIC is required")
    @Column(name = "CNIC")
    private String nic;

    @NotBlank(message = "Full name is required")
    @Column(name = "Full_Name")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Column(name = "DOB")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Column(name = "Gender")
    private String gender;

    @NotBlank(message = "Contact number is required")
    @Column(name = "Contact_Number")
    private String contactNumber;

    @Email(message = "Email should be valid")
    @Column(name = "Email")
    private String email;

    @Column(name = "Address")
    private String address;

    @NotBlank(message = "Blood type needed is required")
    @Column(name = "Blood_Type_Needed")
    private String bloodTypeNeeded;

    @Column(name = "Hospital_Preference")
    private String hospitalPreference;

    @Column(name = "Medical_Condition")
    private String medicalCondition;

    @Column(name = "Registration_Date")
    private LocalDate registrationDate;

    @NotBlank(message = "Password is required")
    @Column(name = "Password")
    private String password;

    // Constructors
    public Receiver() {}

    public Receiver(String nic, String fullName, LocalDate dateOfBirth, String gender,
                   String contactNumber, String email, String address, String bloodTypeNeeded,
                   String hospitalPreference, String medicalCondition, String password) {
        this.nic = nic;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.bloodTypeNeeded = bloodTypeNeeded;
        this.hospitalPreference = hospitalPreference;
        this.medicalCondition = medicalCondition;
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

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBloodTypeNeeded() { return bloodTypeNeeded; }
    public void setBloodTypeNeeded(String bloodTypeNeeded) { this.bloodTypeNeeded = bloodTypeNeeded; }

    public String getHospitalPreference() { return hospitalPreference; }
    public void setHospitalPreference(String hospitalPreference) { this.hospitalPreference = hospitalPreference; }

    public String getMedicalCondition() { return medicalCondition; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Receiver{" +
                "nic='" + nic + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", bloodTypeNeeded='" + bloodTypeNeeded + '\'' +
                ", hospitalPreference='" + hospitalPreference + '\'' +
                ", medicalCondition='" + medicalCondition + '\'' +
                ", registrationDate=" + registrationDate +
                ", password='[PROTECTED]'" +
                '}';
    }
}
