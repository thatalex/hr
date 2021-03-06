;             
CREATE USER IF NOT EXISTS SA SALT '8ae19a5e86b11498' HASH '4e148df7184296be6dad3fb40e2a8c6ac373de75d06320fdbe392a065eb43cfd' ADMIN;           
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_662A682D_5B20_41E3_A452_BAF291308443 START WITH 19 BELONGS_TO_TABLE;   
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_72059A3D_40B3_4A05_9A8A_B0AD572F0056 START WITH 3 BELONGS_TO_TABLE;    
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_BFDCE52E_341E_4F62_AD1B_0BCCBFCD23C4 START WITH 1 BELONGS_TO_TABLE;    
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_2CCF38A3_9624_4ADE_BE1C_17AE55D0BF8C START WITH 2 BELONGS_TO_TABLE;    
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_E3057F7C_C5FD_40F1_8E2E_632E780D197A START WITH 5 BELONGS_TO_TABLE;    
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_585FC9D1_5041_4BD6_9DB4_9AA21F511F70 START WITH 28 BELONGS_TO_TABLE;   
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_988070B5_4263_4F77_8887_4A1B6E2B98F5 START WITH 10 BELONGS_TO_TABLE;   
CREATE CACHED TABLE PUBLIC.ACCOUNT(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_E3057F7C_C5FD_40F1_8E2E_632E780D197A) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_E3057F7C_C5FD_40F1_8E2E_632E780D197A,
    NAME VARCHAR(255),
    USER_USER_ID BIGINT
);   
ALTER TABLE PUBLIC.ACCOUNT ADD CONSTRAINT PUBLIC.CONSTRAINT_E PRIMARY KEY(ID);
-- 4 +/- SELECT COUNT(*) FROM PUBLIC.ACCOUNT; 
INSERT INTO PUBLIC.ACCOUNT(ID, NAME, USER_USER_ID) VALUES
(1, STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435'), 1),
(2, STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430'), 1),
(3, STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435'), 2),
(4, STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430'), 2);         
CREATE CACHED TABLE PUBLIC.ACCOUNT_SUBJECTS(
    ACCOUNT_ID BIGINT NOT NULL,
    SUBJECTS_ID BIGINT NOT NULL
);            
ALTER TABLE PUBLIC.ACCOUNT_SUBJECTS ADD CONSTRAINT PUBLIC.CONSTRAINT_4 PRIMARY KEY(ACCOUNT_ID, SUBJECTS_ID);  
-- 19 +/- SELECT COUNT(*) FROM PUBLIC.ACCOUNT_SUBJECTS;       
INSERT INTO PUBLIC.ACCOUNT_SUBJECTS(ACCOUNT_ID, SUBJECTS_ID) VALUES
(4, 16),
(4, 14),
(4, 10),
(4, 12),
(4, 17),
(4, 18),
(4, 13),
(4, 11),
(4, 15),
(3, 4),
(3, 1),
(3, 7),
(3, 8),
(3, 5),
(3, 6),
(3, 3),
(3, 2),
(3, 9),
(3, 19);      
CREATE CACHED TABLE PUBLIC.CATEGORY(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_988070B5_4263_4F77_8887_4A1B6E2B98F5) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_988070B5_4263_4F77_8887_4A1B6E2B98F5,
    NAME VARCHAR(255),
    TYPE VARCHAR(255)
);    
ALTER TABLE PUBLIC.CATEGORY ADD CONSTRAINT PUBLIC.CONSTRAINT_3 PRIMARY KEY(ID);               
-- 9 +/- SELECT COUNT(*) FROM PUBLIC.CATEGORY;
INSERT INTO PUBLIC.CATEGORY(ID, NAME, TYPE) VALUES
(1, STRINGDECODE('\u0417\u0430\u0440\u043f\u043b\u0430\u0442\u0430'), 'INCOME'),
(2, STRINGDECODE('\u0414\u043e\u043f. \u0437\u0430\u0440\u0430\u0431\u043e\u0442\u043e\u043a'), 'INCOME'),
(3, STRINGDECODE('\u0414\u043e\u043b\u0433\u0438'), 'COST'),
(4, STRINGDECODE('\u041f\u0440\u043e\u0434\u0443\u043a\u0442\u044b'), 'COST'),
(5, STRINGDECODE('\u0410\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u044c'), 'COST'),
(6, STRINGDECODE('\u0423\u0441\u043b\u0443\u0433\u0438'), 'COST'),
(7, STRINGDECODE('\u0422\u043e\u0432\u0430\u0440\u044b'), 'COST'),
(8, STRINGDECODE('\u0422\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442'), 'COST'),
(9, STRINGDECODE('\u041f\u043e\u0434\u0430\u0440\u043a\u0438'), 'COST');            
CREATE CACHED TABLE PUBLIC.CURRENCY(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_585FC9D1_5041_4BD6_9DB4_9AA21F511F70) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_585FC9D1_5041_4BD6_9DB4_9AA21F511F70,
    CODE VARCHAR(255),
    DATE BINARY(255),
    NAME VARCHAR(255),
    RATE DOUBLE,
    SCALE INTEGER
);       
ALTER TABLE PUBLIC.CURRENCY ADD CONSTRAINT PUBLIC.CONSTRAINT_5 PRIMARY KEY(ID);               
-- 27 +/- SELECT COUNT(*) FROM PUBLIC.CURRENCY;               
INSERT INTO PUBLIC.CURRENCY(ID, CODE, DATE, NAME, RATE, SCALE) VALUES
(1, 'BYN', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205190cf278', STRINGDECODE('\u0411\u0435\u043b\u043e\u0440\u0443\u0441\u0441\u043a\u0438\u0439 \u0440\u0443\u0431\u043b\u044c'), 1.0, 1),
(2, 'AUD', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0410\u0432\u0441\u0442\u0440\u0430\u043b\u0438\u0439\u0441\u043a\u0438\u0439 \u0434\u043e\u043b\u043b\u0430\u0440'), 1.5094, 1),
(3, 'BGN', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0411\u043e\u043b\u0433\u0430\u0440\u0441\u043a\u0438\u0439 \u043b\u0435\u0432'), 1.1973, 1),
(4, 'UAH', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0413\u0440\u0438\u0432\u0435\u043d'), 7.6447, 100),
(5, 'DKK', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0414\u0430\u0442\u0441\u043a\u0438\u0445 \u043a\u0440\u043e\u043d'), 3.1433, 10),
(6, 'USD', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0414\u043e\u043b\u043b\u0430\u0440 \u0421\u0428\u0410'), 1.9968, 1),
(7, 'EUR', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0415\u0432\u0440\u043e'), 2.3418, 1),
(8, 'PLN', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0417\u043b\u043e\u0442\u044b\u0445'), 5.4557, 10),
(9, 'IRR', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0418\u0440\u0430\u043d\u0441\u043a\u0438\u0445 \u0440\u0438\u0430\u043b\u043e\u0432'), 4.7441, 100000),
(10, 'ISK', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0418\u0441\u043b\u0430\u043d\u0434\u0441\u043a\u0438\u0445 \u043a\u0440\u043e\u043d'), 1.8912, 100),
(11, 'JPY', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0419\u0435\u043d'), 1.82, 100),
(12, 'CAD', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u041a\u0430\u043d\u0430\u0434\u0441\u043a\u0438\u0439 \u0434\u043e\u043b\u043b\u0430\u0440'), 1.5533, 1),
(13, 'CNY', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u041a\u0438\u0442\u0430\u0439\u0441\u043a\u0438\u0445 \u044e\u0430\u043d\u0435\u0439'), 3.1284, 10),
(14, 'KWD', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u041a\u0443\u0432\u0435\u0439\u0442\u0441\u043a\u0438\u0439 \u0434\u0438\u043d\u0430\u0440'), 6.6077, 1),
(15, 'MDL', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u041c\u043e\u043b\u0434\u0430\u0432\u0441\u043a\u0438\u0445 \u043b\u0435\u0435\u0432'), 1.1865, 10),
(16, 'NZD', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u041d\u043e\u0432\u043e\u0437\u0435\u043b\u0430\u043d\u0434\u0441\u043a\u0438\u0439 \u0434\u043e\u043b\u043b\u0430\u0440'), 1.3807, 1),
(17, 'NOK', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u041d\u043e\u0440\u0432\u0435\u0436\u0441\u043a\u0438\u0445 \u043a\u0440\u043e\u043d'), 2.4708, 10),
(18, 'RUB', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0420\u043e\u0441\u0441\u0438\u0439\u0441\u043a\u0438\u0445 \u0440\u0443\u0431\u043b\u0435\u0439'), 3.2522, 100),
(19, 'XDR', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0421\u0414\u0420 (\u0421\u043f\u0435\u0446\u0438\u0430\u043b\u044c\u043d\u044b\u0435 \u043f\u0440\u0430\u0432\u0430 \u0437\u0430\u0438\u043c\u0441\u0442\u0432\u043e\u0432\u0430\u043d\u0438\u044f)'), 2.8315, 1);        
INSERT INTO PUBLIC.CURRENCY(ID, CODE, DATE, NAME, RATE, SCALE) VALUES
(20, 'SGD', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0421\u0438\u043d\u0433\u0430\u043f\u0443\u0440c\u043a\u0438\u0439 \u0434\u043e\u043b\u043b\u0430\u0440'), 1.4887, 1),
(21, 'KGS', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0421\u043e\u043c\u043e\u0432'), 2.9257, 100),
(22, 'KZT', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0422\u0435\u043d\u0433\u0435'), 6.1192, 1000),
(23, 'TRY', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0422\u0443\u0440\u0435\u0446\u043a\u0438\u0445 \u043b\u0438\u0440'), 4.2717, 10),
(24, 'GBP', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0424\u0443\u043d\u0442 \u0441\u0442\u0435\u0440\u043b\u0438\u043d\u0433\u043e\u0432'), 2.6756, 1),
(25, 'CZK', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0427\u0435\u0448\u0441\u043a\u0438\u0445 \u043a\u0440\u043e\u043d'), 9.0807, 100),
(26, 'SEK', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0428\u0432\u0435\u0434\u0441\u043a\u0438\u0445 \u043a\u0440\u043e\u043d'), 2.2861, 10),
(27, 'CHF', X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770805000007e20519ff78', STRINGDECODE('\u0428\u0432\u0435\u0439\u0446\u0430\u0440\u0441\u043a\u0438\u0439 \u0444\u0440\u0430\u043d\u043a'), 2.0147, 1);       
CREATE CACHED TABLE PUBLIC.LOGS(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_2CCF38A3_9624_4ADE_BE1C_17AE55D0BF8C) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_2CCF38A3_9624_4ADE_BE1C_17AE55D0BF8C,
    DATE BINARY(255),
    INVOKER VARCHAR(255),
    LEVEL VARCHAR(255),
    MESSAGE VARCHAR(1024)
); 
ALTER TABLE PUBLIC.LOGS ADD CONSTRAINT PUBLIC.CONSTRAINT_2 PRIMARY KEY(ID);   
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.LOGS;    
INSERT INTO PUBLIC.LOGS(ID, DATE, INVOKER, LEVEL, MESSAGE) VALUES
(1, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205190cf178', 'admin@localhost', 'INFO', 'Login success: admin@localhost');         
CREATE CACHED TABLE PUBLIC.PLAN(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_BFDCE52E_341E_4F62_AD1B_0BCCBFCD23C4) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_BFDCE52E_341E_4F62_AD1B_0BCCBFCD23C4,
    DATETIME BINARY(255),
    NAME VARCHAR(255),
    VALUE DOUBLE,
    CATEGORY_ID BIGINT,
    SUBJECT_ID BIGINT,
    USER_USER_ID BIGINT
);       
ALTER TABLE PUBLIC.PLAN ADD CONSTRAINT PUBLIC.CONSTRAINT_25 PRIMARY KEY(ID);  
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PLAN;    
CREATE CACHED TABLE PUBLIC.SUBJECT(
    DTYPE VARCHAR(31) NOT NULL,
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_662A682D_5B20_41E3_A452_BAF291308443) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_662A682D_5B20_41E3_A452_BAF291308443,
    DATETIME BINARY(255),
    NAME VARCHAR(255),
    VALUE DOUBLE,
    ACCOUNT_ID BIGINT,
    CATEGORY_ID BIGINT,
    SUBJECT_ID BIGINT,
    USER_USER_ID BIGINT
);           
ALTER TABLE PUBLIC.SUBJECT ADD CONSTRAINT PUBLIC.CONSTRAINT_B PRIMARY KEY(ID);
-- 18 +/- SELECT COUNT(*) FROM PUBLIC.SUBJECT;
INSERT INTO PUBLIC.SUBJECT(DTYPE, ID, DATETIME, NAME, VALUE, ACCOUNT_ID, CATEGORY_ID, SUBJECT_ID, USER_USER_ID) VALUES
('ScientificWork', 1, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205190cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0417\u0430\u0440\u043f\u043b\u0430\u0442\u0430'), 210.0, 3, 1, NULL, NULL),
('ScientificWork', 2, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205170cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0417\u0430\u0440\u043f\u043b\u0430\u0442\u0430'), 213.0, 4, 1, NULL, NULL),
('ScientificWork', 3, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205150cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0414\u043e\u043f. \u0437\u0430\u0440\u0430\u0431\u043e\u0442\u043e\u043a'), 92.0, 3, 2, NULL, NULL),
('ScientificWork', 4, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205130cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0414\u043e\u043f. \u0437\u0430\u0440\u0430\u0431\u043e\u0442\u043e\u043a'), 101.0, 4, 2, NULL, NULL),
('ScientificWork', 5, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205110cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0414\u043e\u043b\u0433\u0438'), 88.0, 3, 3, NULL, NULL),
('ScientificWork', 6, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e2050f0cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0414\u043e\u043b\u0433\u0438'), 245.0, 4, 3, NULL, NULL),
('ScientificWork', 7, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e2050d0cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u041f\u0440\u043e\u0434\u0443\u043a\u0442\u044b'), 129.0, 3, 4, NULL, NULL),
('ScientificWork', 8, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e2050b0cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u041f\u0440\u043e\u0434\u0443\u043a\u0442\u044b'), 160.0, 4, 4, NULL, NULL),
('ScientificWork', 9, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205090cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0410\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u044c'), 219.0, 3, 5, NULL, NULL),
('ScientificWork', 10, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205070cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0410\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u044c'), 209.0, 4, 5, NULL, NULL),
('ScientificWork', 11, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205050cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0423\u0441\u043b\u0443\u0433\u0438'), 208.0, 3, 6, NULL, NULL),
('ScientificWork', 12, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205030cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0423\u0441\u043b\u0443\u0433\u0438'), 70.0, 4, 6, NULL, NULL),
('ScientificWork', 13, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e205010cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0422\u043e\u0432\u0430\u0440\u044b'), 195.0, 3, 7, NULL, NULL),
('ScientificWork', 14, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e2041d0cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0422\u043e\u0432\u0430\u0440\u044b'), 177.0, 4, 7, NULL, NULL),
('ScientificWork', 15, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e2041b0cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u0422\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442'), 89.0, 3, 8, NULL, NULL),
('ScientificWork', 16, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e204190cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u0422\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442'), 70.0, 4, 8, NULL, NULL);
INSERT INTO PUBLIC.SUBJECT(DTYPE, ID, DATETIME, NAME, VALUE, ACCOUNT_ID, CATEGORY_ID, SUBJECT_ID, USER_USER_ID) VALUES
('ScientificWork', 17, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e204170cf278', STRINGDECODE('\u041d\u0430\u043b\u0438\u0447\u043d\u044b\u0435, \u041f\u043e\u0434\u0430\u0440\u043a\u0438'), 175.0, 3, 9, NULL, NULL),
('ScientificWork', 18, X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770905000007e204150cf278', STRINGDECODE('\u041a\u0440\u0435\u0434\u0438\u0442\u043a\u0430, \u041f\u043e\u0434\u0430\u0440\u043a\u0438'), 121.0, 4, 9, NULL, NULL);
CREATE CACHED TABLE PUBLIC.USER_ROLES(
    USER_USER_ID BIGINT NOT NULL,
    ROLES VARCHAR(255)
);         
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.USER_ROLES;              
INSERT INTO PUBLIC.USER_ROLES(USER_USER_ID, ROLES) VALUES
(1, 'ADMIN'),
(2, 'USER');        
CREATE CACHED TABLE PUBLIC.USERS(
    USER_ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_72059A3D_40B3_4A05_9A8A_B0AD572F0056) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_72059A3D_40B3_4A05_9A8A_B0AD572F0056,
    ACTIVE BOOLEAN,
    EMAIL VARCHAR(255) NOT NULL,
    NAME VARCHAR(255),
    PASSWORD VARCHAR(255) NOT NULL
);              
ALTER TABLE PUBLIC.USERS ADD CONSTRAINT PUBLIC.CONSTRAINT_4D PRIMARY KEY(USER_ID);            
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.USERS;   
INSERT INTO PUBLIC.USERS(USER_ID, ACTIVE, EMAIL, NAME, PASSWORD) VALUES
(1, TRUE, 'admin@localhost', 'ADMIN', '$2a$10$Xem9ADc1KcsqOZRrd07Ape2ySICJBIWgUD7XxEqx3I3M4KVo2R9vO'),
(2, TRUE, 'lecture@localhost', 'Test lecture', '$2a$10$xXaXI8THDHSr1MReIB.ytO82mgWGDFvOe8VAKaWbbIJPb.M2tCzjq');
CREATE CACHED TABLE PUBLIC.USERS_ACCOUNTS(
    USER_USER_ID BIGINT NOT NULL,
    ACCOUNTS_ID BIGINT NOT NULL
);            
ALTER TABLE PUBLIC.USERS_ACCOUNTS ADD CONSTRAINT PUBLIC.CONSTRAINT_F PRIMARY KEY(USER_USER_ID, ACCOUNTS_ID);  
-- 4 +/- SELECT COUNT(*) FROM PUBLIC.USERS_ACCOUNTS;          
INSERT INTO PUBLIC.USERS_ACCOUNTS(USER_USER_ID, ACCOUNTS_ID) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4);       
ALTER TABLE PUBLIC.ACCOUNT_SUBJECTS ADD CONSTRAINT PUBLIC.UK_4TRJFNB7FCJM13PQ2SX88DH76 UNIQUE(SUBJECTS_ID);   
ALTER TABLE PUBLIC.USERS ADD CONSTRAINT PUBLIC.UK_6DOTKOTT2KJSP8VW4D0M25FB7 UNIQUE(EMAIL);    
ALTER TABLE PUBLIC.USERS_ACCOUNTS ADD CONSTRAINT PUBLIC.UK_NBNVIGJ13OD728SOEBNNV9FKS UNIQUE(ACCOUNTS_ID);     
ALTER TABLE PUBLIC.ACCOUNT ADD CONSTRAINT PUBLIC.FKRICFHCW1VU2SV6F58OFLQSGL9 FOREIGN KEY(USER_USER_ID) REFERENCES PUBLIC.USERS(USER_ID) NOCHECK;              
ALTER TABLE PUBLIC.SUBJECT ADD CONSTRAINT PUBLIC.FKDLEXHUJQM18EMNOO8L7MUUFED FOREIGN KEY(SUBJECT_ID) REFERENCES PUBLIC.SUBJECT(ID) NOCHECK;   
ALTER TABLE PUBLIC.SUBJECT ADD CONSTRAINT PUBLIC.FKQFNV6J3CQE07YNY9U2V12GLHK FOREIGN KEY(CATEGORY_ID) REFERENCES PUBLIC.CATEGORY(ID) NOCHECK; 
ALTER TABLE PUBLIC.USER_ROLES ADD CONSTRAINT PUBLIC.FK5GIKIW021W6Y16A8T5VJWQWYJ FOREIGN KEY(USER_USER_ID) REFERENCES PUBLIC.USERS(USER_ID) NOCHECK;           
ALTER TABLE PUBLIC.SUBJECT ADD CONSTRAINT PUBLIC.FKK2YIICW6J2LNS80U3M6NQNT9R FOREIGN KEY(USER_USER_ID) REFERENCES PUBLIC.USERS(USER_ID) NOCHECK;              
ALTER TABLE PUBLIC.USERS_ACCOUNTS ADD CONSTRAINT PUBLIC.FKNVKJ4808N0M0DLY240KNGEG02 FOREIGN KEY(USER_USER_ID) REFERENCES PUBLIC.USERS(USER_ID) NOCHECK;       
ALTER TABLE PUBLIC.USERS_ACCOUNTS ADD CONSTRAINT PUBLIC.FKGOACPUA1VIGNRE8J8ARQ9N9B6 FOREIGN KEY(ACCOUNTS_ID) REFERENCES PUBLIC.ACCOUNT(ID) NOCHECK;           
ALTER TABLE PUBLIC.SUBJECT ADD CONSTRAINT PUBLIC.FK2EJR1LESPR8AP73J0SSQ86SH9 FOREIGN KEY(ACCOUNT_ID) REFERENCES PUBLIC.ACCOUNT(ID) NOCHECK;   
