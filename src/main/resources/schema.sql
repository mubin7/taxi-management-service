create table booking (destinationxposition float(53), destinationyposition float(53), sourcexposition float(53), sourceyposition float(53), journey_end_time timestamp(6), journey_start_time timestamp(6), booking_id varchar(255) not null, journey_status varchar(255) check (journey_status in ('IN_PROGRESS','COMPLETED')), taxi_id varchar(255), primary key (booking_id));

create table taxi (x_position float(53), y_position float(53), taxi_id varchar(255) not null, taxi_status varchar(255) check (taxi_status in ('AVAILABLE','NOT_AVAILABLE','BOOKED')), primary key (taxi_id));

alter table if exists booking add constraint FKb9pxgdcu5is3umpn5m4uyq0ls foreign key (taxi_id) references taxi;