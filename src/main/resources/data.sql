-- https://docs.spring.io/spring-boot/how-to/data-initialization.html

INSERT INTO user(id, email, pwd, name, creation_date, update_date) 
VALUES
(1, 'admin@admin.com', sha2('123',512), 'Administrador', sysdate(), sysdate()),
(2, 'user@user.com',   sha2('123',512), 'user',          sysdate(), sysdate());

insert into Role (id, name, description) 
values 
(1, 'Admin', 'Promove novos usuaros e aloca blocos de trabalho'), 
(2, 'Worker', 'Trabalha nos blocos alocados'), 
(3, 'Novo', 'Soh pode fazer login e editar dados pessoais.');


insert into Permission (id, name, description) values 
(1, 'LER', 'Permite consultar recurso.'),
(2, 'ATUALIZAR', 'Permite atualizar recurso.'),
(3, 'CRIAR', 'Permite criar recurso'),
(4, 'REMOVER', 'Permite remover recurso.');


INSERT INTO User_has_role(User_id,role_id) values
(1,1),
(2,2);

INSERT INTO role_has_Permission(role_id,Permission_id) values
(1,1),
(1,2),
(1,3),
(1,4),
(2,1),
(2,2);


