drop schema if exists myapp cascade;

create schema myapp;

create table myapp.sample (
  x float8 not null,
  created_at timestamptz default current_timestamp
);

