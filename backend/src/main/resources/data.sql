IF NOT EXISTS (SELECT 1 FROM Roles WHERE role_name = 'HR')
    INSERT INTO Roles (pk_role_id, role_name) VALUES (NEWID(), 'HR');
IF NOT EXISTS (SELECT 1 FROM Roles WHERE role_name = 'Employee')
    INSERT INTO Roles (pk_role_id, role_name) VALUES (NEWID(), 'Employee');
IF NOT EXISTS (SELECT 1 FROM Roles WHERE role_name = 'Manager')
    INSERT INTO Roles (pk_role_id, role_name) VALUES (NEWID(), 'Manager');

IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_USER')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_USER');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_PROFILE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_PROFILE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_ORGCHART')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_ORGCHART');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_TRAVEL')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_TRAVEL');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_EXPENSE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_EXPENSE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_DOCUMENT')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_DOCUMENT');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_JOB')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_JOB');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_REFERRAL')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_REFERRAL');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_GAME')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_GAME');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_BOOKING')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_BOOKING');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_ALL_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_ALL_POST');

IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_TRAVEL')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_TRAVEL');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_EXPENSE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_EXPENSE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'ADD_EXPENSE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'ADD_EXPENSE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_EXPENSE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_EXPENSE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'ADD_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'ADD_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_DOCUMENT')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_DOCUMENT');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'ADD_DOCUMENT')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'ADD_DOCUMENT');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_DOCUMENT')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_DOCUMENT');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_BOOKING')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_BOOKING');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'ADD_BOOKING')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'ADD_BOOKING');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_BOOKING')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_BOOKING');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_JOB')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_JOB');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'SHARE_JOB')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'SHARE_JOB');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'REFER_JOB')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'REFER_JOB');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_PROFILE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_PROFILE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'UPDATE_OWN_PROFILE')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'UPDATE_OWN_PROFILE');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_GAME')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_GAME');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_ORGCHART')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_ORGCHART');

IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_TEAM')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_TEAM');

INSERT INTO role_permissions (fk_role_id, fk_permission_id)
SELECT r.pk_role_id, p.pk_permission_id
FROM Roles r, Permissions p
WHERE r.role_name = 'HR'
  AND p.permission_name IN (
      'MANAGE_ALL_USER','MANAGE_ALL_PROFILE','MANAGE_ALL_ORGCHART',
      'MANAGE_ALL_TRAVEL','MANAGE_ALL_EXPENSE','MANAGE_ALL_DOCUMENT',
      'MANAGE_ALL_JOB','MANAGE_ALL_REFERRAL','MANAGE_ALL_GAME',
      'MANAGE_ALL_BOOKING','MANAGE_ALL_POST'
  )
  AND NOT EXISTS (
    SELECT 1 FROM role_permissions rp
    WHERE rp.fk_role_id = r.pk_role_id AND rp.fk_permission_id = p.pk_permission_id
  );

INSERT INTO role_permissions (fk_role_id, fk_permission_id)
SELECT r.pk_role_id, p.pk_permission_id
FROM Roles r, Permissions p
WHERE r.role_name = 'Employee'
  AND p.permission_name IN (
      'VIEW_TRAVEL',
      'VIEW_EXPENSE','ADD_EXPENSE','MANAGE_EXPENSE',
      'VIEW_POST','ADD_POST','MANAGE_POST',
      'VIEW_DOCUMENT','ADD_DOCUMENT','MANAGE_DOCUMENT',
      'VIEW_BOOKING','ADD_BOOKING','MANAGE_BOOKING',
      'VIEW_JOB','SHARE_JOB','REFER_JOB',
      'VIEW_PROFILE','UPDATE_OWN_PROFILE','VIEW_GAME','VIEW_ORGCHART'
  )
  AND NOT EXISTS (
    SELECT 1 FROM role_permissions rp
    WHERE rp.fk_role_id = r.pk_role_id AND rp.fk_permission_id = p.pk_permission_id
  );

INSERT INTO role_permissions (fk_role_id, fk_permission_id)
SELECT r.pk_role_id, p.pk_permission_id
FROM Roles r, Permissions p
WHERE r.role_name = 'Manager'
  AND p.permission_name IN (
      'VIEW_TRAVEL',
      'VIEW_EXPENSE','ADD_EXPENSE','MANAGE_EXPENSE',
      'VIEW_POST','ADD_POST','MANAGE_POST',
      'VIEW_DOCUMENT','ADD_DOCUMENT','MANAGE_DOCUMENT',
      'VIEW_BOOKING','ADD_BOOKING','MANAGE_BOOKING',
      'VIEW_JOB','SHARE_JOB','REFER_JOB',
      'VIEW_PROFILE','UPDATE_OWN_PROFILE','VIEW_GAME','VIEW_ORGCHART',
      'VIEW_TEAM'
  )
  AND NOT EXISTS (
    SELECT 1 FROM role_permissions rp
    WHERE rp.fk_role_id = r.pk_role_id AND rp.fk_permission_id = p.pk_permission_id
  );

IF NOT EXISTS (SELECT 1 FROM Post_Visibilities WHERE visibility = 'ALL_EMPLOYEES')
    INSERT INTO Post_Visibilities (pk_post_visisbility_id, visibility) VALUES (NEWID(), 'ALL_EMPLOYEES');

IF NOT EXISTS (SELECT 1 FROM Tags WHERE tag = 'Achievement')
    INSERT INTO Tags (pk_tag_id, tag) VALUES (NEWID(), 'Achievement');
IF NOT EXISTS (SELECT 1 FROM Tags WHERE tag = 'Milestone')
    INSERT INTO Tags (pk_tag_id, tag) VALUES (NEWID(), 'Milestone');
IF NOT EXISTS (SELECT 1 FROM Tags WHERE tag = 'Teamwork')
    INSERT INTO Tags (pk_tag_id, tag) VALUES (NEWID(), 'Teamwork');
IF NOT EXISTS (SELECT 1 FROM Tags WHERE tag = 'Innovation')
    INSERT INTO Tags (pk_tag_id, tag) VALUES (NEWID(), 'Innovation');
IF NOT EXISTS (SELECT 1 FROM Tags WHERE tag = 'Birthday')
    INSERT INTO Tags (pk_tag_id, tag) VALUES (NEWID(), 'Birthday');
IF NOT EXISTS (SELECT 1 FROM Tags WHERE tag = 'Work Anniversary')
    INSERT INTO Tags (pk_tag_id, tag) VALUES (NEWID(), 'Work Anniversary');

SELECT GETDATE();