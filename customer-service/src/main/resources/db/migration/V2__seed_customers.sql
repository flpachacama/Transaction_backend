WITH p AS (
INSERT INTO persons(name, gender, age, identification, address, phone)
VALUES ('Jose Lema','MALE',34,'1723434343','Otavalo sn y principal','098254785')
    RETURNING id
    )
INSERT INTO customers(id, client_id, password, status)
SELECT id, 'joselema', crypt('1234', gen_salt('bf')), true FROM p;

WITH p AS (
INSERT INTO persons(name, gender, age, identification, address, phone)
VALUES ('Marianela Montalvo','FEMALE',28,'1723434344','Amazonas y NNUU','097548965')
    RETURNING id
    )
INSERT INTO customers(id, client_id, password, status)
SELECT id, 'marianela', crypt('5678', gen_salt('bf')), true FROM p;

WITH p AS (
INSERT INTO persons(name, gender, age, identification, address, phone)
VALUES ('Juan Osorio','MALE',32,'1723434345','13 junio y Equinoccial','098874587')
    RETURNING id
    )
INSERT INTO customers(id, client_id, password, status)
SELECT id, 'juanosorio', crypt('1245', gen_salt('bf')), true FROM p;