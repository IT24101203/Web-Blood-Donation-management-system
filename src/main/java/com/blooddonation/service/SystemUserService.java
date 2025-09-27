package com.blooddonation.service;

import com.blooddonation.model.SystemUser;
import java.util.List;
import java.util.Optional;

public interface SystemUserService {
    
    /**
     * Authenticate user by UserID and Password
     * @param userId the user ID
     * @param password the password
     * @return Optional SystemUser if authentication successful
     */
    Optional<SystemUser> authenticateUser(String userId, String password);
    
    /**
     * Find user by UserID
     * @param userId the user ID
     * @return Optional SystemUser if found
     */
    Optional<SystemUser> findByUserId(String userId);
    
    /**
     * Update last login time for user
     * @param userId the user ID
     */
    void updateLastLogin(String userId);
    
    /**
     * Check if user exists and is active
     * @param userId the user ID
     * @return true if user exists and is active
     */
    boolean userExists(String userId);
    
    /**
     * Get all system users
     * @return List of all system users
     */
    List<SystemUser> getAllUsers();
    
    /**
     * Save a system user
     * @param user the system user to save
     * @return the saved system user
     */
    SystemUser saveUser(SystemUser user);
    
    /**
     * Delete a system user by user ID
     * @param userId the user ID to delete
     */
    void deleteUser(String userId);
}

