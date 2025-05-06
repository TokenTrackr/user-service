-- V1__create_user_table.sql
CREATE TABLE "users_tbl" (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        username VARCHAR(50) NOT NULL UNIQUE,
                        enabled BOOLEAN NOT NULL DEFAULT TRUE,
                        keycloak_id VARCHAR(255),
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL
);
