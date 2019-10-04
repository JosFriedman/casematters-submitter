-- run in MS SQL CASEMATTERS_INTERNET_INTAKE* db

IF EXISTS (SELECT * FROM sys.triggers WHERE object_id = OBJECT_ID(N'[dbo].[formSubmissionTrigger]'))
BEGIN
    DROP TRIGGER dbo.formSubmissionTrigger;
END
GO


CREATE TRIGGER [dbo].[formSubmissionTrigger]
   ON  [dbo].[Submissions]
   AFTER UPDATE
AS
BEGIN
    IF @@ROWCOUNT = 0
        return

    SET NOCOUNT ON

    -- only call the WebIntake_sp procs if there are no attachments; if attachments, then the attachment trigger will call them
	declare @submissionID int;
    declare @attachmentCount int;
   
    select @submissionID = submissionID from inserted;
   	select @attachmentCount = count(submissionID) from SubmissionAttachments where submissionID = @submissionID;
   
	if (@attachmentCount = 0)
	begin
	    EXEC CSC_CaseMatters_App_Dev.dbo.WebIntake_sp;
	    EXEC OATH_CaseMatters_App_Dev.dbo.WebIntake_sp;
	    EXEC OCB_CaseMatters_App_Dev.dbo.WebIntake_sp;
	end
END



