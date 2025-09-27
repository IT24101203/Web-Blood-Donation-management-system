package com.blooddonation.service;

import com.blooddonation.model.SystemUser;
import com.blooddonation.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SystemUserServiceImpl implements SystemUserService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemUser> authenticateUser(String userId, String password) {
        try {
            System.out.println("=== SystemUser Authentication Debug ===");
            System.out.println("Attempting to authenticate UserID: " + userId);
            System.out.println("Password: " + password);
            
            // Try to find user by ID first (any status)
            Optional<SystemUser> userById = systemUserRepository.findByUserIdAndIsActiveTrue(userId);
            
            if (userById.isPresent()) {
                SystemUser foundUser = userById.get();
                System.out.println("✅ User found by ID: " + foundUser.getUsername());
                System.out.println("📧 Email: " + foundUser.getEmail());
                System.out.println("👤 Full Name: " + foundUser.getFullName());
                System.out.println("🔑 Stored password: '" + foundUser.getPassword() + "'");
                System.out.println("🔑 Input password: '" + password + "'");
                System.out.println("🎭 Role: '" + foundUser.getRole() + "'");
                System.out.println("✅ Status: " + foundUser.getIsActive());
                
                // Check password match
                boolean passwordMatch = foundUser.getPassword() != null && foundUser.getPassword().equals(password);
                System.out.println("🔐 Passwords match: " + passwordMatch);
                
                // Check if user is active
                boolean isActive = "Active".equals(foundUser.getIsActive());
                System.out.println("🟢 User is active: " + isActive);
                
                if (passwordMatch && isActive) {
                    System.out.println("🎉 Authentication successful!");
                    return Optional.of(foundUser);
                } else if (!isActive) {
                    System.out.println("❌ User account is inactive!");
                    return Optional.empty();
                } else {
                    System.out.println("❌ Password mismatch!");
                    return Optional.empty();
                }
            } else {
                System.out.println("❌ No user found with UserID: " + userId);
                
                // Let's try to find any user with this ID (even inactive) for debugging
                try {
                    Optional<SystemUser> anyUser = systemUserRepository.findById(userId);
                    if (anyUser.isPresent()) {
                        SystemUser user = anyUser.get();
                        System.out.println("🔍 Found user but inactive: " + user.getUsername() + " (Active: " + user.getIsActive() + ")");
                    }
                } catch (Exception e) {
                    System.out.println("🔍 Could not check for inactive user: " + e.getMessage());
                }
                
                return Optional.empty();
            }
        } catch (Exception e) {
            System.err.println("❌ Error authenticating user: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<SystemUser> findByUserId(String userId) {
        try {
            return systemUserRepository.findByUserIdAndIsActiveTrue(userId);
        } catch (Exception e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void updateLastLogin(String userId) {
        // Last login functionality not implemented in current database schema
        // This method is kept for interface compatibility
    }

    @Override
    public boolean userExists(String userId) {
        try {
            return systemUserRepository.existsByUserIdAndIsActiveTrue(userId);
        } catch (Exception e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<SystemUser> getAllUsers() {
        try {
            return systemUserRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    @Transactional
    public SystemUser saveUser(SystemUser user) {
        try {
            return systemUserRepository.save(user);
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw new RuntimeException("Failed to save user: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        try {
            systemUserRepository.deleteById(userId);
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }
}
