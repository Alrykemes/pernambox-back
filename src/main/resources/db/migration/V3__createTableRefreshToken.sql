CREATE TABLE refresh_token
(
    token           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_token_users FOREIGN KEY (user_id)
        REFERENCES users (id)
);