INSERT IGNORE ysofthr.company VALUES (101, '2020-01-10', 'Manager', 'Leo', 'Smith', 'leo@mail.com', 'leo', '+905342002030', '1972-01-02', 'United Kingdom', 12500, 'PC Payroll', null, 1);
INSERT IGNORE ysofthr.company VALUES (102, '2020-02-10', 'Manager', 'Christine', 'Jackson', 'christie@mail.com', 'christine', '+905342002040', '1965-04-16', 'United Kingdom', 14000, 'PC Payroll', null, 0);
INSERT IGNORE ysofthr.company VALUES (103, '2020-03-10', 'Manager', 'Hadden', 'Atkinson', 'hadden@gmail.com', 'hadden', '+905342002050', '1984-10-12', 'Poland', 9600, 'Calculator.Net', null, 0);
INSERT IGNORE ysofthr.company VALUES (104, '2020-04-10', 'Analyst', 'Ellen', 'Reid', 'ellen@outlook.com', 'ellen', '+905342002060', '1980-05-02', 'United States', 8000, 'PC Payroll', null, 1);
INSERT IGNORE ysofthr.company VALUES (105, '2020-05-10', 'Analyst', 'Mark', 'Bishop', 'mark@mail.com', 'mark', '+905342002070', '1981-12-02', 'Canada', 7900, 'PC Payroll', null, 0);
INSERT IGNORE ysofthr.company VALUES (106, '2020-06-10', 'Analyst', 'Ja', 'Dong', 'ja@mail.com', 'ja', '+905342002080', '1965-11-22', 'Korea', 10000, 'Calculator.Net', null, 0);
INSERT IGNORE ysofthr.company VALUES (107, '2020-07-10', 'Designer', 'Maxim', 'Shafirov', 'maxim@mail.ru', 'maxim', '+905342002080', '1998-06-15', 'Russia', 7000, 'PC Payroll', null, 1);
INSERT IGNORE ysofthr.company VALUES (108, '2020-08-10', 'Designer', 'Yaroslav', 'Morozov', 'yaroslav@mail.ru', 'yaroslav', '+905342002090', '1999-12-02', 'Russia', 7000, 'PC Payroll', null, 0);
INSERT IGNORE ysofthr.company VALUES (109, '2020-09-10', 'Designer', 'Duygu', 'Özkan', 'duygu@mail.com.tr', 'duygu', '+905342003010', '1976-10-14', 'Turkey', 7200, 'Calculator.Net', null, 0);
INSERT IGNORE ysofthr.company VALUES (110, '2020-10-10', 'Programmer', 'Aaron', 'Swartz', 'aaron@mail.com', 'aaron', '+905342003020', '1986-12-02', 'United States', 16000, 'PC Payroll', null, 1);
INSERT IGNORE ysofthr.company VALUES (111, '2020-11-10', 'Programmer', 'Jane Manchun', 'Wong', 'jane@mail.com', 'janemanchun', '+905342003030', '1995-12-12', 'Hong Kong', 15500, 'PC Payroll', null, 0);
INSERT IGNORE ysofthr.company VALUES (112, '2020-12-10', 'Programmer', 'Joshua', 'Hill', 'joshua@mail.com', 'joshua', '+905342003040', '1977-07-10', 'Germany', 14500, 'Calculator.Net', null, 0);
INSERT IGNORE ysofthr.company VALUES (113, '2020-01-11', 'Tester', 'Hayri', 'Tufan', 'hayri@mail.com.tr', 'hayri', '+905342003040', '1982-12-02', 'Turkey', 5000, 'PC Payroll', null, 1);
INSERT IGNORE ysofthr.company VALUES (114, '2020-02-11', 'Tester', 'Tural', 'Əhmədov', 'tural@mail.com', 'tural', '+905342003050', '1998-04-24', 'Azerbaijan', 5000, 'PC Payroll', null, 0);
INSERT IGNORE ysofthr.company VALUES (115, '2020-03-11', 'Tester', 'Jackie', 'Wilson', 'jackie@mail.com', 'jackie', '+905342003060', '1982-12-02', 'Estonia', 4500, 'Calculator.Net', null, 0);

-- Below ones will inserted even if exists

INSERT IGNORE ysofthr.teams VALUES ('Admins', 101, 104, 107, 110, 113);
INSERT IGNORE ysofthr.teams VALUES ('Turbo', 103, 106, 107, 111, 114);
INSERT IGNORE ysofthr.teams VALUES ('Junior', 103, 105, 109, 110, 115);

INSERT IGNORE ysofthr.projects VALUES ('Website Development', 'JavaScript', 'Turbo', '2020-03-10', '2020-06-11', '2020-03-01', 'This is a website development project.');
INSERT IGNORE ysofthr.projects VALUES ('HR System', 'Java', 'Junior', '2020-02-10', '2020-05-11', '2020-02-01', null);
INSERT IGNORE ysofthr.projects VALUES ('Computer Project', 'Python', 'Turbo', '2019-12-20', '2020-06-10', '2019-12-20', null);