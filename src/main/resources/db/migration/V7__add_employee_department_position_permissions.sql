-- ==========================================
-- V7__add_department_and_position_permissions.sql
-- ==========================================

INSERT INTO permissions (name)
VALUES
    ('DEPARTMENT_READ'),
    ('DEPARTMENT_CREATE'),
    ('DEPARTMENT_UPDATE'),
    ('DEPARTMENT_DELETE'),

    ('POSITION_READ'),
    ('POSITION_CREATE'),
    ('POSITION_UPDATE'),
    ('POSITION_DELETE')
ON CONFLICT (name) DO NOTHING;