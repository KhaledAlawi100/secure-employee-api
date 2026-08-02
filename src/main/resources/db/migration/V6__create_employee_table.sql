-- ===========================================================
-- V6__create_employee_table.sql
-- Employee Management Module
-- ===========================================================

-- ===========================================================
-- Departments
-- ===========================================================

CREATE TABLE departments
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL UNIQUE,

    description VARCHAR(500)
);

-- ===========================================================
-- Positions
-- ===========================================================

CREATE TABLE positions
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL UNIQUE,

    description VARCHAR(500)
);

-- ===========================================================
-- Employees
-- ===========================================================

CREATE TABLE employees
(
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100) NOT NULL,

    phone VARCHAR(30),

    department_id BIGINT,

    position_id BIGINT,

    salary DOUBLE PRECISION NOT NULL
        CHECK (salary >= 0),

    approved BOOLEAN NOT NULL DEFAULT FALSE,

    status VARCHAR(30) NOT NULL,

    manager_id BIGINT,

    user_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_employee_position
        FOREIGN KEY (position_id)
        REFERENCES positions(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_employee_manager
        FOREIGN KEY (manager_id)
        REFERENCES employees(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_employee_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT
);

-- ===========================================================
-- Indexes
-- ===========================================================

CREATE INDEX idx_employee_department
    ON employees(department_id);

CREATE INDEX idx_employee_position
    ON employees(position_id);

CREATE INDEX idx_employee_manager
    ON employees(manager_id);

CREATE INDEX idx_employee_status
    ON employees(status);

CREATE INDEX idx_employee_user
    ON employees(user_id);

CREATE INDEX idx_department_name
    ON departments(name);

CREATE INDEX idx_position_name
    ON positions(name);