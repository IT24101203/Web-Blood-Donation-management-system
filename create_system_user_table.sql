-- Create SystemUser table for staff authentication
USE BloodDonationSystem;

-- Check if table exists, if not create it
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='SystemUser' AND xtype='U')
BEGIN
    CREATE TABLE SystemUser (
        UserID VARCHAR(20) PRIMARY KEY,
        Username VARCHAR(100) NOT NULL,
        Password VARCHAR(255) NOT NULL,
        Role VARCHAR(50) NOT NULL,
        Email VARCHAR(100),
        FullName VARCHAR(100) NOT NULL,
        Department VARCHAR(100),
        IsActive BIT DEFAULT 1,
        CreatedAt DATETIME DEFAULT GETDATE(),
        LastLogin DATETIME,
        CreatedBy VARCHAR(20)
    );
    
    -- Insert sample admin user
    INSERT INTO SystemUser (UserID, Username, Password, Role, Email, FullName, Department, IsActive, CreatedBy)
    VALUES ('ADM1', 'admin', 'admin123', 'Admin', 'admin@bloodlink.org', 'System Administrator', 'IT', 1, 'SYSTEM');
    
    -- Insert sample customer support
    INSERT INTO SystemUser (UserID, Username, Password, Role, Email, FullName, Department, IsActive, CreatedBy)
    VALUES ('HOS1', 'support_staff', 'hospw123', 'Customer Support', 'support@hospital.org', 'Customer Support Staff', 'Support', 1, 'ADM1');
    
    -- Insert sample customer support
    INSERT INTO SystemUser (UserID, Username, Password, Role, Email, FullName, Department, IsActive, CreatedBy)
    VALUES ('SUP1', 'support', 'support123', 'Customer Support', 'support@bloodlink.org', 'Customer Support Rep', 'Support', 1, 'ADM1');
    
    -- Insert sample doctor
    INSERT INTO SystemUser (UserID, Username, Password, Role, Email, FullName, Department, IsActive, CreatedBy)
    VALUES ('DOC1', 'doctor', 'doctor123', 'Doctor', 'doctor@bloodlink.org', 'Dr. Medical Professional', 'Medical', 1, 'ADM1');
    
    PRINT 'SystemUser table created successfully with sample data!';
END
ELSE
BEGIN
    PRINT 'SystemUser table already exists.';
END

-- Show table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'SystemUser'
ORDER BY ORDINAL_POSITION;

-- Show sample data
SELECT * FROM SystemUser;

