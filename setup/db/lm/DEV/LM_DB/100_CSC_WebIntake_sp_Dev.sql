-- run in MS SQL CSC_CaseMatters_App_* db

--***************webintake_sp**********--

IF EXISTS (SELECT * FROM sys.procedures WHERE object_id = OBJECT_ID(N'[dbo].[WebIntake_sp]'))
BEGIN
    DROP PROCEDURE dbo.WebIntake_sp;
END
GO


CREATE PROCEDURE [dbo].[WebIntake_sp]
       
AS
--------------------------------------------------------------------------------
-- Name:    WebIntake_sp
-- Version: 005
--
-- Description: This is a program written for Civil Service Commission agency (CSC) for web intake process
--				People can submit two types of appeals on CSC web site filling out Disciplinary or Disqualification forms.
--              The data from the forms will be stored in three tables Submissions, SubmissionData and SubmissionAttachments 
--              for supporting document.
--              WebIntake_sp periodically will query for new intake submissions and transform the forms data into Law Manager
--              structure staging table - SubmissionDataLM
-- Part I -     the process will perform check for duplicate contacts calling LookupEntityIntake_sp procedure.
--              If no duplicate exists it will create new contact for Appellant and Representative (if exists)
--              calling InsertIntakeContact_sp       
-- Part II -    validates all the required  for creating new intake case data and if not valid 'errors' the submission 
--              inserting submissionID and error message in SubmissionError table OR if all information is valid, creates NEW intake 
--              and copying Filers and Representative's information in Matter Notes tab.
--              If Intake is created successfully it creates Intake folder to store documents submitted with a form
-- Part III     When documents are moved to a specified folder, the process creates links in Law Manager between new Intake 
--              and files so CSC users can open the supportive documents and review them
--
-- Note:        the PRINT statements are commented out and left in the program for testing and debuggin purpose.
--
-- Created By:   Rimma Chernyakhovskaya	
-- Date Created: 03/30/2014
--
--
-- History:     12/03/2014 by RimmaC - Version 2 modifications:
--                  1. Part III -  adding 'Subseuqent Document submissions' using submissionParentID
--                  2. Part II - creating a Note Type for invalid Penalty, Exam # and Disqualification reason
--                 Removed on 12/09 - matterEntity.note is only 255 chars, not enough to accomodate the string 3. Part II - saving Appellant and Representative notes in Players tab as well
--                  4. Part II - formatting notes text by adding more white spaces for better readability 
--              12/11/2014 by RimmaC - adding new Part IV section - Initial Submission Event and Additional Submissions Event notifications
--              01/22/2015 by RimmaC - Version: 004 corrected the logic for Part IV
--              02/06/2015 by RimmaC - revert back to Version 004 from 02/02/2015 - the enhancement interfeers with iWay rollback 
--              05/28/2015 by RimmaC - added check for NULL for @examNumber
--              06/03/2015 by RimmaC - creating Matter with status 'pending' and status flag 'Closed'
--              05/03/2017 by ASheltzer - added selection for CSC agency only
--------------------------------------------------------------------------------
SET NoCount ON

--------------------------------------------------------------------------------
-- Variable Declarations
--------------------------------------------------------------------------------

BEGIN

DECLARE
-- variables for new Intake
@newmatterkey int,
@newtablekey int,
@mattertypekey int,
@mattersubtypekey int,
@penaltykey int,
@matterstatuskey int,
@dateopened date, 
@matterName varchar(100),
@matterstageKey int,
@submissionscount int,
@submissionid int,
@submissionParentID int,
@indentitytypekey int,  -- entity type key for type 'Individual'
@repentitytypekey int, -- entity type key for type 'Attorney'
@approlekey int,
@reprolekey int,
@agencyKey int,
@repFirmKey int,
@procresult int,
@LMIntakeUserKey int,
@LMGroupKey int,
@agencyName varchar(100),

@intakerollback bit,

@NYCDocumentPath varchar(500),
@matternumber varchar(20),
@matterfolder varchar(200),

@appentitykey int,
@repentitykey int,
@matternotetypekey int,
@appIntakeNote varchar(2000),
@repIntakeNote varchar(2000),
@appIntakeNoteTypeKey int,
@repIntakeNoteTypeKey int,
@intInvalidNoteTypeKey int,
@IntakeInvDataNote varchar(200),
@IntakeOtherAgencyName varchar(200),
@NewIntakeNumber varchar(15),
@NewIntakeName varchar(255),
@OrgEntityKey int, --Rep's oraganization
@dupContactEntityKey int,
@checkExamNo int,

-- variables for to hold one submission data
@formkey varchar(128),
@form_key  varchar(128) ,
@Form  varchar(128),
@dateSubmitted datetime ,
@filerNamePrefix varchar(128),
@filerLastName varchar(128),
@filerFirstName varchar(128),
@filerMiddleName varchar(128),
@filerNameSuffix varchar(128),
@filerStreetAddress varchar(128),
@filerCity varchar(128),
@filerState varchar(128),
@filerZip varchar(128),
@filerPhone varchar(128),
@filerFax varchar(128),
@filerEmail varchar(128),
@representativeOrganization varchar(128),
@representativeNamePrefix varchar(128),
@representativeLastName varchar(128),
@representativeFirstName varchar(128),
@representativeMiddleName varchar(128),
@representativeNameSuffix varchar(128),
@representativeStreetAddress varchar(128),
@representativeCity varchar(128),
@representativeState varchar(128),
@representativeZip varchar(128),
@representativePhone varchar(128),
@representativeFax varchar(128),
@representativeEmail varchar(128),
@civilServiceTitle varchar(128),
@examNumber varchar(128), 
@examName varchar(128),
@disqualificationReason varchar(128),
@penalty varchar(128),
@agency varchar(128),
@agencyOther varchar(128),
@electronicSignature varchar(128),
@mattertypecode varchar(10),
@ErrMsg varchar(250),

-- variables for Initial Submission Events
@eventIntakeKey int,
@eventIntakeCategory int,
@eventIntakeDefaultSubject varchar(250),
@eventIntakeStatus int,
@eventIntakeDescription varchar(1000),
@formName varchar(250),
@nbrDocs int,

-- variables for documents
@documentscount int,
@docfilename varchar(200),
@docpath varchar(200),
@docmatterkey int,
@newdockey int,
@doctypekey int,
@docfullpath varchar(500),
@docTitle varchar(255),

-- for additional submissions event notification
@nbrAddSubmisions int,
@eventIntakeKey_ad int,
@eventIntakeCategory_ad  int,
@eventIntakeDefaultSubject_ad  varchar(250),
@eventIntakeStatus_ad  int,
@eventIntakeDescription_ad  varchar(1000),
@formName_ad  varchar(250),
@nbrDocs_ad  int,
@eventmatterkey int

-- the table will hold distinct submissionID for Additional Documents Event Notification
DECLARE @AdditionalSubmissions TABLE
       (submissionID  int,
         submissionParentID int,
         matter_key int,
         event_sent int )


 -- setting default variables
 SELECT @LMIntakeUserKey = personnel_key from Personnel where login_name = 'LMIntake'
 SELECT top 1 @LMGroupKey =  lmgroup_key from PersonnelLMGroup where personnel_key = @LMIntakeUserKey

-- get the new submissions int LM staging table from SubmissionData table

INSERT INTO CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM
select s.submissionID, 
Max(CASE WHEN s.FormName = 'Notice of Appeal - Disciplinary Case' THEN '76' ELSE '50' END) MatterTypeCode,
s.submissionTimestamp dateSubmitted,
Max(CASE WHEN d.fieldname = 'filerNamePrefix' THEN d.fieldValue ELSE NULL END) filerNamePrefix,
Max(CASE WHEN d.fieldname = 'filerLastName' THEN d.fieldValue ELSE NULL END) filerLastName,
Max(CASE WHEN d.fieldname = 'filerFirstName' THEN d.fieldValue ELSE NULL END) filerFirstName,
Max(CASE WHEN d.fieldname = 'filerMiddleInitial' THEN d.fieldValue ELSE NULL END) filerMiddleName,
Max(CASE WHEN d.fieldname = 'filerNameSuffix' THEN d.fieldValue ELSE NULL END) filerNameSuffix,
Max(CASE WHEN d.fieldname = 'filerStreetAddress' THEN d.fieldValue ELSE NULL END) filerStreetAddress,
Max(CASE WHEN d.fieldname = 'filerCity' THEN d.fieldValue ELSE NULL END) filerCity,
Max(CASE WHEN d.fieldname = 'filerState' THEN d.fieldValue ELSE NULL END) filerState,
Max(CASE WHEN d.fieldname = 'filerZip' THEN d.fieldValue ELSE NULL END) filerZip,
Max(CASE WHEN d.fieldname = 'filerPhone' THEN d.fieldValue ELSE NULL END) filerPhone,
Max(CASE WHEN d.fieldname = 'filerFax' THEN d.fieldValue ELSE NULL END) filerFax,
Max(CASE WHEN d.fieldname = 'filerEmail' THEN d.fieldValue ELSE NULL END) filerEmail,
Max(CASE WHEN d.fieldname = 'representativeOrganization' THEN d.fieldValue ELSE NULL END) representativeOrganization,
Max(CASE WHEN d.fieldname = 'representativeNamePrefix' THEN d.fieldValue ELSE NULL END) representativeNamePrefix,
Max(CASE WHEN d.fieldname = 'representativeLastName' THEN d.fieldValue ELSE NULL END) representativeLastName,
Max(CASE WHEN d.fieldname = 'representativeFirstName' THEN d.fieldValue ELSE NULL END) representativeFirstName,
Max(CASE WHEN d.fieldname = 'representativeMiddleInitial' THEN d.fieldValue ELSE NULL END) representativeMiddleName,
Max(CASE WHEN d.fieldname = 'representativeNameSuffix' THEN d.fieldValue ELSE NULL END) representativeNameSuffix,
Max(CASE WHEN d.fieldname = 'representativeStreetAddress' THEN d.fieldValue ELSE NULL END) representativeStreetAddress,
Max(CASE WHEN d.fieldname = 'representativeCity' THEN d.fieldValue ELSE NULL END) representativeCity,
Max(CASE WHEN d.fieldname = 'representativeState' THEN d.fieldValue ELSE NULL END) representativeState,
Max(CASE WHEN d.fieldname = 'representativeZip' THEN d.fieldValue ELSE NULL END) representativeZip,
Max(CASE WHEN d.fieldname = 'representativePhone' THEN d.fieldValue ELSE NULL END) representativePhone,
Max(CASE WHEN d.fieldname = 'representativeFax' THEN d.fieldValue ELSE NULL END) representativeFax,
Max(CASE WHEN d.fieldname = 'representativeEmail' THEN d.fieldValue ELSE NULL END) representativeEmail,
Max(CASE WHEN d.fieldname = 'civilServiceTitle' THEN d.fieldValue ELSE NULL END) civilServiceTitle,
Max(CASE WHEN d.fieldname = 'examNo' THEN d.fieldValue ELSE NULL END) examNumber,
Max(CASE WHEN d.fieldname = 'examName' THEN d.fieldValue ELSE NULL END) examName,
Max(CASE WHEN d.fieldname = 'disqualificationSubReason' THEN d.fieldValue ELSE NULL END) disqualificationReason,
Max(CASE WHEN d.fieldname = 'penalty' THEN d.fieldValue ELSE NULL END) penalty,
Max(CASE WHEN d.fieldname = 'agency' THEN d.fieldValue ELSE NULL END) agency,
Max(CASE WHEN d.fieldname = 'agencyOther' THEN d.fieldValue ELSE NULL END) agencyOther,
Max(CASE WHEN d.fieldname = 'electronicSignature' THEN d.fieldValue ELSE NULL END) electronicSignature,
NULL matter_key ,
NULL matter_number ,
NULL matter_folder,
NULL err_message ,
NULL doc_created 
  FROM CASEMATTERS_INTERNET_INTAKE.dbo.submissions s
  INNER JOIN CASEMATTERS_INTERNET_INTAKE.dbo.submissiondata d  on s.submissionID = d.submissionID
  WHERE s.agencyAbbreviation = 'CSC' and s.lawmanagerCaseID is NULL --and s.submissionID in (58054, 58057) -- for testing
   and not exists(SELECT null from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionError e where s.submissionID = e.submissionID)
   and not exists(SELECT null from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM lm where s.submissionID = lm.submissionID)
   GROUP BY s.submissionID, s.submissionTimestamp, s.agency

-- get the count of the new submissions
SELECT @submissionscount = count(*) from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM where matter_key IS NULL and err_message IS NULL
--PRINT 'Sumissions count: '+CAST(@submissionscount as varchar)
-- continue the process when there are new submissions
if @submissionscount > 0
BEGIN

-- set default variables
--SELECT @matterstatuskey = matter_status_key from MatterStatus where description = 'Active'
SELECT @matterstatuskey = matter_status_key from MatterStatus where control = 'pend'
SELECT @matterstagekey = matter_stage_key from MatterStage where description = 'Intake' and control = 'CSC'
SELECT @indentitytypekey = entity_type_key from EntityType where code = 'Individual'
SELECT @repentitytypekey = entity_type_key from EntityType where code = 'Attorney'

SELECT @eventIntakeKey = event_type_key, @eventIntakeCategory = event_category_key, @eventIntakeDefaultSubject= ISNULL(default_subject, 'New Intake Submission') from EventType where code = 'INTAKE' and control = 'CSC'
SELECT @eventIntakeStatus =  event_status_key  from EventStatus where event_category_key = @eventIntakeCategory and default_flag = 'Y'
IF @eventIntakeDefaultSubject IS NULL  SET @eventIntakeDefaultSubject = 'New Internet Intake Submission'

SELECT top 1 @dupContactEntityKey = entity_key from Entity where code = 'DDD' and person_company_flag = 'P'
IF @dupContactEntityKey IS NULL SET @dupContactEntityKey = 0
SELECT top 1 @appIntakeNoteTypeKey  = matter_note_type_key from MatterNoteType where code = 'IntakeAppellant'
IF @appIntakeNoteTypeKey IS NULL SET @appIntakeNoteTypeKey = 0
SELECT top 1 @repIntakeNoteTypeKey  = matter_note_type_key from MatterNoteType where code = 'IntakeRep'
IF @repIntakeNoteTypeKey IS NULL SET @repIntakeNoteTypeKey = 0
SELECT top 1 @intInvalidNoteTypeKey =  matter_note_type_key from MatterNoteType where code = 'IntakeInvData'
IF @intInvalidNoteTypeKey IS NULL SET @intInvalidNoteTypeKey = 0
SELECt top 1 @approlekey = matter_entity_type_key from MatterEntityType where code = 'appellant' and control = 'CSC'
IF @approlekey IS NULL SET @approlekey = 0
SELECt top 1 @reprolekey = matter_entity_type_key from MatterEntityType where code = 'appellantrep' and control = 'CSC'
IF @approlekey IS NULL SET @approlekey = 0

SELECT @NYCDocumentPath  = variable_value_store from LMVariable where variable_type_key in (Select variable_type_key from LMVariableType
where (Upper(name) like 'NYCDOCUMENTPATH%'))
--PRINT 'Documetn Path: '+ @NYCDocumentPath
-- start processing submissions
WHILE @submissionscount > 0
BEGIN

-- get the first submissionID
SELECT  top 1 @submissionid = submissionID from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM where matter_key IS NULL and err_message IS NULL
  

-- setting all local variables from one submission
SELECT @dateSubmitted =  dateSubmitted,
	@filerNamePrefix = filerNamePrefix ,
	@filerLastName = filerLastName  ,
	@filerFirstName = filerFirstName ,
	@filerMiddleName = filerMiddleName ,
	@filerNameSuffix = filerNameSuffix ,
	@filerStreetAddress = filerStreetAddress ,
	@filerCity = filerCity,
	@filerState = filerState ,
	@filerZip = filerZip ,
	@filerPhone = filerPhone ,
	@filerFax = filerFax,
	@filerEmail = filerEmail ,
	@representativeOrganization = representativeOrganization ,
	@representativeNamePrefix = representativeNamePrefix ,
	@representativeLastName = representativeLastName ,
	@representativeFirstName = representativeFirstName ,
	@representativeMiddleName = representativeMiddleName,
	@representativeNameSuffix = representativeNameSuffix ,
	@representativeStreetAddress = representativeStreetAddress,
	@representativeCity = representativeCity ,
	@representativeState = representativeState ,
	@representativeZip = representativeZip,
	@representativePhone = representativePhone,
	@representativeFax = representativeFax ,
	@representativeEmail = representativeEmail,
	@civilServiceTitle = civilServiceTitle ,
	@examNumber = examNumber , 
	@examName = examName,
	@disqualificationReason = disqualificationReason,
	@penalty = penalty,
	@agency = agency,
	@agencyOther = agencyOther ,
	@electronicSignature = electronicSignature,
	@mattertypecode = MatterTypeCode
   FROM CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM where submissionID = @submissionid
   
   -- get the form name
  -- SELECT @formName = formName from CASEMATTERS_INTERNET_INTAKE.dbo.Submissions where submissionID = @submissionid
   
   -- resetting some variables
   SET @ErrMsg = ''
   SET @intakerollback = 0
   SET @IntakeInvDataNote = ''
   SET @IntakeOtherAgencyName = ''
   SET @matternumber = NULL
   SET @matterfolder = NULL
   SET @OrgEntityKey = NULL
   SET @repIntakeNote = ''
   SET @appIntakeNote = ''
   
   
   SET @appIntakeNote = 'Filer Prefix: '+isNull(@filerNamePrefix, ' ')+ CHAR(13)+
			                       +'  Filer First Name: '+@filerFirstName+'  Filer Middle Name: '+isNull(@filerMiddleName, ' ')+ CHAR(13)+'  Filer Last Name: '+@filerLastName+CHAR(13)+'  Filer Suffix: '+isNull(@filerNameSuffix, ' ')+CHAR(13)+
			                       +'  Filer Address: '+@filerStreetAddress+' '+@filerCity+' '+@filerState+' '+@filerZip+ CHAR(13)+
			                       +'  Phone: '+isNull(@filerPhone, ' ')+'  Fax: '+isNull(@filerFax, ' ')+CHAR(13)+'  Email: '+isNull(@filerEmail, ' ')

   IF (@representativeFirstName IS NOT NULL) AND (@representativeLastName IS NOT NULL)
     BEGIN
     --Print 'Setting reps note'
     SET @repIntakeNote = 'Rep Prefix: '+isNull(@representativeNamePrefix, ' ')+ CHAR(13)+
			                       +'  Rep First Name: '+@representativeFirstName+'  Rep Middle Name: '+isNull(@representativeMiddleName, ' ')+CHAR(13)+'  Rep Last Name: '+@representativeLastName+CHAR(13)+'  Rep Suffix: '+isNull(@representativeNameSuffix, ' ')+CHAR(13)+
			                       +'  Rep Address: '+@representativeStreetAddress+' '+@representativeCity+' '+isNull(@representativeState, ' ')+' '+@representativeZip+ CHAR(13)+
			                       +'  Phone: '+isNull(@representativePhone, ' ')+'  Fax: '+isNull(@representativeFax, ' ')+CHAR(13)+'  Email: '+isNull(@representativeEmail, ' ')+ CHAR(13)+'  Rep Organization: '+isNull(@representativeOrganization, ' ')
     END
  --PRINT @appIntakeNote
  --PRINT @repIntakeNote
   -- get the Agency Key
   IF @agency IS NOT NULL
   SELECT top 1 @agencyKey =entity_key, @agencyName =  name from Entity where entity_acronym = @agency and entity_type_key = (select entity_type_key from EntityType where code = 'CityAgency')
  -- get Matter Type
   if @mattertypecode IS NOT NULL
   SELECT top 1  @mattertypekey = matter_type_key from MatterType where code  = @mattertypecode and control = 'CSC'
   -- Invalid Agency or Intake Type
   if @agencyKey IS NULL  SET @ErrMsg = 'Invalid Agency: '+isNULL(@agency, 'No agency entered')
   if @mattertypekey IS NULL SET @ErrMsg = 'Invalid Matter Type: '+@mattertypecode
   
   -- when  ERROR set the error flag and go the next submission
   IF @ErrMsg <> ''
     BEGIN		
		UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM SET err_message = @ErrMsg where submissionID = @submissionid
		UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.Submissions SET error_flag = 1 where submissionID = @submissionid
		INSERT INTO CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionError(submissionID, Err_Message, Processed_Date) Values(@submissionid, @ErrMsg, getdate())
	 END
   ELSE
	  BEGIN
     -- PART I check for duplicates and insert new contacts
     -- Filer
      EXECUTE @procresult = LookupEntityIntake_sp @filerFirstName, @filerLastName, @indentitytypekey
      
      --PRINT 'Lookup Result: '+CAST(@procresult as varchar)
      IF @procresult = 0
        BEGIN
      -- insert new Contact       
		EXECUTE InsertIntakeContact_sp
					@intNamePrefix = @filerNamePrefix,
					@intLastName = @filerLastName,
					@intFirstName = @filerFirstName,
					@intMiddleName = @filerMiddleName,
					@intNameSuffix = @filerNameSuffix,
					@intStreetAddress = @filerStreetAddress,
					@intCity = @filerCity,
					@intState = @filerState,
					@intZip = @filerZip,
					@intPhone = @filerPhone,
					@intFax = @filerFax,
					@intEmail = @filerEmail,
					@entitytypekey = @indentitytypekey,
					@UserKey = @LMIntakeUserKey,
					@GroupKey = @LMGroupKey,
					@intSubmissionDate =  @dateSubmitted,
					@RepOrgEntityKey = NULL,
					@intfiler = 'I',
					@OutNewEntityKey = @appentitykey   OUTPUT 
					
			-- couldn't create Filer		
			IF (@appentitykey IS NULL) or (@appentitykey = -1)		
			  BEGIN	
			  --  PRINT ' Appelant wasnt created'
			    SET @appentitykey = @dupContactEntityKey 
			    SELECT top 1 @matterstagekey = matter_stage_key from MatterStage where code = 'duplicate'	  	
			    SET @appIntakeNote = 'ERROR: Could not create Contact for Filer: '+ CHAR(13)+ CHAR(10)+@appIntakeNote	
			  END
          END           
          ELSE
          BEGIN
           -- PRINT 'Duplicate Appellant'
            SET @appentitykey = @dupContactEntityKey 
            SELECT top 1 @matterstagekey = matter_stage_key from MatterStage where code = 'duplicate'
            SET @appIntakeNote = 'Possible Duplicate for Filer: '+ CHAR(13)+ CHAR(10)+@appIntakeNote
          END          
       
       -- check for Rep
      -- Print 'rep First name: '+@representativeFirstName
      -- Print 'Rep Last name: '+@representativeLastName
          -- Check for Rep
          IF (@representativeFirstName IS NOT NULL) AND (@representativeLastName IS NOT NULL)
            BEGIN
                  
            EXECUTE @procresult = LookupEntityIntake_sp @representativeFirstName, @representativeLastName, @repentitytypekey
            -- inserting new rep
          --  PRINT 'Lookup for Rep: '+CAST(@procresult as varchar)
            IF @procresult = 0
              BEGIN
               -- trying to get rep's Organizaiont entity key
                IF (@representativeOrganization IS NOT NULL)
                  BEGIN
                 -- Print 'Reps Org is not null'
                    SELECT top 1 @OrgEntityKey = entity_key from Entity where name = @representativeOrganization and person_company_flag = 'C'
                   -- if @OrgEntityKey IS NULL Print 'OrgEntityKey is NULL'                    
                  END
                  
                
                  EXECUTE InsertIntakeContact_sp
					@intNamePrefix = @representativeNamePrefix,
					@intLastName = @representativeLastName,
					@intFirstName = @representativeFirstName,
					@intMiddleName = @representativeMiddleName,
					@intNameSuffix = @representativeNameSuffix,
					@intStreetAddress = @representativeStreetAddress,
					@intCity = @representativeCity,
					@intState = @representativeState,
					@intZip = @representativeZip,
					@intPhone = @representativePhone,
					@intFax = @representativeFax,
					@intEmail = @representativeEmail,
					@entitytypekey = @repentitytypekey,
					@UserKey = @LMIntakeUserKey,
					@GroupKey = @LMGroupKey,
					@intSubmissionDate =  @dateSubmitted,
					@RepOrgEntityKey = @OrgEntityKey,
					@intfiler = 'R',
					@OutNewEntityKey = @repentitykey   OUTPUT 
				-- couldn't create Filer	
			       IF (@repentitykey IS NULL) or (@repentitykey = -1)
			         BEGIN
			    -- PRINT 'Rep was not created'
			           SET @repentitykey = @dupContactEntityKey 
			           SELECT top 1 @matterstagekey = matter_stage_key from MatterStage where code = 'duplicate'
				       SET	@repIntakeNote=  'ERROR: Could not create Contact for Rep: '+@repIntakeNote
				     END	
              END --no rep exists
              ELSE
                BEGIN
                  SET @repentitykey = @dupContactEntityKey 
                  SELECT top 1 @matterstagekey = matter_stage_key from MatterStage where code = 'duplicate'
                  SET  @repIntakeNote=  'Possible Duplicate for Rep: '+ @repIntakeNote
                 END --             
             
           END -- check for Rep
         
         -- creating Intake Part II
           -- getting keys 
           IF @disqualificationReason IS NOT NULL
           BEGIN
             SELECT @mattersubtypekey = matter_subtype_key from MatterSubtype where control = 'CSC' and description = @disqualificationReason
             IF @mattersubtypekey IS NULL SET @IntakeInvDataNote = 'Invalid Disqualification Reason: '+@disqualificationReason
           END
           
           IF @penalty IS NOT NULL
           BEGIN
SELECT @penaltykey = penalty_imposed_key from PenaltyImposed where description = @penalty             
             IF @penaltykey IS NULL 
               BEGIN
                  IF @IntakeInvDataNote <> ''
                    SET @IntakeInvDataNote = @IntakeInvDataNote+'  Invalid Penalty: '+@penalty
                  ELSE
                    SET @IntakeInvDataNote = 'Invalid Penalty: '+@penalty
                END
           END
             --PRINT @IntakeInvDataNote
           IF @examNumber IS NOT NULL
           BEGIN
             -- If Exam Number is not a numeric value
             SELECT @checkExamNo = IsNumeric(@examNumber)
             --PRINT @checkExamNo      
             IF @checkExamNo = 0
             BEGIN
               IF @IntakeInvDataNote <> ''
                 SET @IntakeInvDataNote = @IntakeInvDataNote+'  Invalid Exam Number: '+@examNumber
               ELSE
                 SET @IntakeInvDataNote = 'Invalid Exam Number: '+@examNumber
             END
           END
           
           
           IF @agencyOther IS NOT NULL SET @IntakeOtherAgencyName = 'Other Agency name: '+@agencyOther
           
           SET @matterName =  @filerFirstName+' '+@filerLastName+' vs. '+@agencyName
           
           -- create intake
           BEGIN TRANSACTION tnxInsertIntake
           
           EXECUTE GetNextNNumbers 'Matter', 1, @newmatterkey OUTPUT
           INSERT INTO Matter (	date_added,	added_by,	owner_key,	group_key,	permissions,	matter_key,	matter_name,	date_opened,
	                            matter_status_key, 	matter_type_key,	matter_stage_key,	status_flag, matter_subtype_key, how_served_code) 
	                            VALUES ( Getdate(), @LMIntakeUserKey, @LMIntakeUserKey, @LMGroupKey, 511, @newmatterkey, @matterName, @dateSubmitted, 	
	                            @matterstatuskey, @mattertypekey, @matterstagekey, 'C', @mattersubtypekey, 'I')
				IF @@ERROR <> 0 
					BEGIN
					  SET @ErrMsg = 'Procedure WebIntake_sp: Insert to Matter table has failed.'
					  SET @intakerollback = 1
					  ROLLBACK TRANSACTION tnxInsertIntake
					  RETURN(-1)
				    END            
	        IF  @checkExamNo = 1
	        INSERT INTO MatterCSC (	exam_name,	exam_number,	agency_key,	matter_csc_key, position_title
                  ) VALUES (@examName, @examNumber, @agencyKey, @newmatterkey, @civilServiceTitle)
             ELSE
             INSERT INTO MatterCSC (	exam_name,		agency_key,	matter_csc_key, position_title
                  ) VALUES (@examName,  @agencyKey, @newmatterkey, @civilServiceTitle)

					IF @@ERROR <> 0 
					BEGIN
					  SET @ErrMsg = 'Procedure WebIntake_sp: Insert to MatterCSC table has failed.'
					  SET @intakerollback = 1
					  ROLLBACK TRANSACTION tnxInsertIntake
					  RETURN(-1)
					END          
            
            
            if @intakerollback = 0  -- Intake was sucessfully created
            -- insert players, notes
            BEGIN
            --PRINT @appIntakeNote
              -- inserting players
              --'Appellant'
              
              EXECUTE GetNextNNumbers 'MatterEntity', 1, @newtablekey OUTPUT
              INSERT INTO MatterEntity (date_added,	added_by,	matter_entity_key,	start_date,	end_date,	entity_key,	matter_entity_type_key,	matter_key)
                  VALUES (Getdate(), @LMIntakeUserKey, @newtablekey, getDate(), Convert(datetime, '9999-12-30'), @appentitykey, @approlekey, @newmatterkey)
					IF @@ERROR <> 0 
					BEGIN             
						SET @ErrMsg = @ErrMsg+ 'Insert Appellant player has failed: '+CAST(@appentitykey  as varchar(50))   
						PRINT @ErrMsg                      
					END
            
               --Rep
              IF (@representativeFirstName IS NOT NULL) and (@representativeLastName IS NOT NULL)
                BEGIN
                  EXECUTE GetNextNNumbers 'MatterEntity', 1, @newtablekey OUTPUT
                  INSERT INTO MatterEntity (date_added,	added_by,	matter_entity_key,	start_date,	end_date,	entity_key,	matter_entity_type_key,	matter_key)
                      VALUES (Getdate(), @LMIntakeUserKey, @newtablekey, getDate(), Convert(datetime, '9999-12-30'), @repentitykey, @reprolekey, @newmatterkey)
                      IF @@ERROR <> 0 
                        BEGIN
                          SET @ErrMsg = @ErrMsg+ ' Insert Representative player has failed: '+CAST(@repentitykey as varchar(50))   
                         -- PRINT @ErrMsg                        
                        END
                END
           
              --Penalty
              IF @penaltykey IS NOT NULL
                BEGIN
                  EXECUTE GetNextNNumbers 'MatterPenalty', 1, @newtablekey OUTPUT
                  INSERT INTO MatterPenalty (matter_penalty_key, penalty_imposed_key, matter_key,date_added,	added_by) 
                     VALUES (@newtablekey, @penaltykey,  @newmatterkey, getdate(), @LMIntakeUserKey )
                     IF @@ERROR <> 0 
                       BEGIN
                         SET @ErrMsg = @ErrMsg+ ' Penalty insert has failed: '+CAST(@penaltykey   as varchar(50))
						-- PRINT @ErrMsg                         
					   END            
                END 
            
               -- inserting matterNotes
              EXECUTE GetNextNNumbers 'MatterNote', 1, @newtablekey OUTPUT
                 INSERT INTO MatterNote (	date_added,	added_by,	owner_key,	group_key,	permissions,	matter_note_key,	matter_key,	note, matter_note_type_key,	date_of_note,	private_flag
                 ) VALUES ( getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, @newtablekey, @newmatterkey, @appIntakeNote, @appIntakeNoteTypeKey, @dateSubmitted, 'N')
                    IF @@ERROR <> 0 
                      BEGIN
                        SET @ErrMsg = @ErrMsg+ ' Appellant Matter Note insert has failed.'   
                      -- PRINT @ErrMsg                     
                      END
            
              IF @repIntakeNote <> ''
                BEGIN
                   EXECUTE GetNextNNumbers 'MatterNote', 1, @newtablekey OUTPUT
                   INSERT INTO MatterNote (	date_added,	added_by,	owner_key,	group_key,	permissions,	matter_note_key,	matter_key,	note, matter_note_type_key,	date_of_note,	private_flag
                   ) VALUES ( getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, @newtablekey,  @newmatterkey, @repIntakeNote, @repIntakeNoteTypeKey, @dateSubmitted, 'N')
                    IF @@ERROR <> 0 
                      BEGIN
                        SET @ErrMsg = @ErrMsg+ ' Representative Matter Note insert has failed.'  
                       -- PRINT @ErrMsg                      
                      END
                    END 
            IF @IntakeOtherAgencyName<>''
              BEGIN
                SELECT top 1 @matternotetypekey = matter_note_type_key from MatterNoteType where code = 'IntakeOthAgn'
                IF @matternotetypekey IS NULL SET @matternotetypekey= 0
                EXECUTE GetNextNNumbers 'MatterNote', 1, @newtablekey OUTPUT
                  INSERT INTO MatterNote (	date_added,	added_by,	owner_key,	group_key,	permissions,	matter_note_key,	matter_key,	note, matter_note_type_key,	date_of_note,	private_flag
                   ) VALUES ( getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, @newtablekey, @newmatterkey, @IntakeOtherAgencyName, @matternotetypekey, @dateSubmitted, 'N')
                   IF @@ERROR <> 0 
					 BEGIN
					   SET @ErrMsg = @ErrMsg+ ' Other Agency Name Matter Note insert has failed.'  
					  -- PRINT @ErrMsg                      
					 END
                   END
              
              IF @IntakeInvDataNote<>''
                BEGIN
                EXECUTE GetNextNNumbers 'MatterNote', 1, @newtablekey OUTPUT
                  INSERT INTO MatterNote (	date_added,	added_by,	owner_key,	group_key,	permissions,	matter_note_key,	matter_key,	note, matter_note_type_key,	date_of_note,	private_flag
                   ) VALUES ( getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, @newtablekey, @newmatterkey, @IntakeInvDataNote, @intInvalidNoteTypeKey, @dateSubmitted, 'N')
                   IF @@ERROR <> 0 
					 BEGIN
					   SET @ErrMsg = @ErrMsg+ ' Invalid Data Matter Note insert has failed.'  
					  -- PRINT @ErrMsg                      
					 END
                   END
             
            IF @electronicSignature IS NOT NULL
              BEGIN
                SELECT top 1 @matternotetypekey = matter_note_type_key from MatterNoteType where code = 'IntakeSig'
                IF @matternotetypekey IS NULL SET @matternotetypekey= 0
                EXECUTE GetNextNNumbers 'MatterNote', 1, @newtablekey OUTPUT
                INSERT INTO MatterNote (	date_added,	added_by,	owner_key,	group_key,	permissions, matter_note_key,	matter_key,	note, matter_note_type_key,	date_of_note,	private_flag
                 ) VALUES ( getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511,  @newtablekey, @newmatterkey, @electronicSignature, @matternotetypekey, @dateSubmitted, 'N')
                 IF @@ERROR <> 0 
					BEGIN
					SET @ErrMsg = @ErrMsg+ ' Electronic Signature Matter Note insert has failed.'  
					--PRINT @ErrMsg                      
					END
			   END
			   
			   -- creating Intake Event notification
			 IF (@eventIntakeKey IS NOT NULL) and ( @eventIntakeStatus is NOT NULL)
			   BEGIN
			    --  SELECT @nbrDocs = count(*)  from CASEMATTERS_INTERNET_INTAKE.dbo.submissionattachments where submissionID = @submissionid
			      SELECT @eventIntakeDescription = 'Submission ID: '+CAST(submissionId as varchar(50))+' Original Submission ID: '+'  Submission Time Stamp: '+CAST(submissionTimestamp as varchar(50))++'  Submission File Count: '+ CAST(IsNull(filesCount, 0) as varchar(500))
			                                    +'  Submission Form Name: '+formName+'  Submission Form Version: '+CAST(formVersion as varchar(20))+'  Agency Form Owner: '+IsNull(agency, ' ')+ '  Agency Acronym: '+IsNull(agencyAbbreviation, ' ')+ 
			                                    +'  Submitter First Name: '+IsNull(UserFirstName,' ')+'  Submitter Middle Name: '+IsNull(UserMiddleName, ' ')+'  Submitter Last Name: '+IsNull(UserLastName, ' ')+
			                                    +'  Submitter Email: '+ IsNull(userEmail, ' ')+ '  Submitter Phone: '+IsNull(userPhone, ' ')+'  Submitter Fax: '+IsNull(userFax, ' ')+
			                                    +'  Submission Description: '+ IsNull(submissionDescription, ' ')
			                                    from CASEMATTERS_INTERNET_INTAKE.dbo.Submissions where submissionID = @submissionid
			      
			    
			      EXECUTE GetNextNNumbers 'EVENT', 1, @newtablekey OUTPUT
			      INSERT INTO Event ( event_key, start_date, event_type_key, event_category_key, subject,	matter_key,	date_added,	added_by,	owner_key,	group_key,	permissions,	status_flag,	event_status_key, description) 
			      VALUES (@newtablekey, @dateSubmitted, @eventIntakeKey, @eventIntakeCategory, @eventIntakeDefaultSubject, @newmatterkey, getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, 'A', @eventIntakeStatus, @eventIntakeDescription)
	           END
               
            IF @ErrMsg <>''
              BEGIN
                SELECT top 1 @matternotetypekey = matter_note_type_key from MatterNoteType where code = 'IntakeInvData'
                if @matternotetypekey IS NULL SET @matternotetypekey= 0
                EXECUTE GetNextNNumbers 'MatterNote', 1, @newtablekey OUTPUT
                 INSERT INTO MatterNote (	date_added,	added_by,	owner_key,	group_key,	permissions,	matter_note_key,	matter_key,	matter_note_type_key, note,	date_of_note,	private_flag
                 ) VALUES ( getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, @newtablekey,  @newmatterkey, @matternotetypekey, @ErrMsg, @dateSubmitted, 'N')
              END 
              
             -- get the matter number and create matter documents folder
             SELECT @matternumber = RTRIM(matter_number) from Matter where matter_key = @newmatterkey  
             IF (@NYCDocumentPath IS NOT NULL) and (@matternumber IS NOT NULL)  
             BEGIN
               SET @matterfolder = @NYCDocumentPath+'\'+CAST(@submissionID as varchar(50))
               UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.Submissions SET lawManagerCaseID = @matternumber, lawManagerCaseDirectory = @matterfolder  where submissionID = @submissionid  
               UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM SET err_message = @ErrMsg, matter_key = @newmatterkey , matter_number = @matternumber where submissionID = @submissionid
             END
            END -- if rollback = 0
            ELSE
            BEGIN
              UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM SET err_message = @ErrMsg where submissionID = @submissionid
		      UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.Submissions SET error_flag= 1 where submissionID = @submissionid
		      INSERT INTO CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionError(submissionID, Err_Message, Processed_Date) Values(@submissionid, @ErrMsg, getdate())
            END -- ELSE     
        COMMIT TRANSACTION tnxInsertIntake 
      END -- Part II 
SET @submissionscount = @submissionscount - 1

END -- while loop

END -- new submissions
-- Part III - creating documents
-- setting Additional submision events variables
SELECT @eventIntakeKey_ad = event_type_key, @eventIntakeCategory_ad = event_category_key, @eventIntakeDefaultSubject_ad= ISNULL(default_subject, 'New Intake Additional Submission') from EventType where code = 'INTAKEADD' and control = 'CSC'
SELECT @eventIntakeStatus_ad =  event_status_key  from EventStatus where event_category_key = @eventIntakeCategory_ad and default_flag = 'Y'
IF @eventIntakeDefaultSubject_ad IS NULL  SET @eventIntakeDefaultSubject_ad = 'Internet Intake Additional Submission'

SELECT @documentscount = count(*) from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionAttachments a
INNER JOIN  CASEMATTERS_INTERNET_INTAKE.dbo.Submissions s on s.submissionID = a.submissionID
INNER JOIN CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM lm on (lm.submissionID = a.submissionID OR lm.submissionID = s.submissionParentID)
WHERE s.filesMoved = 1 and a.document_key is null and a.error_flag IS NULL
-- populating temp table with distinct additional submissions for sending event notification
INSERT INTO @AdditionalSubmissions (submissionID, submissionParentID, matter_key)
SELECT DISTINCT a.submissionID, 
                s.submissionParentID,
                lm.matter_key
				from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionAttachments a
				INNER JOIN  CASEMATTERS_INTERNET_INTAKE.dbo.Submissions s on s.submissionID = a.submissionID
				INNER JOIN CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM lm on (lm.submissionID = s.submissionParentID)
				WHERE s.filesMoved = 1 and a.document_key is null and a.error_flag IS NULL and lm.matter_key IS NOT NULL
SELECT @nbrDocs_ad =  COUNT(*) FROM @AdditionalSubmissions

--PRINT 'Number of documents: '+CAST(@documentscount as varchar)
IF @documentscount > 0
BEGIN
  SELECT @doctypekey = document_type_key from DocumentType where code = 'INTDOC'

    -- start processing submissions
   WHILE @documentscount > 0
   BEGIN
   SELECT TOP 1 @submissionid = a.submissionID, 
                @submissionParentID = s.submissionParentID,
				@docpath = s.lawManagerCaseDirectory,  
				@docmatterkey  = lm.matter_key, 
				@docfilename = a.standardizedFileName ,
				@docTitle = CAST(a.submissionID as varchar(50))+': '+a.originalFileName 
				from CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionAttachments a
				INNER JOIN  CASEMATTERS_INTERNET_INTAKE.dbo.Submissions s on s.submissionID = a.submissionID
				INNER JOIN CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionDataLM lm on (lm.submissionID = a.submissionID OR lm.submissionID = s.submissionParentID)
				WHERE s.filesMoved = 1 and a.document_key is null and a.error_flag IS NULL
	
	IF @docpath IS NULL 
	SELECT @docpath = lawManagerCaseDirectory FROM CASEMATTERS_INTERNET_INTAKE.dbo.Submissions WHERE submissionID = @submissionParentID
	
	SET @docfullpath = 	@docpath+'\'+@docfilename		
	-- create document record
	 IF @docfullpath IS NOT NULL
	 BEGIN
	   EXECUTE GetNextNNumbers 'Document', 1, @newdockey OUTPUT
       INSERT INTO Document (date_added,	added_by,	document_date,	file_name, document_type_key,	matter_key, document_key, owner_key, group_key,  permissions, title)
              VALUES (Getdate(), @LMIntakeUserKey,  getDate(), @docfullpath,  @doctypekey, @docmatterkey,  @newdockey, @LMIntakeUserKey, @LMGroupKey, 511, @docTitle )
       IF @@ERROR <> 0 
         BEGIN
           UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionAttachments SET error_flag = 1 where submissionID = @submissionid and standardizedFileName =  @docfilename                        
         END         
       ELSE
          UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionAttachments set document_key = @newdockey WHERE submissionID = @submissionid and standardizedFileName =  @docfilename 
	 END
	 ELSE
	 UPDATE CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionAttachments SET error_flag = 1 where submissionID = @submissionid and standardizedFileName =  @docfilename                        
    
    SET @documentscount = @documentscount - 1
   END
  -- PRINT 'Event Type ' +CAST(@eventIntakeKey_ad  as varchar)
  -- PRINT 'Event Status ' +CAST(@eventIntakeStatus_ad  as varchar)
  -- PRINT 'nbr of documents: '+CAST(@nbrDocs_ad as varchar)
  
   -- PART IV - Sending Additional Documents Event Notifications
   IF ( @nbrDocs_ad > 0) AND (@eventIntakeKey_ad IS NOT NULL) and ( @eventIntakeStatus_ad is NOT NULL)
   BEGIN
      WHILE @nbrDocs_ad > 0
        BEGIN
        SELECT TOP 1 @submissionid = submissionID, 
                @submissionParentID = submissionParentID,				
				@eventmatterkey  = matter_key
	    FROM	@AdditionalSubmissions where event_sent IS NULL
				
	
			   -- creating Intake Event notification			 
			   
			      SELECT @eventIntakeDescription_ad = 'Submission ID: '+CAST(submissionId as varchar(50))+' Original Submission ID: '+CAST(submissionParentID as varchar(50))+'  Submission Time Stamp: '+CAST(submissionTimestamp as varchar(50))++'  Submission File Count: '+ CAST(IsNull(filesCount, 0) as varchar(500))
			                                    +'  Submission Form Name: '+formName+'  Submission Form Version: '+CAST(formVersion as varchar(20))+'  Agency Form Owner: '+IsNull(agency, ' ')+ '  Agency Acronym: '+IsNull(agencyAbbreviation, ' ')+ 
			                                    +'  Submitter First Name: '+IsNull(UserFirstName,' ')+'  Submitter Middle Name: '+IsNull(UserMiddleName, ' ')+'  Submitter Last Name: '+IsNull(UserLastName, ' ')+
			                                    +'  Submitter Email: '+ IsNull(userEmail, ' ')+ '  Submitter Phone: '+IsNull(userPhone, ' ')+'  Submitter Fax: '+IsNull(userFax, ' ')+
			                                    +'  Submission Description: '+ IsNull(submissionDescription, ' '), @dateSubmitted = submissionTimestamp
			                                    from CASEMATTERS_INTERNET_INTAKE.dbo.Submissions where submissionID = @submissionid
			      
			    
			      EXECUTE GetNextNNumbers 'EVENT', 1, @newtablekey OUTPUT
			      INSERT INTO Event ( event_key, start_date, event_type_key, event_category_key, subject,	matter_key,	date_added,	added_by,	owner_key,	group_key,	permissions,	status_flag,	event_status_key, description) 
			      VALUES (@newtablekey, @dateSubmitted, @eventIntakeKey_ad, @eventIntakeCategory_ad, @eventIntakeDefaultSubject_ad, @eventmatterkey, getdate(), @LMIntakeUserKey, @LMIntakeUserKey,  @LMGroupKey, 511, 'A', @eventIntakeStatus_ad, @eventIntakeDescription_ad)
	              -- if insert fails writes to the SubmissionError table
	              IF @@ERROR <> 0 
	                BEGIN
                      INSERT INTO CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionError(submissionID, Err_Message, Processed_Date) Values(@submissionid, 'Event Notification for this Submission failed.', getdate())
                      UPDATE @AdditionalSubmissions  SET event_sent = 0 where submissionID = @submissionid          
                    END                                   
                    -- flags temp table with successful event submission      
                   ELSE
                     UPDATE @AdditionalSubmissions  SET event_sent = 1 where submissionID = @submissionid   
	               	
      
      SET @nbrDocs_ad = @nbrDocs_ad - 1
      END
   END
   -- Additional Submission Event does not exists
   --ELSE
	  -- BEGIN
	    -- INSERT INTO CASEMATTERS_INTERNET_INTAKE.dbo.SubmissionError(submissionID, Err_Message, Processed_Date) --Values(@submissionid, 'Additional Submission Event for this Submission cannot be created.', getdate())
         --SELECT submissionID,  'Additional Submission Event for this Submission cannot be created.', getdate() FROM @AdditionalSubmissions 
         --  
       --END 
   DELETE  @AdditionalSubmissions 
END

END

GO

PRINT 'The new procedure WebIntake_sp was successfully installed.'
GO

