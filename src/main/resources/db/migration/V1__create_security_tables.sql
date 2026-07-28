-- =========================================
-- V1: Create Security Tables
-- =========================================


-- =========================================
-- Permissions
-- =========================================

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);


-- =========================================
-- Roles
-- =========================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);


-- =========================================
-- Users
-- =========================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(50) NOT NULL UNIQUE,

    email VARCHAR(255) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE
);


-- =========================================
-- User ↔ Role
-- =========================================

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,

    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE
);


-- =========================================
-- Role ↔ Permission
-- =========================================

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,

    permission_id BIGINT NOT NULL,

    PRIMARY KEY (role_id, permission_id),

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id)
        REFERENCES permissions(id)
        ON DELETE CASCADE
);