-- Create Doctor_Note table for Blood Donation System
-- This table stores doctor notes and observations for donors

CREATE TABLE Doctor_Note (
    NoteID BIGINT IDENTITY(1,1) PRIMARY KEY,
    DonorNIC VARCHAR(20) NOT NULL,
    Outcome VARCHAR(50) NOT NULL,
    ReasonForDeferral VARCHAR(500),
    RecoveryAdvice VARCHAR(500),
    FollowUpRequired BIT DEFAULT 0,
    FollowUpDate DATETIME,
    AdditionalNotes VARCHAR(1000),
    CreatedAt DATETIME DEFAULT GETDATE(),
    CreatedBy VARCHAR(100),
    
    -- Foreign key constraint to link with Donor table
    FOREIGN KEY (DonorNIC) REFERENCES Donor(NIC)
);

-- Create indexes for better performance
CREATE INDEX IX_Doctor_Note_DonorNIC ON Doctor_Note(DonorNIC);
CREATE INDEX IX_Doctor_Note_Outcome ON Doctor_Note(Outcome);
CREATE INDEX IX_Doctor_Note_CreatedAt ON Doctor_Note(CreatedAt);
CREATE INDEX IX_Doctor_Note_FollowUpRequired ON Doctor_Note(FollowUpRequired);
CREATE INDEX IX_Doctor_Note_CreatedBy ON Doctor_Note(CreatedBy);

-- Insert some sample data for testing
INSERT INTO Doctor_Note (DonorNIC, Outcome, ReasonForDeferral, RecoveryAdvice, FollowUpRequired, FollowUpDate, AdditionalNotes, CreatedBy)
VALUES 
('123456789V', 'Successful', NULL, 'Continue regular exercise and maintain healthy diet', 0, NULL, 'Donor was in excellent health condition', 'Dr. Smith'),
('987654321V', 'Deferred', 'Low hemoglobin level', 'Increase iron-rich foods and retest in 3 months', 1, DATEADD(MONTH, 3, GETDATE()), 'Hemoglobin level was 11.5 g/dL, below minimum requirement', 'Dr. Johnson'),
('456789123V', 'Successful', NULL, 'Maintain current lifestyle', 0, NULL, 'All health parameters within normal range', 'Dr. Brown');
