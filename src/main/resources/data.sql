-- Insert permissions
INSERT INTO permissions (name, description) VALUES
('USER_READ', 'Read user information'),
('USER_WRITE', 'Write user information'),
('USER_DELETE', 'Delete user information'),
('ADMIN_READ', 'Admin read permission'),
('ADMIN_WRITE', 'Admin write permission'),
('ADMIN_DELETE', 'Admin delete permission');

-- Insert roles
INSERT INTO roles (name, description) VALUES
('ROLE_USER', 'Standard user role'),
('ROLE_ADMIN', 'Administrator role');

-- Insert role-permission mappings
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- USER role permissions
(1, 1), -- USER_READ
(1, 2), -- USER_WRITE
-- ADMIN role permissions
(2, 1), -- USER_READ
(2, 2), -- USER_WRITE
(2, 3), -- USER_DELETE
(2, 4), -- ADMIN_READ
(2, 5), -- ADMIN_WRITE
(2, 6); -- ADMIN_DELETE

-- Insert users (passwords are encoded with BCrypt)
-- Password for both users is: "password123"
INSERT INTO users (username, email, password, first_name, last_name, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at) VALUES
('user', 'user@example.com', '$2a$10$NQuRk7OMCKS2lbZr84yNuOZ3eJv8js.hUohgC7vpTWJL3EbvDITmm', 'John', 'Doe', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('admin', 'admin@example.com', '$2a$10$NQuRk7OMCKS2lbZr84yNuOZ3eJv8js.hUohgC7vpTWJL3EbvDITmm', 'Admin', 'User', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert user-role mappings
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- user -> ROLE_USER
(2, 2); -- admin -> ROLE_ADMIN