-- =========================================================
-- V2: Create refresh_tokens table
-- =========================================================

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,

    token VARCHAR(512) NOT NULL,

    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    revoked_at TIMESTAMP WITH TIME ZONE,

    user_id BIGINT NOT NULL,

    CONSTRAINT uk_refresh_tokens_token
        UNIQUE (token),

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user_id
    ON refresh_tokens(user_id);