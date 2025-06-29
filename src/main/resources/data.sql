-- ラジオ局
INSERT INTO radio_station (id, name) VALUES (1, 'ニッポン放送');
INSERT INTO radio_station (id, name) VALUES (2, '文化放送');
INSERT INTO radio_station (id, name) VALUES (3, 'TOKYO FM');

-- ラジオ番組
INSERT INTO radio_program (title, host, description, station_id) VALUES ('オードリーANN', 'オードリー', '', 1);
INSERT INTO radio_program (title, host, description, station_id) VALUES ('星野源ANN', '星野源', '', 1);

-- ユーザー
-- password: userpass
INSERT INTO app_user (id, username, password, role) VALUES (1, 'user', '$2b$12$VYk3.f4zBpDR1NXVvMKP1.F1B.avMuGzlmbnw8aDnHNXiH9JluFlW', 'USER');
-- password: adminpass
INSERT INTO app_user (id, username, password, role) VALUES (2, 'admin', '$2b$12$Tl36hRHbVZOQiPKoFtXY0eT331/JTneK5L32C1KOX2jn3HGhRz.j2', 'ADMIN');