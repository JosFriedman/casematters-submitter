-- run in MS SQL CASEMATTERS_INTERNET_INTAKE* db

IF EXISTS (SELECT * FROM sys.views WHERE object_id = OBJECT_ID(N'[dbo].[LmAttachmentPath]'))
BEGIN
    DROP VIEW dbo.LmAttachmentPath;
END
GO



CREATE VIEW [dbo].[LmAttachmentPath]
AS

(
SELECT 'CSC' as AgencyAbbreviation, CAST(variable_value_store as varchar(255)) as AttachmentPath from CSC_CaseMatters_App_Dev.dbo.LMVariable where variable_type_key in (Select variable_type_key from CSC_CaseMatters_App_Dev.dbo.LMVariableType
where (Upper(name) like 'NYCDOCUMENTPATH%'))
UNION
SELECT 'OATH' as AgencyAbbreviation, CAST(variable_value_store as varchar(255)) AttachmentPath from OATH_CaseMatters_App_Dev.dbo.LMVariable where variable_type_key in (Select variable_type_key from OATH_CaseMatters_App_Dev.dbo.LMVariableType
where (Upper(name) like 'NYCDOCUMENTPATH'))
UNION
SELECT 'OCB'  as AgencyAbbreviation, CAST(variable_value_store as varchar(255))AttachmentPath from OCB_CaseMatters_App_Dev.dbo.LMVariable where variable_type_key in (Select variable_type_key from OCB_CaseMatters_App_Dev.dbo.LMVariableType
where (Upper(name) like 'NYCDOCUMENTPATH'))
)


