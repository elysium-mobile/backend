INSERT INTO users (id, name, last_name, phone_number, dni, created_at, updated_at)
VALUES
    (1, 'Carlos',   'Ramirez',   '987654321', '12345678', NOW(), NOW()),
    (2, 'Maria',    'Lopez',     '912345678', '23456789', NOW(), NOW()),
    (3, 'Jorge',    'Quispe',    '956781234', '34567890', NOW(), NOW()),
    (4, 'Ana',      'Torres',    '945678123', '45678901', NOW(), NOW()),
    (5, 'Luis',     'Mamani',    '934567812', '56789012', NOW(), NOW()),
    (6, 'Sofia',    'Vargas',    '923456781', '67890123', NOW(), NOW()),
    (7, 'Diego',    'Chavez',    '998877665', '78901234', NOW(), NOW()),
    (8, 'Valeria',  'Mendoza',   '911223344', '89012345', NOW(), NOW());

INSERT INTO companies (id, name, ruc, contact_email, contact_phone, created_at, updated_at)
VALUES
    (1, 'TechCorp SAC',      '20123456781', 'contacto@techcorp.pe',   '014567890', NOW(), NOW()),
    (2, 'InnovatePeru SRL',  '20987654322', 'info@innovateperu.com',  '016789012', NOW(), NOW());

INSERT INTO memberships (id, membership_start, membership_over, membership_status, created_at, updated_at)
VALUES
    (1, '2024-01-01', '2025-01-01', 'ACTIVE',  NOW(), NOW()),
    (2, '2024-03-01', '2025-03-01', 'ACTIVE',  NOW(), NOW()),
    (3, '2024-06-01', '2024-12-01', 'PENDING', NOW(), NOW());

INSERT INTO user_accounts (id, user_id, email, password, anonymous_name, membership_id, company_id, created_at, updated_at)
VALUES
    (1, 1, 'carlos.ramirez@techcorp.pe',    '$2b$10$Cdx/6aA/qHcMsdNDWOxVe.Ms9aOd4Hdy7N8mqk.ycSulmcJQgDFZu', 'CRamz',   1, 1, NOW(), NOW()),
    (2, 2, 'maria.lopez@techcorp.pe',       '$2b$10$k81EGalrWpc.koG5YdHesOjWCw5hs2M3b7BsJeil01DKbJyh9wC.C', 'MariLop', 1, 1, NOW(), NOW()),
    (3, 3, 'jorge.quispe@techcorp.pe',      '$2b$10$jubFX21ewEIZEfz7APJoqOImjEMFFWKZ3xGuMtML.Nzyd.K.0mjVO', 'JQuispe', 1, 1, NOW(), NOW()),
    (4, 4, 'ana.torres@innovateperu.com',   '$2b$10$SIKqLgLUnBOU1PKAEbFrw.tYfRBbF4InXig0U6pbR4fszp4vC1ppi', 'AnaTor',  2, 2, NOW(), NOW()),
    (5, 5, 'luis.mamani@innovateperu.com',  '$2b$10$Zoq/MW1r2tjqs0h0CU5/ZeYJlnFt/P6GKCB6Bp1v8KrmJFnnybvme', 'LuisMam', 2, 2, NOW(), NOW()),
    (6, 6, 'sofia.vargas@innovateperu.com', '$2b$10$DHJ.WSyOHmpQhdb18AHjhOW1Grd2/9Bj1Wk0c0yQS19B8WrxUVoPm', 'SofiVar', 2, 2, NOW(), NOW()),
    (7, 7, 'diego.chavez@techcorp.pe',      '$2b$10$p4E/MlKYu7UEfAzS2rWWwuu/V.3x2FhBMYneeQTXxHU7agepT7/Mu', 'DiegoC',  1, 1, NOW(), NOW()),
    (8, 8, 'valeria.mendoza@techcorp.pe',   '$2b$10$Pw013KINvswjsdyz9d1uTuoJ2wcpdZUg9CyV3iDNMlsLRhfEbbtYK', 'ValMen',  3, 1, NOW(), NOW());

INSERT INTO companies_employees (company_id, employees_id)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 7), (1, 8),
    (2, 4), (2, 5), (2, 6);
ON CONFLICT (id) DO NOTHING;

INSERT INTO employee_profiles (id, date_start, position, salary, work_of_team_id, user_account_id, created_at, updated_at)
VALUES
    (1, '2022-03-01', 'Backend Developer',  4500, 1, 1, NOW(), NOW()),
    (2, '2021-07-15', 'Frontend Developer', 4000, 1, 2, NOW(), NOW()),
    (3, '2023-01-10', 'QA Engineer',        3500, 2, 3, NOW(), NOW()),
    (4, '2020-05-20', 'Product Manager',    6000, 3, 4, NOW(), NOW()),
    (5, '2022-11-01', 'Data Analyst',       4200, 3, 5, NOW(), NOW()),
    (6, '2023-06-01', 'DevOps Engineer',    5000, 2, 6, NOW(), NOW()),
    (7, '2021-09-01', 'Tech Lead',          7000, 1, 7, NOW(), NOW()),
    (8, '2024-01-15', 'Junior Developer',   2800, 1, 8, NOW(), NOW());


INSERT INTO rrhh_profiles (id, rrhh_department, status_hierarchy, user_account_id, created_at, updated_at)
VALUES
    (1, 'Recursos Humanos', 'Senior HR Manager', 1, NOW(), NOW()),
    (2, 'Recursos Humanos', 'HR Coordinator',    4, NOW(), NOW());


INSERT INTO dashboards (id, ruc, company_id, title, description, created_at, updated_at)
VALUES
    (1, '20123456781', 1, 'Dashboard TechCorp',     'Panel principal de métricas de TechCorp SAC',     NOW(), NOW()),
    (2, '20987654322', 2, 'Dashboard InnovatePeru', 'Panel principal de métricas de InnovatePeru SRL', NOW(), NOW());


INSERT INTO widgets (id, title, refresh_period, dashboard_id, created_at, updated_at)
VALUES
    (1, 'Productividad del Equipo', 30, 1, NOW(), NOW()),
    (2, 'Tickets Pendientes',       15, 1, NOW(), NOW()),
    (3, 'Ventas del Mes',           60, 2, NOW(), NOW()),
    (4, 'NPS Score',                60, 2, NOW(), NOW());


INSERT INTO dashboards_widgets (dashboard_id, widgets_id)
VALUES
    (1, 1), (1, 2),
    (2, 3), (2, 4);


INSERT INTO area_companies (id, name, annual_budget, company_id, created_at, updated_at)
VALUES
    (1, 'Desarrollo de Software', 200000, 1, NOW(), NOW()),
    (2, 'Control de Calidad',     80000,  1, NOW(), NOW()),
    (3, 'Producto e Innovación',  150000, 2, NOW(), NOW()),
    (4, 'Data & Analytics',       120000, 2, NOW(), NOW());


INSERT INTO companies_area_company_lists (company_id, area_company_list_id)
VALUES
    (1, 1), (1, 2),
    (2, 3), (2, 4);


INSERT INTO unit_of_works (id, name, created_at, updated_at)
VALUES
    (1, 'Sprint Backend Q1',  NOW(), NOW()),
    (2, 'Sprint Frontend Q1', NOW(), NOW()),
    (3, 'Sprint QA Q1',       NOW(), NOW()),
    (4, 'Roadmap Producto',   NOW(), NOW()),
    (5, 'Pipeline Analytics', NOW(), NOW());


INSERT INTO area_company_unit_of_work_lists (area_company_id, unit_of_work_list_id)
VALUES
    (1, 1), (1, 2),
    (2, 3),
    (3, 4),
    (4, 5);


INSERT INTO work_teams (id, team_name, leader_of_team, unit_of_work_id, created_at, updated_at)
VALUES
    (1, 'Team Alpha',   'Carlos Ramirez', 1, NOW(), NOW()),
    (2, 'Team Beta',    'Maria Lopez',    2, NOW(), NOW()),
    (3, 'Team QA',      'Jorge Quispe',   3, NOW(), NOW()),
    (4, 'Team Product', 'Ana Torres',     4, NOW(), NOW()),
    (5, 'Team Data',    'Luis Mamani',    5, NOW(), NOW());


INSERT INTO unit_of_work_work_team_lists (unit_of_work_id, work_team_list_id)
VALUES
    (1, 1), (2, 2), (3, 3), (4, 4), (5, 5);


INSERT INTO membership_plans (id, plan_name, price, membership_id, created_at, updated_at)
VALUES
    (1, 'Plan Básico',      49,  1, NOW(), NOW()),
    (2, 'Plan Profesional', 99,  2, NOW(), NOW()),
    (3, 'Plan Enterprise',  199, 2, NOW(), NOW());


INSERT INTO benefits (id, title, description, membership_plan_id, created_at, updated_at)
VALUES
    (1, 'Acceso al Foro',          'Participación en foros de la empresa',        1, NOW(), NOW()),
    (2, 'Notificaciones Básicas',  'Alertas por mensajes y actividad general',     1, NOW(), NOW()),
    (3, 'Dashboard Personalizado', 'Widgets configurables en el panel',            2, NOW(), NOW()),
    (4, 'Encuestas Ilimitadas',    'Creación y respuesta ilimitada de encuestas',  2, NOW(), NOW()),
    (5, 'Reportes Avanzados',      'Exportación de métricas y performance',        3, NOW(), NOW()),
    (6, 'Soporte Prioritario',     'Canal de soporte dedicado 24/7',              3, NOW(), NOW());


INSERT INTO membership_plans_benefits (membership_plan_id, benefits_id)
VALUES
    (1, 1), (1, 2),
    (2, 3), (2, 4),
    (3, 5), (3, 6);


INSERT INTO orders (id, user_account_id, amount, membership_id, created_at, updated_at)
VALUES
    (1, 1, 49,  1, NOW(), NOW()),
    (2, 4, 99,  2, NOW(), NOW()),
    (3, 7, 199, 2, NOW(), NOW());


INSERT INTO payments (id, order_id, transaction_id, payment_date, payment_status, payment_method, created_at, updated_at)
VALUES
    (1, 1, 'TXN-2024-001', '2024-01-05 10:00:00', 'SUCCEEDED', 'CREDIT_CARD', NOW(), NOW()),
    (2, 2, 'TXN-2024-002', '2024-03-07 14:30:00', 'PENDING', 'PAYPAL', NOW(), NOW()),
    (3, 3, 'TXN-2024-003', '2024-06-10 09:15:00', 'FAILED', 'BANK_TRANSFER', NOW(), NOW());


INSERT INTO forums (id, title, description, company_id, created_at, updated_at)
VALUES
    (1, 'Foro TechCorp',     'Espacio de comunicación para TechCorp SAC',     1, NOW(), NOW()),
    (2, 'Foro InnovatePeru', 'Espacio de comunicación para InnovatePeru SRL', 2, NOW(), NOW());


INSERT INTO categories (id, title, description, forum_id, created_at, updated_at)
VALUES
    (1, 'Anuncios',        'Comunicados oficiales de la empresa',      1, NOW(), NOW()),
    (2, 'Soporte Técnico', 'Dudas y resolución de problemas técnicos', 1, NOW(), NOW()),
    (3, 'General',         'Temas varios de InnovatePeru',             2, NOW(), NOW()),
    (4, 'Proyectos',       'Seguimiento de proyectos activos',         2, NOW(), NOW());


INSERT INTO forums_categories (forum_id, categories_id)
VALUES
    (1, 1), (1, 2),
    (2, 3), (2, 4);


INSERT INTO threads (id, title, area_company_id, last_message, category_id, message_count, created_at, updated_at)
VALUES
    (1, 'Bienvenida al equipo Q1 2024',      1, NOW(), 1, 2, NOW(), NOW()),
    (2, 'Error en pipeline de CI/CD',         1, NOW(), 2, 1, NOW(), NOW()),
    (3, 'Kickoff Roadmap Producto 2024',      3, NOW(), 3, 3, NOW(), NOW()),
    (4, 'Resultados del sprint de Analytics', 4, NOW(), 4, 2, NOW(), NOW());


INSERT INTO categories_threads (category_id, threads_id)
VALUES
    (1, 1), (2, 2), (3, 3), (4, 4);


INSERT INTO messages (id, user_account_id, content_message, thread_id, created_at, updated_at)
VALUES
    (1, 1, 'Bienvenidos a todos al primer sprint del año. ¡Mucho éxito!',                  1, NOW(), NOW()),
    (2, 2, 'Gracias Carlos, listos para empezar.',                                         1, NOW(), NOW()),
    (3, 7, 'El pipeline falla en el paso de tests. Revisar la configuración de Jest.',     2, NOW(), NOW()),
    (4, 4, 'Iniciamos el roadmap: prioridad en feature de notificaciones en tiempo real.', 3, NOW(), NOW()),
    (5, 5, 'Confirmado, estoy coordinando con el equipo de data.',                         3, NOW(), NOW()),
    (6, 6, 'El dashboard de analytics ya refleja los datos del sprint pasado.',            3, NOW(), NOW()),
    (7, 5, 'Los resultados del sprint de analytics superaron el benchmark en un 12%.',     4, NOW(), NOW());


INSERT INTO threads_messages (thread_id, messages_id)
VALUES
    (1, 1), (1, 2),
    (2, 3),
    (3, 4), (3, 5), (3, 6),
    (4, 7);


INSERT INTO assets (id, message_id, name, url, file_size, file_type, created_at, updated_at)
VALUES
    (1, 3, 'ci_error_log.pdf',      'https://storage.softwork.pe/assets/ci_error_log.pdf',      '245KB', 'PDF',   NOW(), NOW()),
    (2, 7, 'analytics_results.pdf', 'https://storage.softwork.pe/assets/analytics_results.pdf', '1.2MB', 'PDF',   NOW(), NOW()),
    (3, 4, 'roadmap_2024.pdf',      'https://storage.softwork.pe/assets/roadmap_2024.pdf',       '890KB', 'PDF',   NOW(), NOW()),
    (4, 1, 'welcome_video.mp4',     'https://storage.softwork.pe/assets/welcome_video.mp4',      '15MB',  'VIDEO', NOW(), NOW());


INSERT INTO messages_assets (message_id, assets_id)
VALUES
    (3, 1), (7, 2), (4, 3), (1, 4);


INSERT INTO notifications (id, seen, notification_type, user_account_id, created_at, updated_at)
VALUES
    (1, false, 'MESSAGE', 1, NOW(), NOW()),
    (2, false, 'FORUM',   2, NOW(), NOW()),
    (3, true,  'PAYMENT', 4, NOW(), NOW()),
    (4, false, 'SURVEY',  3, NOW(), NOW()),
    (5, false, 'MESSAGE', 5, NOW(), NOW()),
    (6, true,  'PAYMENT', 7, NOW(), NOW());


INSERT INTO notification_details (id, title, content, notification_id, created_at, updated_at)
VALUES
    (1, 'Nuevo mensaje en tu hilo',         'Carlos respondió en "Bienvenida al equipo Q1 2024"', 1, NOW(), NOW()),
    (2, 'Actividad en el foro',             'Se creó una nueva categoría en Foro TechCorp',        2, NOW(), NOW()),
    (3, 'Pago confirmado',                  'Tu pago de S/99 fue procesado correctamente',         3, NOW(), NOW()),
    (4, 'Nueva encuesta disponible',        'Tienes una encuesta pendiente de responder',          4, NOW(), NOW()),
    (5, 'Nuevo mensaje en Kickoff Roadmap', 'Luis Mamani respondió en el hilo del roadmap',        5, NOW(), NOW()),
    (6, 'Pago procesado Plan Enterprise',   'Tu suscripción al Plan Enterprise está activa',       6, NOW(), NOW());


INSERT INTO performances (id, employee_profile_id, date_time, classification, created_at, updated_at)
VALUES
    (1, 1, '2024-03-31', 4, NOW(), NOW()),
    (2, 2, '2024-03-31', 5, NOW(), NOW()),
    (3, 3, '2024-03-31', 3, NOW(), NOW()),
    (4, 5, '2024-03-31', 4, NOW(), NOW()),
    (5, 7, '2024-03-31', 5, NOW(), NOW());


INSERT INTO comments_employees (id, title, content, rrhh_profile_id, performance_id, created_at, updated_at)
VALUES
    (1, 'Excelente manejo del sprint',      'Carlos demostró liderazgo técnico sólido en Q1.',       1, 1, NOW(), NOW()),
    (2, 'Buen trabajo en UI',               'Maria entregó los componentes antes de lo previsto.',   1, 2, NOW(), NOW()),
    (3, 'Oportunidad de mejora en testing', 'Jorge debe reforzar sus conocimientos en pruebas E2E.', 1, 3, NOW(), NOW()),
    (4, 'Análisis preciso del negocio',     'Luis entregó insights de alto valor para el equipo.',   2, 4, NOW(), NOW()),
    (5, 'Referente técnico del equipo',     'Diego lidera con el ejemplo en buenas prácticas.',      1, 5, NOW(), NOW());


INSERT INTO performances_comment_employee_lists (performance_id, comment_employee_list_id)
VALUES
    (1, 1), (2, 2), (3, 3), (4, 4), (5, 5);


INSERT INTO surveys (id, title, description, target_type, expiration_time, created_at, updated_at)
VALUES
    (1, 'Satisfacción Laboral Q1 2024', 'Evaluación del clima laboral del primer trimestre', 'AREA_COMPANY', '2024-04-30', NOW(), NOW()),
    (2, 'Evaluación de Sprint',         'Retrospectiva del equipo de desarrollo',            'UNIT_OF_WORK',  '2024-04-15', NOW(), NOW()),
    (3, 'Cultura y Valores',            'Percepción de cultura organizacional',              'TEAM_OF_WORK',  '2024-05-01', NOW(), NOW());


INSERT INTO questions_surveys (id, text_question, question_type, survey_id, created_at, updated_at)
VALUES
    (1, '¿Cómo calificarías el ambiente de trabajo en tu área?',              'RATING',          1, NOW(), NOW()),
    (2, '¿Qué aspectos mejorarías en tu equipo?',                             'OPEN_SURVEY',     1, NOW(), NOW()),
    (3, '¿Se cumplieron los objetivos del sprint?',                           'MULTIPLE_CHOICE', 2, NOW(), NOW()),
    (4, '¿El equipo contó con los recursos necesarios?',                     'MULTIPLE_CHOICE', 2, NOW(), NOW()),
    (5, '¿Sientes que los valores de la empresa se reflejan en el día a día?','RATING',          3, NOW(), NOW());


INSERT INTO answers (id, value, score_answer, created_at, updated_at)
VALUES
    (1, 1, 4, NOW(), NOW()),
    (2, 1, 5, NOW(), NOW()),
    (3, 1, 3, NOW(), NOW()),
    (4, 2, 4, NOW(), NOW()),
    (5, 1, 5, NOW(), NOW());


INSERT INTO survey_responses (id, survey_id, employee_profile_id, submitted_at, commentary, cause, created_at, updated_at)
VALUES
    (1, 1, 1, NOW(), 'En general buen ambiente, aunque hay margen de mejora en la comunicación.', 'Cultura organizacional', NOW(), NOW()),
    (2, 1, 2, NOW(), 'Me siento cómoda y el equipo es muy colaborativo.',                         'Clima laboral',          NOW(), NOW()),
    (3, 2, 3, NOW(), 'El sprint fue intenso pero logramos todos los objetivos.',                  'Gestión del sprint',     NOW(), NOW()),
    (4, 2, 1, NOW(), 'Faltó tiempo para refactorizar, pero el resultado fue positivo.',           'Deuda técnica',          NOW(), NOW()),
    (5, 3, 4, NOW(), 'Los valores se viven en el día a día del equipo.',                         'Cultura',                NOW(), NOW());


INSERT INTO reports (id, reason, description, user_account_id, report_date, area_company_id, created_at, updated_at)
VALUES
    (1, 'Comportamiento inapropiado',  'Comentarios fuera de lugar durante la reunión diaria del equipo.',           3, '2024-02-10', 1, NOW(), NOW()),
    (2, 'Falla de seguridad',          'Se detectó una credencial expuesta en un commit del repositorio.',           7, '2024-02-18', 1, NOW(), NOW()),
    (3, 'Incumplimiento de entrega',   'El módulo de QA no fue entregado en la fecha pactada del sprint.',           3, '2024-03-01', 2, NOW(), NOW()),
    (4, 'Conflicto entre compañeros',  'Desacuerdo recurrente entre miembros del equipo de producto.',               4, '2024-03-05', 3, NOW(), NOW()),
    (5, 'Uso indebido de recursos',    'Uso de licencias de software fuera del alcance autorizado.',                 5, '2024-03-12', 4, NOW(), NOW()),
    (6, 'Retraso reiterado',           'Llegadas tarde constantes a las ceremonias ágiles del equipo de datos.',     6, '2024-03-20', 4, NOW(), NOW()),
    (7, 'Reporte de bug crítico',      'Caída del pipeline de CI/CD afectando despliegues en producción.',           1, '2024-03-22', 1, NOW(), NOW()),
    (8, 'Falta de comunicación',       'El equipo de QA no fue informado de cambios en el alcance del sprint.',      2, '2024-03-25', 2, NOW(), NOW());

SELECT setval('users_id_seq',               (SELECT MAX(id) FROM users));
SELECT setval('companies_id_seq',           (SELECT MAX(id) FROM companies));
SELECT setval('memberships_id_seq',         (SELECT MAX(id) FROM memberships));
SELECT setval('user_accounts_id_seq',       (SELECT MAX(id) FROM user_accounts));
SELECT setval('employee_profiles_id_seq',   (SELECT MAX(id) FROM employee_profiles));
SELECT setval('rrhh_profiles_id_seq',       (SELECT MAX(id) FROM rrhh_profiles));
SELECT setval('dashboards_id_seq',          (SELECT MAX(id) FROM dashboards));
SELECT setval('widgets_id_seq',             (SELECT MAX(id) FROM widgets));
SELECT setval('area_companies_id_seq',        (SELECT MAX(id) FROM area_companies));
SELECT setval('unit_of_works_id_seq',        (SELECT MAX(id) FROM unit_of_works));
SELECT setval('work_teams_id_seq',          (SELECT MAX(id) FROM work_teams));
SELECT setval('membership_plans_id_seq',    (SELECT MAX(id) FROM membership_plans));
SELECT setval('benefits_id_seq',            (SELECT MAX(id) FROM benefits));
SELECT setval('orders_id_seq',              (SELECT MAX(id) FROM orders));
SELECT setval('payments_id_seq',            (SELECT MAX(id) FROM payments));
SELECT setval('forums_id_seq',              (SELECT MAX(id) FROM forums));
SELECT setval('categories_id_seq',          (SELECT MAX(id) FROM categories));
SELECT setval('threads_id_seq',             (SELECT MAX(id) FROM threads));
SELECT setval('messages_id_seq',            (SELECT MAX(id) FROM messages));
SELECT setval('assets_id_seq',              (SELECT MAX(id) FROM assets));
SELECT setval('notifications_id_seq',       (SELECT MAX(id) FROM notifications));
SELECT setval('notification_details_id_seq',(SELECT MAX(id) FROM notification_details));
SELECT setval('performances_id_seq',        (SELECT MAX(id) FROM performances));
SELECT setval('comments_employees_id_seq',   (SELECT MAX(id) FROM comments_employees));
SELECT setval('surveys_id_seq',             (SELECT MAX(id) FROM surveys));
SELECT setval('questions_surveys_id_seq',    (SELECT MAX(id) FROM questions_surveys));
SELECT setval('answers_id_seq',             (SELECT MAX(id) FROM answers));
SELECT setval('survey_responses_id_seq',    (SELECT MAX(id) FROM survey_responses));
SELECT setval('reports_id_seq', (SELECT MAX(id) FROM reports));