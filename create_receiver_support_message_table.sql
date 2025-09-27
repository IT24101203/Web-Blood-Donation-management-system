-- Create ReceiverSupport_Message table for customer support messages
USE BloodDonationSystem;

-- Check if table exists, if not create it
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ReceiverSupport_Message' AND xtype='U')
BEGIN
    CREATE TABLE ReceiverSupport_Message (
        Message_ID INT IDENTITY(1,1) PRIMARY KEY,      
        Receiver_CNIC VARCHAR(12) NOT NULL,                 -- FK from Receiver table
        Receiver_Full_Name VARCHAR(100) NOT NULL,          -- Redundant, but useful for display
        Receiver_Email VARCHAR(100) NOT NULL,              -- Receiver's email
        Subject NVARCHAR(200) NOT NULL,                 
        Message_Text NVARCHAR(MAX) NOT NULL,            
        Priority VARCHAR(10) NOT NULL 
            CHECK (Priority IN ('High', 'Medium', 'Low')),
        Reply_Text NVARCHAR(MAX) NULL,                  
        Message_Date DATETIME DEFAULT GETDATE(),        
        Reply_Date DATETIME NULL,                       

        CONSTRAINT FK_ReceiverSupport_Receiver FOREIGN KEY (Receiver_CNIC) 
            REFERENCES Receiver(CNIC)                       -- FK to Receiver table
            ON DELETE CASCADE  
    );
    
    PRINT 'ReceiverSupport_Message table created successfully!';
END
ELSE
BEGIN
    PRINT 'ReceiverSupport_Message table already exists.';
END

-- Verify table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'ReceiverSupport_Message'
ORDER BY ORDINAL_POSITION;

-- Check if there are any existing messages
SELECT COUNT(*) as TotalMessages FROM ReceiverSupport_Message;

PRINT 'Table verification complete.';
