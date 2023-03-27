INSERT INTO public.roles (uuid, name) VALUES ('07a3fd37-3dd3-42c9-862a-4653397d862f', 'ROLE_ADMIN');
INSERT INTO public.roles (uuid, name) VALUES ('93132a67-85a8-46f6-8136-713734da36a3', 'ROLE_TECHNICAL');
INSERT INTO public.users (uuid, create_date, name, password, user_name, team_uuid) VALUES ('8b080999-05e9-4460-b1f7-c9fa1cc353b8', '2023-03-22 22:24:10.298000', 'su', '$2a$10$YkfyPb.tnt1ivUL/jvmwYOu3UkiLkpXPz7mjmPctBCrvnFmImeLkm', 'admin', null);
INSERT INTO public.users_roles (users_uuid, roles_uuid) VALUES ('8b080999-05e9-4460-b1f7-c9fa1cc353b8', '07a3fd37-3dd3-42c9-862a-4653397d862f');

