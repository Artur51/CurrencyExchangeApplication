INSERT INTO user_registration_data VALUES (1, 'password','username');

INSERT INTO currency_exchange_time_and_rates VALUES ('2020-01-01');

INSERT INTO currency_exchange_rate VALUES (0, 'USD','1.1','2020-01-01'),
                                          (1, 'JPY','1.2','2020-01-01'),
                                          (2, 'AUD','1.4','2020-01-01'),
                                          (3, 'RUB','1.3','2020-01-01');
										  
INSERT INTO exchange_currency_log_event VALUES
 (0,'2020-01-01 00:00:01', '10.1','1.1',0, 2,1),
 (1,'2020-01-02 00:00:01', '10.1','1.1',0, 0,1),
 (2,'2020-01-03 00:00:01', '10.1','1.1',0, 3,1),
 (3,'2020-01-04 00:00:01', '10.1','1.1',3, 2,1),
 (4,'2020-01-05 00:00:01', '10.1','1.1',2, 3,1),
 (5,'2020-01-06 00:00:01', '10.1','1.1',1, 2,1),
 (6,'2020-01-07 00:00:01', '10.1','1.1',0, 2,1),
 (7,'2020-01-08 00:00:01', '10.1','1.1',2, 1,1),
 (8,'2020-01-09 00:00:01', '10.1','1.1',0, 2,1),
 (9,'2020-01-10 00:00:01', '10.1','1.1',3, 2,1),
 (10,'2020-01-11 00:00:01', '10.1','1.1',0, 2,1),
 (11,'2020-01-12 00:00:01', '10.1','1.1',1, 2,1);