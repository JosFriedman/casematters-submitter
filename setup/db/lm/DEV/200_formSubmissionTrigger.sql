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
    
    EXEC CSC_CaseMatters_App_Dev.dbo.WebIntake_sp;
    EXEC OATH_CaseMatters_App_Dev.dbo.WebIntake_sp;
    EXEC OCB_CaseMatters_App_Dev.dbo.WebIntake_sp;
   
END


