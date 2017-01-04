insert into users (username, password, enabled) values ('erick','erick123',true);
insert into authorities (username, authority) values ('erick','TRUSTED_CLIENT');
insert into oauth_client_details(client_id,
								 resource_ids,
								 client_secret,
								 scope,
								 authorized_grant_types,
								 web_server_redirect_uri,
								 authorities,
								 access_token_validity,
								 refresh_token_validity,
								 additional_information,
								 autoapprove)
						values ('my-trusted-client',
						        'oauth2-resource',
						        '123',
						        'read',
						        'password',
						        null,
						        'CLIENT',
						        '60',
						        '120',
						        null,
						        null);