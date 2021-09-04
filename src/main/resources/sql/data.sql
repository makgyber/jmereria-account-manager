insert into CUSTOMER
    select * from (
        select 10001,'John Doe', 'abcdef-1234-567890'
    ) x where not exists(select id from CUSTOMER where id = 10001);
insert into ACCOUNT
    select * from (
        select 1,'12345678', 1000000,'HKD', 10001 union
        select 2,'88888888', 1000000,'HKD', 10001
    ) x where not exists(select id from ACCOUNT where id in (1,2));