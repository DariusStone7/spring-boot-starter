INSERT INTO profile (label, created_at) values ( 'ADMIN', now());

INSERT INTO users ( email, name, username, password, status, created_at)
values ( 'nguetsa.darius@yahoo.com', 'Darius NGUETSA', 'darius', '$2a$12$JwtQhLliatxWIIq6YZ9CPeWtuPHya11aGNPSVPZWVvSJ8urUqfTAy', 'ACTIVE', now());

INSERT INTO user_profiles values (1, 1);
