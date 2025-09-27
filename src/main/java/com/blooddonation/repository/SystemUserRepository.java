package com.blooddonation.repository;

import com.blooddonation.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, String> {
    
    /**
     * Find system user by UserID and Password
     * @param userId the user ID
     * @param password the password
     * @return Optional SystemUser if found
     */
    @Query("SELECT u FROM SystemUser u WHERE u.userId = :userId AND u.password = :password AND u.isActive = 'Active'")
    Optional<SystemUser> findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    
    /**
     * Find system user by UserID only (any status)
     * @param userId the user ID
     * @return Optional SystemUser if found
     */
    @Query("SELECT u FROM SystemUser u WHERE u.userId = :userId")
    Optional<SystemUser> findByUserIdAndIsActiveTrue(@Param("userId") String userId);
    
    /**
     * Find system user by UserID only (active users only)
     * @param userId the user ID
     * @return Optional SystemUser if found and active
     */
    @Query("SELECT u FROM SystemUser u WHERE u.userId = :userId AND u.isActive = 'Active'")
    Optional<SystemUser> findByUserIdAndActiveTrue(@Param("userId") String userId);
    
    /**
     * Check if user exists by UserID
     * @param userId the user ID
     * @return true if user exists and is active
     */
    @Query("SELECT COUNT(u) > 0 FROM SystemUser u WHERE u.userId = :userId AND u.isActive = 'Active'")
    boolean existsByUserIdAndIsActiveTrue(@Param("userId") String userId);
}
