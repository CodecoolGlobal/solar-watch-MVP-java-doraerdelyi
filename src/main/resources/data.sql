INSERT INTO solar_watch.public.roles (role_type) VALUES ('ROLE_USER')
ON CONFLICT (role_type) DO NOTHING;

INSERT INTO solar_watch.public.roles (role_type) VALUES ('ROLE_ADMIN')
ON CONFLICT (role_type) DO NOTHING;