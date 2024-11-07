INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('c084162a-133f-4046-8866-fe5b8f43f6c9', 'juan.delgado@ejemplo.com', 'Pa5sw0rd!', 'Juan', 'Delgado', 'Pérez', 'Hospital Central', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('4b95832f-685e-4116-b4ca-e681d7c03542', 'maria.sanchez85@gmail.com', 'MiContr4seña2!', 'María', 'Sánchez', 'López', 'Clínica Universitaria', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('7a6f309b-091e-4c23-9c5a-2b06d079361b', 'pedro.martinez@salud.gob.es', 'Segura123@', 'Pedro', 'Martínez', 'González', 'Centro de Salud Rural', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('9f384d71-22ee-4b54-9467-5c2a4856b42f', 'juan.perez@ejemplo.com', 'Contraseña1@', 'Juan', 'Pérez', 'González', 'Universidad de Ejemplo', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('3a72b9f5-864c-40fe-821b-7d5ca1234567', 'maria.rodriguez@ejemplo.com', 'MiContrasena2*', 'María', 'Rodríguez', 'López', 'Hospital Central', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('b2c3d4e5-f6g7-h8i9-j0k1-l2m3n4o5p6q7', 'pedro.martinez@ejemplo.com', 'segura3#Password', 'Pedro', 'Martínez', 'Hernández', 'Empresa XYZ', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('5a6b7c8d-9e0f-1g2h-3i4j-5k6l7m8n9o0', 'ana.garcia@ejemplo.com', 'SuperSegura4$!', 'Ana', 'García', 'Jiménez', 'Gimnasio Fit', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('7d8e9f0a-1b2c-3d4e-5f6g-7h8i9j0k1l2', 'luis.fernandez@ejemplo.com', 'MiClaveMasSegura5%', 'Luis', 'Fernández', 'Ruiz', 'Restaurante La Cocina', true);

INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('f25517b5-3304-4fa2-9133-bcb53e07ea18', 'luise.fdezmedina@gmail.com', 'admin1234', 'Luis Eduardo', 'Fernández-Medina', 'Cimas', 'ESI UCLM', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('5803dc40-eec9-46c6-a308-b32588f294b6', 'guillermo.espejo@alu.uclm.es', 'admin1234', 'Guillermo', 'Espejo', 'Palomeque', 'ESI UCLM', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('86a54ba8-5c2d-496a-9250-ce7d35aabb11', 'yolanda.galvan@alu.uclm.es', 'admin1234', 'Yolanda', 'Galván', 'Redondo', 'ESI UCLM', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('22fad7d4-4746-4bdf-9606-6a5659615b11', 'adrian.gomez14@alu.uclm.es', 'admin1234', 'Adrian', 'Gomez del Moral', 'Rodriguez-Madridejos', 'ESI UCLM', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('1cff7cf2-4c6f-463f-b671-256d7c534fd6', 'ivan.jimenez4@alu.uclm.es', 'admin1234', 'Iván', 'Jimenez', 'Quintana', 'ESI UCLM', true);
INSERT INTO Usuarios (id, email, pwd, nombre, apellido1, apellido2, centro, twoFA) VALUES ('b6c34fef-9d99-423d-82cd-daa178f05f11', 'antonio.sanchez36@alu.uclm.es', 'admin1234', 'Antonio', 'Sanchez', 'Sanchez', 'ESI UCLM', true);


INSERT INTO Admins (id, interno) VALUES ('c084162a-133f-4046-8866-fe5b8f43f6c9', 1);
INSERT INTO Admins (id, interno) VALUES ('4b95832f-685e-4116-b4ca-e681d7c03542', 0);
INSERT INTO Admins (id, interno) VALUES ('7a6f309b-091e-4c23-9c5a-2b06d079361b', 0);

INSERT INTO Admins (id, interno) VALUES ('f25517b5-3304-4fa2-9133-bcb53e07ea18', 0);
INSERT INTO Admins (id, interno) VALUES ('5803dc40-eec9-46c6-a308-b32588f294b6', 0);
INSERT INTO Admins (id, interno) VALUES ('86a54ba8-5c2d-496a-9250-ce7d35aabb11', 0);
INSERT INTO Admins (id, interno) VALUES ('22fad7d4-4746-4bdf-9606-6a5659615b11', 0);
INSERT INTO Admins (id, interno) VALUES ('1cff7cf2-4c6f-463f-b671-256d7c534fd6', 0);
INSERT INTO Admins (id, interno) VALUES ('b6c34fef-9d99-423d-82cd-daa178f05f11', 0);

INSERT INTO Empleados (id, departamento, fechaalta, perfil, bloqueado, verificado) VALUES ('9f384d71-22ee-4b54-9467-5c2a4856b42f', 'Informática', '2023-11-15', 'Estudiante', 1, 0);
INSERT INTO Empleados (id, departamento, fechaalta, perfil, bloqueado, verificado) VALUES ('3a72b9f5-864c-40fe-821b-7d5ca1234567', 'Medicina', '2024-01-02', 'Médico', 0, 1);
INSERT INTO Empleados (id, departamento, fechaalta, perfil, bloqueado, verificado) VALUES ('b2c3d4e5-f6g7-h8i9-j0k1-l2m3n4o5p6q7', 'Ventas', '2023-09-20', 'Vendedor', 1, 0);
INSERT INTO Empleados (id, departamento, fechaalta, perfil, bloqueado, verificado) VALUES ('5a6b7c8d-9e0f-1g2h-3i4j-5k6l7m8n9o0', 'Recepción', '2024-02-10', 'Recepcionista', 1, 0);
INSERT INTO Empleados (id, departamento, fechaalta, perfil, bloqueado, verificado) VALUES ('7d8e9f0a-1b2c-3d4e-5f6g-7h8i9j0k1l2', 'Cocina', '2023-12-15', 'Cocinero', 0, 1);

INSERT INTO ausencias (fecha_inicio, fecha_fin, motivo, fk_usuario) VALUES ('2024-03-15', '2024-03-16', 'Enfermedad', '9f384d71-22ee-4b54-9467-5c2a4856b42f');
INSERT INTO ausencias (fecha_inicio, fecha_fin, motivo, fk_usuario) VALUES ('2024-04-05', '2024-04-20', 'Vacaciones', '9f384d71-22ee-4b54-9467-5c2a4856b42f');
INSERT INTO ausencias (fecha_inicio, fecha_fin, motivo, fk_usuario) VALUES ('2024-03-20', '2024-03-22', 'Asuntos personales', '3a72b9f5-864c-40fe-821b-7d5ca1234567');

--INSERT INTO turnos (hora_inicio, hora_final) VALUES ("")