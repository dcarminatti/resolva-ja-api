insert into users (id, name, email, password, created_at, updated_at)
values (1, 'John Doe', 'admin@email.com', '$2a$10$/6P29uLM.HzTpGwkraMppeJd59RgPm1ykEvSUPJS2dergYsoFRSRa', now(), now()),
       (2, 'Jane Smith', 'technician@email.com', '$2a$10$/6P29uLM.HzTpGwkraMppeJd59RgPm1ykEvSUPJS2dergYsoFRSRa', now(), now()),
       (3, 'Alice Johnson', 'user@email.com', '$2a$10$/6P29uLM.HzTpGwkraMppeJd59RgPm1ykEvSUPJS2dergYsoFRSRa', now(), now());

insert into administrator (id)
values (1);

insert into technician (id, specialty)
values (2, 'Network');

alter sequence users_id_seq restart with 3;

insert into sla (id, description, response_time_hours, resolution_time_hours)
values (1, 'Standard service level agreement', 4, 24);

alter sequence sla_id_seq restart with 2;

insert into category (id, name, description, sla_id)
values (1, 'Software', 'Issues related to software applications', 1);

alter sequence category_id_seq restart with 2;

insert into ticket (id, title, description, status, priority, creation_date, deadline, user_id, technician_id, category_id)
values (1, 'Issue with login', 'Unable to log in to the system', 'STARTED', 'LOW', now(), now() + interval '7 days', 1, 2, 1);

alter sequence ticket_id_seq restart with 2;

insert into ticket_comment (id, comment, user_id, ticket_id)
values (1, 'Initial comment on ticket 1', 1, 1);

alter sequence ticket_comment_id_seq restart with 2;

insert into ticket_feedback (id, rating, comment, date, ticket_id)
values (1, 5, 'Great support!', now(), 1);

alter sequence ticket_feedback_id_seq restart with 2;

insert into ticket_history (id, date, previous_status, current_status, note, ticket_id)
values (1, now(), 'NEW', 'STARTED', 'Ticket created and assigned to technician', 1);

alter sequence ticket_history_id_seq restart with 2;

insert into notification (id, user_id, ticket_id, type, message, send_date)
values (1, 1, 1, 'NEW_TICKET', 'A new ticket has been created', now());

alter sequence notification_id_seq restart with 2;

insert into department (id, name, location)
values (1, 'IT Support', 'Main Office');

alter sequence department_id_seq restart with 2;