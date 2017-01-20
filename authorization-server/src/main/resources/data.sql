insert into users (
  username,
  password,
  enabled) values ('dave','secret',true);
 
insert into authorities(username, authority) values ('dave','USER');
insert into authorities(username, authority) values ('dave','TRUSTED');
