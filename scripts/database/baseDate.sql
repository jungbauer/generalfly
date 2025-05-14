-- insert roles for the role table
INSERT INTO role (id, name) VALUES ((select nextval('role_id_seq')), 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES ((select nextval('role_id_seq')), 'ROLE_USER');
INSERT INTO role (id, name) VALUES ((select nextval('role_id_seq')), 'ROLE_DEMO');