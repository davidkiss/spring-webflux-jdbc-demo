# Create Postgres DB
```$bash
psql -d postgres
```

then
```$sql
create database webflux_demo;
create user sa;
grant all privileges on database webflux_demo to sa;
```