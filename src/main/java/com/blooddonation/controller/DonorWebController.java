package com.blooddonation.controller;

import com.blooddonation.dto.DonorRegistrationRequest;
import com.blooddonation.model.Donor;
import com.blooddonation.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class DonorWebController {

    private final DonorService donorService;

    @Autowired
    public DonorWebController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping("/donor/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("donor", new DonorRegistrationRequest());
        return "Donorregister";
    }

    @PostMapping("/donor/register")
    public String submitRegisterForm(@Valid @ModelAttribute("donor") DonorRegistrationRequest request,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return "Donorregister";
        }

        try {
            Donor donor = donorService.registerDonor(request);
            model.addAttribute("successMessage", "Registration successful! Your donor ID: " + donor.getNic() + ". You can now login with your NIC and password.");
            return "redirect:/?success=true";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "Donorregister";
        }
    }

    @PostMapping("/donor/login")
    public String loginDonor(@RequestParam String nic, 
                           @RequestParam String password,
                           HttpSession session, 
                           Model model) {
        try {
            Donor donor = donorService.authenticateDonor(nic, password);
            session.setAttribute("donor", donor);
            return "redirect:/dashboard";
        } catch (Exception ex) {
            model.addAttribute("loginError", "Invalid NIC or password. Please try again.");
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/donor/logout")
    public String logoutDonor(HttpSession session) {
        session.removeAttribute("donor");
        return "redirect:/";
    }

    @GetMapping("/donor/edit-profile")
    public String showEditProfileForm(HttpSession session, Model model) {
        Donor donor = (Donor) session.getAttribute("donor");
        if (donor == null) {
            return "redirect:/";
        }
        
        // Create a DTO for editing (excluding password)
        DonorRegistrationRequest editRequest = new DonorRegistrationRequest();
        editRequest.setNic(donor.getNic());
        editRequest.setFullName(donor.getFullName());
        editRequest.setDateOfBirth(donor.getDateOfBirth());
        editRequest.setGender(donor.getGender());
        editRequest.setBloodType(donor.getBloodType());
        editRequest.setContactNumber(donor.getContactNumber());
        editRequest.setEmail(donor.getEmail());
        editRequest.setAddress(donor.getAddress());
        editRequest.setAge(donor.getAge());
        
        model.addAttribute("donor", editRequest);
        model.addAttribute("isEdit", true);
        return "EditDonorProfile";
    }

    @PostMapping("/donor/update-profile")
    public String updateProfile(@Valid @ModelAttribute("donor") DonorRegistrationRequest request,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {
        Donor currentDonor = (Donor) session.getAttribute("donor");
        if (currentDonor == null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "EditDonorProfile";
        }

        try {
            // Update the donor with new information
            currentDonor.setFullName(request.getFullName());
            currentDonor.setDateOfBirth(request.getDateOfBirth());
            currentDonor.setGender(request.getGender());
            currentDonor.setBloodType(request.getBloodType());
            currentDonor.setContactNumber(request.getContactNumber());
            currentDonor.setEmail(request.getEmail());
            currentDonor.setAddress(request.getAddress());
            
            // Update age based on date of birth
            if (request.getDateOfBirth() != null) {
                currentDonor.setAge(java.time.LocalDate.now().getYear() - request.getDateOfBirth().getYear());
            }

            // Save the updated donor
            Donor updatedDonor = donorService.updateDonor(currentDonor);
            
            // Update session with the updated donor
            session.setAttribute("donor", updatedDonor);
            
            return "redirect:/dashboard?success=Profile updated successfully!";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("isEdit", true);
            return "EditDonorProfile";
        }
    }
}


