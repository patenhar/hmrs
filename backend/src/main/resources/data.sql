-- INSERT INTO roles (pk_role_id, role_name) VALUES (NEWID(),'Employee'), (NEWID(), 'Manager'), (NEWID(), 'Admin');
-- INSERT INTO permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'Something')
SELECT GETDATE();

-- ── Social Module: Post Visibilities ────────────────────────────────────────
IF NOT EXISTS (SELECT 1 FROM Post_Visibilities WHERE visibility = 'ALL_EMPLOYEES')
    INSERT INTO Post_Visibilities (pk_post_visisbility_id, visibility) VALUES (NEWID(), 'ALL_EMPLOYEES');

-- ── Social Module: Tags ──────────────────────────────────────────────────────
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

-- ── Social Module: Permissions ───────────────────────────────────────────────
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'ADD_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'ADD_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'VIEW_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'VIEW_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'UPDATE_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'UPDATE_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'DELETE_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'DELETE_POST');
IF NOT EXISTS (SELECT 1 FROM Permissions WHERE permission_name = 'MANAGE_POST')
    INSERT INTO Permissions (pk_permission_id, permission_name) VALUES (NEWID(), 'MANAGE_POST');
