-- Create DonorSupport_Message table for customer support messages
USE BloodDonationSystem;

-- Check if table exists, if not create it
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DonorSupport_Message' AND xtype='U')
BEGIN
    CREATE TABLE DonorSupport_Message (
        Message_ID INT IDENTITY(1,1) PRIMARY KEY,      
        Donor_NIC VARCHAR(12) NOT NULL,                 -- FK from Donor table
        Donor_Full_Name VARCHAR(100) NOT NULL,          -- Redundant, but useful for display
        Donor_Email VARCHAR(100) NOT NULL,              -- Donor's email
        Subject NVARCHAR(200) NOT NULL,                 
        Message_Text NVARCHAR(MAX) NOT NULL,            
        Priority VARCHAR(10) NOT NULL 
            CHECK (Priority IN ('High', 'Medium', 'Low')),
        Reply_Text NVARCHAR(MAX) NULL,                  
        Message_Date DATETIME DEFAULT GETDATE(),        
        Reply_Date DATETIME NULL,                       

        CONSTRAINT FK_CustomerSupport_Donor FOREIGN KEY (Donor_NIC) 
            REFERENCES Donor(NIC)                       -- FK to Donor table
            ON DELETE CASCADE  
    );
    
    PRINT 'DonorSupport_Message table created successfully!';
END
ELSE
BEGIN
    PRINT 'DonorSupport_Message table already exists.';
END

-- Verify table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'DonorSupport_Message'
ORDER BY ORDINAL_POSITION;

-- Check if there are any existing messages
SELECT COUNT(*) as TotalMessages FROM DonorSupport_Message;

PRINT 'Table verification complete.';

