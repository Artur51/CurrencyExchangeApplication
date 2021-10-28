    drop table if exists currency_exchange_rate CASCADE;
    drop table if exists currency_exchange_time_and_rates CASCADE;
    drop table if exists exchange_currency_log_event CASCADE;
    drop table if exists user_registration_data CASCADE;
    
    drop sequence if exists hibernate_sequence;
    create sequence hibernate_sequence start with 1 increment by 1;
    
    create table user_registration_data (
        id bigint not null,
        password varchar(100) not null,
        username varchar(100) not null unique,
        primary key (id)
    );
    
    create table currency_exchange_time_and_rates (
        time date not null,
        primary key (time)
    );

    create table currency_exchange_rate (
        id integer not null,
        currency varchar(10),
        rate decimal(19,5),
        parent_id date not null,
        primary key (id),
        
        foreign key (parent_id) 
       	references currency_exchange_time_and_rates
    );
    
    
    create table exchange_currency_log_event (
        id integer not null,
        created_at timestamp not null,
        currency_amount_received decimal(19,5),
        currency_amount_sold decimal(19,5),
        currency_purchase_id integer,
        currency_sold_id integer,
        user_id bigint,
        primary key (id),
        
        foreign key (user_id) 
        references user_registration_data,
        
        foreign key (currency_purchase_id) 
        references currency_exchange_rate,      
       
        foreign key (currency_sold_id) 
        references currency_exchange_rate
      
    );
