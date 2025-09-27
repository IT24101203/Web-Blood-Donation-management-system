-- Create HealthAssessment table if it doesn't exist
USE BloodDonationSystem;

-- Check if table exists, if not create it
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='HealthAssessment' AND xtype='U')
BEGIN
    CREATE TABLE HealthAssessment (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        DonorNIC VARCHAR(20) NOT NULL,
        Weight DECIMAL(5,2),
        Height DECIMAL(5,2),
        BloodPressure VARCHAR(20),
        HeartRate INT,
        Temperature DECIMAL(4,2),
        HemoglobinLevel DECIMAL(4,2),
        HasRecentIllness BIT,
        RecentMedications VARCHAR(255),
        HasTattoo BIT,
        HasPiercing BIT,
        HasTraveledRecently BIT,
        TravelDetails VARCHAR(255),
        HasSexualContact BIT,
        HasDrugUse BIT,
        HasPregnancy BIT,
        PregnancyDetails VARCHAR(255),
        AssessmentDate DATETIME DEFAULT GETDATE(),
        AssessmentStatus VARCHAR(50) DEFAULT 'PENDING',
        Notes VARCHAR(MAX),
        FOREIGN KEY (DonorNIC) REFERENCES Donor(NIC)
    );
    
    PRINT 'HealthAssessment table created successfully!';
END
ELSE
BEGIN
    PRINT 'HealthAssessment table already exists.';
END

-- Show table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'HealthAssessment'
ORDER BY ORDINAL_POSITION;