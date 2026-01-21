-- liquibase formatted sql

-- changeset resource-planner-demo:7
-- Extended Seed Data for Resource Planner Demo
-- ============================================================================

-- Sample Roles
INSERT INTO roles (id, name) VALUES
(1, 'admin'),
(2, 'manager'),
(3, 'staff'),
(4, 'customer');

-- Sample Users
INSERT INTO users (id, username, password_hash, roles) VALUES
(1, 'max@mustermann.de', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'admin'),
(2, 'manager@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'manager'),
(3, 'staff1@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'staff'),
(4, 'staff2@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'staff');

-- Assign Roles to Users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3);

-- Sample Rooms
INSERT INTO room (name, capacity, description, is_active, working_hours_start, working_hours_end) VALUES
('Conference Room A', 10, 'Large conference room with projector and whiteboard', TRUE, '08:00', '18:00'),
('Conference Room B', 6, 'Medium-sized meeting room', TRUE, '08:00', '18:00'),
('Executive Boardroom', 12, 'Premium boardroom with video conferencing', TRUE, '07:00', '20:00'),
('Training Room', 20, 'Large training room with workstations', TRUE, '09:00', '17:00'),
('Small Meeting Room 1', 4, 'Intimate meeting space', TRUE, '08:00', '18:00'),
('Small Meeting Room 2', 4, 'Cozy meeting room', TRUE, '08:00', '18:00'),
('Consultation Room 1', 2, 'Private consultation space', TRUE, '09:00', '17:00'),
('Consultation Room 2', 2, 'Private consultation space', TRUE, '09:00', '17:00'),
('Presentation Hall', 50, 'Large hall for presentations and events', TRUE, '08:00', '22:00'),
('Breakout Room', 8, 'Collaborative workspace', TRUE, '08:00', '18:00');

-- Sample People (Staff/Resources)
INSERT INTO person (name, email, title, is_active, working_hours_start, working_hours_end) VALUES
('Dr. Sarah Johnson', 'sarah.johnson@example.com', 'Senior Consultant', TRUE, '09:00', '17:00'),
('Dr. Michael Chen', 'michael.chen@example.com', 'Medical Specialist', TRUE, '08:00', '16:00'),
('Emily Rodriguez', 'emily.rodriguez@example.com', 'Business Advisor', TRUE, '10:00', '18:00'),
('James Wilson', 'james.wilson@example.com', 'Legal Consultant', TRUE, '09:00', '17:00'),
('Lisa Anderson', 'lisa.anderson@example.com', 'Financial Advisor', TRUE, '08:30', '16:30'),
('David Thompson', 'david.thompson@example.com', 'Technical Specialist', TRUE, '09:00', '17:00'),
('Maria Garcia', 'maria.garcia@example.com', 'HR Consultant', TRUE, '09:00', '17:00'),
('Robert Brown', 'robert.brown@example.com', 'Marketing Consultant', TRUE, '10:00', '18:00'),
('Jennifer Lee', 'jennifer.lee@example.com', 'Project Manager', TRUE, '08:00', '16:00'),
('William Taylor', 'william.taylor@example.com', 'Senior Trainer', TRUE, '09:00', '17:00');

-- Sample Appointment Types
INSERT INTO appointment_type (name, description, duration_minutes, price, requires_room, is_active) VALUES
('Initial Consultation', 'First-time consultation meeting', 60, 150.00, TRUE, TRUE),
('Follow-up Meeting', 'Follow-up consultation session', 30, 75.00, TRUE, TRUE),
('Extended Session', 'Extended consultation or training session', 120, 280.00, TRUE, TRUE),
('Quick Check-in', 'Brief status update or check-in', 15, 35.00, FALSE, TRUE),
('Workshop', 'Group workshop or training', 180, 500.00, TRUE, TRUE),
('Team Meeting', 'Internal team collaboration session', 60, 0.00, TRUE, TRUE),
('Client Presentation', 'Formal client presentation', 90, 200.00, TRUE, TRUE),
('Strategy Session', 'Strategic planning meeting', 120, 350.00, TRUE, TRUE),
('Training Course', 'Half-day training course', 240, 800.00, TRUE, TRUE),
('One-on-One Coaching', 'Individual coaching session', 45, 120.00, TRUE, TRUE);

-- Link People to Appointment Types
INSERT INTO person_appointment_type (person_id, appointment_type_id) VALUES
-- Dr. Sarah Johnson
(1, 1), (1, 2), (1, 3), (1, 10),
-- Dr. Michael Chen
(2, 1), (2, 2), (2, 3),
-- Emily Rodriguez
(3, 1), (3, 2), (3, 7), (3, 8),
-- James Wilson
(4, 1), (4, 2), (4, 3),
-- Lisa Anderson
(5, 1), (5, 2), (5, 8), (5, 10),
-- David Thompson
(6, 5), (6, 9), (6, 10),
-- Maria Garcia
(7, 1), (7, 2), (7, 6), (7, 10),
-- Robert Brown
(8, 1), (8, 2), (8, 7), (8, 8),
-- Jennifer Lee
(9, 6), (9, 8), (9, 10),
-- William Taylor
(10, 5), (10, 9), (10, 10);

-- Sample Appointments (varying dates and times)
INSERT INTO appointment (start_time, end_time, appointment_type_id, room_id, person_id, customer_name, customer_email, status, created_by_user_id, user_agreement_accepted) VALUES
-- Week 1 - January 2025
('2025-01-20 09:00:00', '2025-01-20 10:00:00', 1, 1, 1, 'John Smith', 'john.smith@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-20 10:30:00', '2025-01-20 11:00:00', 2, 2, 1, 'Mary Johnson', 'mary.j@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-20 14:00:00', '2025-01-20 15:00:00', 1, 1, 2, 'Robert Wilson', 'r.wilson@example.com', 'CONFIRMED', 2, TRUE),
('2025-01-21 09:00:00', '2025-01-21 11:00:00', 3, 3, 3, 'Linda Brown', 'linda.b@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-21 13:00:00', '2025-01-21 14:00:00', 1, 5, 4, 'David Martinez', 'd.martinez@example.com', 'CONFIRMED', 2, TRUE),
('2025-01-22 10:00:00', '2025-01-22 10:30:00', 2, 7, 2, 'Susan Anderson', 'susan.a@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-22 14:30:00', '2025-01-22 16:30:00', 3, 1, 5, 'James Taylor', 'j.taylor@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-23 09:00:00', '2025-01-23 12:00:00', 5, 4, 10, 'ABC Corporation', 'training@abc.com', 'CONFIRMED', 2, TRUE),
('2025-01-23 15:00:00', '2025-01-23 16:00:00', 1, 2, 6, 'Michael Lee', 'mlee@example.com', 'CONFIRMED', 1, TRUE),

-- Week 2
('2025-01-27 09:30:00', '2025-01-27 10:30:00', 1, 1, 1, 'Patricia Davis', 'p.davis@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-27 11:00:00', '2025-01-27 12:00:00', 1, 2, 3, 'Christopher White', 'c.white@example.com', 'CONFIRMED', 2, TRUE),
('2025-01-27 14:00:00', '2025-01-27 15:30:00', 7, 3, 8, 'Tech Startup Inc', 'contact@techstartup.com', 'CONFIRMED', 1, TRUE),
('2025-01-28 09:00:00', '2025-01-28 10:00:00', 1, 5, 7, 'Elizabeth Harris', 'e.harris@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-28 13:00:00', '2025-01-28 14:00:00', 6, 10, 9, 'Internal Team Meeting', 'team@example.com', 'CONFIRMED', 2, TRUE),
('2025-01-29 10:00:00', '2025-01-29 10:45:00', 10, 7, 1, 'Thomas Clark', 't.clark@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-29 14:00:00', '2025-01-29 16:00:00', 8, 3, 5, 'Global Investments Ltd', 'strategy@global.com', 'CONFIRMED', 1, TRUE),
('2025-01-30 09:00:00', '2025-01-30 10:00:00', 1, 1, 2, 'Barbara Lewis', 'b.lewis@example.com', 'CONFIRMED', 2, TRUE),
('2025-01-30 11:00:00', '2025-01-30 12:00:00', 1, 2, 4, 'Richard Walker', 'r.walker@example.com', 'CONFIRMED', 1, TRUE),
('2025-01-30 15:00:00', '2025-01-30 18:00:00', 5, 4, 6, 'Developer Workshop', 'dev@company.com', 'CONFIRMED', 2, TRUE),

-- Week 3
('2025-02-03 09:00:00', '2025-02-03 10:00:00', 1, 1, 1, 'Nancy Hall', 'n.hall@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-03 14:00:00', '2025-02-03 14:30:00', 2, 7, 2, 'Kevin Allen', 'k.allen@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-04 10:00:00', '2025-02-04 11:30:00', 7, 3, 3, 'Marketing Solutions', 'sales@marketing.com', 'CONFIRMED', 2, TRUE),
('2025-02-04 13:00:00', '2025-02-04 15:00:00', 3, 1, 4, 'Steven Young', 's.young@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-05 09:00:00', '2025-02-05 13:00:00', 9, 4, 10, 'Leadership Training', 'hr@company.com', 'CONFIRMED', 2, TRUE),
('2025-02-05 14:30:00', '2025-02-05 15:30:00', 1, 2, 5, 'Carol King', 'c.king@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-06 09:30:00', '2025-02-06 10:30:00', 1, 5, 6, 'Daniel Wright', 'd.wright@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-06 11:00:00', '2025-02-06 12:00:00', 6, 10, 9, 'Project Sync Meeting', 'team@example.com', 'CONFIRMED', 2, TRUE),
('2025-02-06 15:00:00', '2025-02-06 16:00:00', 1, 1, 7, 'Michelle Lopez', 'm.lopez@example.com', 'CONFIRMED', 1, TRUE),

-- Future appointments (pending/upcoming)
('2025-02-10 09:00:00', '2025-02-10 10:00:00', 1, 1, 1, 'George Hill', 'g.hill@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-10 14:00:00', '2025-02-10 15:30:00', 7, 3, 8, 'Enterprise Corp', 'meeting@enterprise.com', 'PENDING', 1, FALSE),
('2025-02-11 10:00:00', '2025-02-11 11:00:00', 1, 2, 3, 'Ashley Scott', 'a.scott@example.com', 'PENDING', 2, FALSE),
('2025-02-12 13:00:00', '2025-02-12 14:45:00', 10, 7, 5, 'Brian Green', 'b.green@example.com', 'CONFIRMED', 1, TRUE),
('2025-02-13 09:00:00', '2025-02-13 10:00:00', 1, 1, 2, 'Melissa Adams', 'm.adams@example.com', 'PENDING', 1, FALSE),
('2025-02-13 14:00:00', '2025-02-13 18:00:00', 9, 4, 10, 'Advanced Workshop', 'training@company.com', 'CONFIRMED', 2, TRUE),

-- Some cancelled appointments
('2025-01-24 10:00:00', '2025-01-24 11:00:00', 1, 1, 1, 'Cancelled Customer', 'cancelled@example.com', 'CANCELLED', 1, TRUE),
('2025-01-31 14:00:00', '2025-01-31 15:00:00', 1, 2, 3, 'No Show Client', 'noshow@example.com', 'CANCELLED', 1, TRUE);

-- Sample Email Templates
INSERT INTO email_templates (name, subject, body) VALUES
('Appointment Confirmation', 'Appointment Confirmed - {{date}} at {{time}}',
'Dear {{customer_name}},

Your appointment has been confirmed for {{date}} at {{time}} with {{person_name}}.

Appointment Details:
- Type: {{appointment_type}}
- Location: {{room_name}}
- Duration: {{duration}} minutes

Please arrive 5 minutes before your scheduled time.

If you need to reschedule or cancel, please contact us at least 24 hours in advance.

Best regards,
Resource Planning Team'),

('Appointment Reminder', 'Reminder: Appointment Tomorrow - {{date}} at {{time}}',
'Dear {{customer_name}},

This is a reminder about your upcoming appointment scheduled for {{date}} at {{time}}.

Appointment Details:
- Person: {{person_name}}
- Location: {{room_name}}
- Duration: {{duration}} minutes

See you soon!

Best regards,
Resource Planning Team'),

('Appointment Cancellation', 'Appointment Cancelled - {{date}} at {{time}}',
'Dear {{customer_name}},

Your appointment scheduled for {{date}} at {{time}} has been cancelled.

If you would like to reschedule, please contact us at your earliest convenience.

Best regards,
Resource Planning Team'),

('Welcome Email', 'Welcome to Our Resource Planning System',
'Dear {{name}},

Welcome to our resource planning system! Your account has been successfully created.

You can now:
- Book appointments online
- View your appointment history
- Manage your profile

Thank you for choosing our services!

Best regards,
Resource Planning Team');

-- Update settings with default email template
UPDATE settings SET default_email_template_id = 1 WHERE id = 1;

-- Sample Notifications
INSERT INTO notification (user_id, title, message, link, is_read) VALUES
(1, 'New Appointment Booked', 'John Smith booked an Initial Consultation for Jan 20, 2025 at 9:00 AM', '/appointments/1', 1),
(1, 'Appointment Starting Soon', 'Your appointment with Mary Johnson starts in 30 minutes', '/appointments/2', 0),
(2, 'Appointment Cancelled', 'Cancelled Customer has cancelled their appointment', '/appointments/36', 1),
(2, 'New Appointment Request', 'Enterprise Corp requested an appointment for Feb 10, 2025', '/appointments/31', 0),
(1, 'Daily Schedule', 'You have 3 appointments scheduled for today', '/schedule', 1),
(2, 'Weekly Report', 'Your weekly appointment summary is ready', '/reports', 0),
(1, 'Room Conflict Warning', 'Potential room scheduling conflict detected for Conference Room A', '/rooms/1', 0),
(3, 'Appointment Reminder', 'Remember your appointment with ABC Corporation tomorrow', '/appointments/8', 0);
