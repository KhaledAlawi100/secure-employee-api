-- =========================================
-- V8: Add OAuth2 Support to Users
-- =========================================

-- =========================================
-- Add Authentication Provider
-- =========================================

ALTER TABLE users
ADD COLUMN provider VARCHAR(20)
NOT NULL DEFAULT 'LOCAL';


-- =========================================
-- Google Provider Unique Identifier
-- =========================================

ALTER TABLE users
ADD COLUMN provider_id VARCHAR(255);


-- =========================================
-- Google Profile Image
-- =========================================

ALTER TABLE users
ADD COLUMN image_url VARCHAR(500);


-- =========================================
-- Password becomes optional
-- (Google users do not have a local password)
-- =========================================

ALTER TABLE users
ALTER COLUMN password DROP NOT NULL;


-- =========================================
-- Ensure provider_id is unique
-- (Multiple NULL values are allowed in PostgreSQL)
-- =========================================

ALTER TABLE users
ADD CONSTRAINT uk_users_provider_id
UNIQUE (provider_id);