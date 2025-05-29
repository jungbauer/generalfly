-- insert roles for the role table
INSERT INTO role (id, name) VALUES ((select nextval('role_id_seq')), 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES ((select nextval('role_id_seq')), 'ROLE_USER');
INSERT INTO role (id, name) VALUES ((select nextval('role_id_seq')), 'ROLE_DEMO');

-- insert admin user. pwd = admin
INSERT INTO user_account (id, email, first_name, last_name, password) VALUES ((select nextval('user_account_seq')), 'admin@admin.com', 'Admin', 'Admin', '$2a$10$CleGaEvo/FLrlYcG3Y.dQ.hBkezMwqbnUwYeypgzsxIP47N0FDB7u');
INSERT INTO user_roles (user_id, role_id) VALUES ((select id from user_account where email='admin@admin.com'),(select id from role where name='ROLE_USER'));
INSERT INTO user_roles (user_id, role_id) VALUES ((select id from user_account where email='admin@admin.com'),(select id from role where name='ROLE_ADMIN'));

-- insert test user. pwd = test
INSERT INTO user_account (id, email, first_name, last_name, password) VALUES ((select nextval('user_account_seq')), 'test@test.com', 'Tester', 'Testing', '$2a$10$vI9IMIdFT5pjs/FhKCtKXOb9u1K6MacKkEGrMAkBR6WS5R6exuqs2');
INSERT INTO user_roles (user_id, role_id) VALUES ((select id from user_account where email='test@test.com'),(select id from role where name='ROLE_USER'));