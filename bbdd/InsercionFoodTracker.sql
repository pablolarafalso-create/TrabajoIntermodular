create database foodTracker;
use foodtracker;
create table usuario(
id_user int auto_increment primary key,
nombre varchar(20) not null,
apellidos varchar(50) not null,
email varchar(50) not null unique,
contraseña varchar(20) not null,
fecha_nacimiento date not null,
altura decimal(3,2) not null,
peso decimal (5,2) not null,
fechaCreacion datetime not null,
racha int not null
);
create table objetivo_diario(
id_user int auto_increment primary key,
kcal int not null,
proteinas decimal(3,1) not null,
carbohidratos decimal(3,1) not null,
grasas decimal(3,1) not null,
foreign key (id_user) references usuario(id_user)
);
create table registro_diario(
registro_id int auto_increment primary key,
hora_registro datetime not null,
id_user int,
foreign key (id_user) references usuario(id_user)
);
create table comida(
id_comida int auto_increment primary key,
tipo_comida varchar(20),
visiblesn char(1) not null
);
create table alimento(
id_alimento int auto_increment primary key,
nombre varchar(50) not null,
kcal int not null,
proteinas decimal(3,1) not null,
carbohidratos decimal(3,1) not null,
grasas decimal(3,1) not null
);
create table registro_diario_comida(
id_comida int,
registro_id int,
primary key (id_comida, registro_id),
foreign key (id_comida) references comida(id_comida),
foreign key (registro_id) references registro_diario(registro_id)
);
create table comida_alimento(
id_comida int,
id_alimento int,
primary key (id_comida, id_alimento),
cantidad int not null,
foreign key (id_comida) references comida(id_comida),
foreign key (id_alimento) references alimento(id_alimento)
);

INSERT INTO alimento (nombre, kcal, proteinas, carbohidratos, grasas) VALUES
-- Cereales y derivados
('Avena', 389, 16.9, 66.3, 6.9),
('Arroz integral', 111, 2.6, 23.0, 0.9),
('Quinoa', 120, 4.4, 21.3, 1.9),
('Cuscús', 112, 3.8, 23.2, 0.2),
('Pan integral', 247, 8.5, 41.0, 4.2),
('Pasta integral', 124, 5.0, 25.0, 1.1),
('Maíz', 96, 3.4, 21.0, 1.5),
('Mijo', 119, 3.5, 23.7, 1.0),
('Amaranto', 102, 3.8, 19.0, 1.6),
('Trigo integral', 340, 13.2, 72.0, 2.5),

-- Proteínas animales
('Pechuga de pollo', 165, 31.0, 0.0, 3.6),
('Pavo', 135, 29.0, 0.0, 1.0),
('Carne de res magra', 217, 26.0, 0.0, 12.0),
('Cerdo magro', 242, 27.0, 0.0, 14.0),
('Salmón', 208, 20.0, 0.0, 13.0),
('Atún', 132, 28.0, 0.0, 1.0),
('Sardinas', 208, 25.0, 0.0, 11.0),
('Huevo', 155, 13.0, 1.1, 11.0),
('Clara de huevo', 52, 11.0, 0.7, 0.2),
('Leche', 42, 3.4, 5.0, 1.0),

-- Proteínas vegetales
('Lentejas', 116, 9.0, 20.0, 0.4),
('Garbanzos', 164, 8.9, 27.0, 2.6),
('Alubias negras', 132, 8.9, 23.7, 0.5),
('Alubias rojas', 127, 8.7, 22.8, 0.5),
('Soja', 173, 16.6, 9.9, 9.0),
('Tofu', 76, 8.0, 1.9, 4.8),
('Tempeh', 193, 20.0, 9.0, 11.0),
('Edamame', 121, 11.0, 10.0, 5.0),
('Habas', 88, 7.6, 18.7, 0.7),
('Guisantes', 81, 5.4, 14.0, 0.4),

-- Verduras
('Brócoli', 55, 3.7, 11.0, 0.6),
('Espinaca', 23, 2.9, 3.6, 0.4),
('Lechuga', 15, 1.4, 2.9, 0.2),
('Tomate', 18, 0.9, 3.9, 0.2),
('Pepino', 16, 0.7, 3.6, 0.1),
('Zanahoria', 41, 0.9, 10.0, 0.2),
('Pimiento rojo', 31, 1.0, 6.0, 0.3),
('Pimiento verde', 20, 0.9, 4.6, 0.2),
('Calabacín', 17, 1.2, 3.1, 0.3),
('Berenjena', 25, 1.0, 6.0, 0.2),

('Cebolla', 40, 1.1, 9.3, 0.1),
('Ajo', 149, 6.4, 33.1, 0.5),
('Coliflor', 25, 1.9, 5.0, 0.3),
('Repollo', 25, 1.3, 6.0, 0.1),
('Judías verdes', 31, 1.8, 7.0, 0.2),
('Espárragos', 20, 2.2, 3.9, 0.1),
('Champiñones', 22, 3.1, 3.3, 0.3),
('Setas', 28, 3.6, 4.3, 0.5),
('Remolacha', 43, 1.6, 10.0, 0.2),
('Apio', 16, 0.7, 3.0, 0.2),

-- Frutas
('Manzana', 52, 0.3, 14.0, 0.2),
('Plátano', 89, 1.1, 23.0, 0.3),
('Naranja', 47, 0.9, 12.0, 0.1),
('Mandarina', 53, 0.8, 13.0, 0.3),
('Pera', 57, 0.4, 15.0, 0.1),
('Melocotón', 39, 0.9, 10.0, 0.3),
('Piña', 50, 0.5, 13.0, 0.1),
('Mango', 60, 0.8, 15.0, 0.4),
('Papaya', 43, 0.5, 11.0, 0.3),
('Sandía', 30, 0.6, 8.0, 0.2),

('Melón', 34, 0.8, 8.0, 0.2),
('Fresas', 32, 0.7, 7.7, 0.3),
('Arándanos', 57, 0.7, 14.0, 0.3),
('Frambuesas', 52, 1.2, 12.0, 0.7),
('Uvas', 69, 0.6, 18.0, 0.2),
('Kiwi', 61, 1.1, 15.0, 0.5),
('Granada', 83, 1.7, 19.0, 1.2),
('Higo', 74, 0.8, 19.0, 0.3),
('Ciruela', 46, 0.7, 11.0, 0.3),
('Cereza', 50, 1.0, 12.0, 0.3),

-- Grasas saludables y frutos secos
('Almendras', 579, 21.0, 22.0, 50.0),
('Nueces', 654, 15.0, 14.0, 65.0),
('Avellanas', 628, 15.0, 17.0, 61.0),
('Pistachos', 562, 20.0, 28.0, 45.0),
('Anacardos', 553, 18.0, 30.0, 44.0),
('Semillas de chía', 486, 17.0, 42.0, 31.0),
('Semillas de lino', 534, 18.0, 29.0, 42.0),
('Semillas de girasol', 584, 21.0, 20.0, 51.0),
('Semillas de calabaza', 559, 30.0, 11.0, 49.0),
('Aguacate', 160, 2.0, 9.0, 15.0),

-- Lácteos y derivados
('Yogur natural', 59, 10.0, 3.6, 0.4),
('Yogur griego', 97, 9.0, 3.0, 5.0),
('Queso fresco', 98, 11.0, 3.0, 4.0),
('Queso curado', 402, 25.0, 1.3, 33.0),
('Requesón', 98, 11.0, 3.4, 4.3),
('Kéfir', 41, 3.3, 4.0, 1.0),
('Leche desnatada', 34, 3.4, 5.0, 0.1),
('Leche semidesnatada', 46, 3.3, 5.0, 1.6),
('Leche entera', 61, 3.2, 5.0, 3.3),
('Cuajada', 98, 5.5, 4.0, 6.0);
