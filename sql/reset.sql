set FOREIGN_KEY_CHECKS = 0;

delete from answer;
delete from log;
delete from review;
delete from stat_answer;
delete from user_questionnaire_submitted;
delete from question;
delete from questionnaire;
delete from user;

insert into user values (1, 'ferruccio', 'ferruccio@polimi.it', 'password', 'Ferruccio', 'Resta', 0, 0, 1);
insert into user values (2, 'luigi', 'luigi@mail.polimi.it', 'password', 'Luigi', 'Fusco', 0, 0, 0);
insert into user values (3, 'francesco', 'francesco@mail.polimi.it', 'password', 'Francesco', 'Gonzales', 0, 0, 0);
insert into user values (4, 'alberto', 'alberto@mail.polimi.it', 'password', 'Alberto', 'Latino', 0, 1, 0);

insert into questionnaire values ('2021-01-10', 'PoliMi Backpack', NULL);
insert into questionnaire values ('2021-01-20', 'PoliMi Pen', NULL);

insert into question values (1, '2021-01-10', 'Is it good for hiking?');
insert into question values (2, '2021-01-20', 'Does it last enough?');
insert into question values (3, '2021-01-20', 'What color is it?');

insert into answer values (1, 2, 'yes');
insert into answer values (1, 3, 'yes');
insert into answer values (2, 2, 'yes');
insert into answer values (2, 3, 'no');
insert into answer values (3, 2, 'red');
insert into answer values (3, 3, 'blue');

insert into stat_answer values ('2021-01-10', 2, NULL, NULL, NULL);
insert into stat_answer values ('2021-01-20', 2, NULL, NULL, NULL);
insert into stat_answer values ('2021-01-10', 3, 22, NULL, 'H');
insert into stat_answer values ('2021-01-20', 3, 22, NULL, 'M');

insert into user_questionnaire_submitted values (2, '2021-01-10', TRUE);
insert into user_questionnaire_submitted values (2, '2021-01-20', TRUE);
insert into user_questionnaire_submitted values (3, '2021-01-10', TRUE);
insert into user_questionnaire_submitted values (3, '2021-01-20', TRUE);

insert into review values (1, '2021-01-10', 'Good value overall');
insert into review values (2, '2021-01-20', 'It broke immediately!!!');
insert into review values (3, '2021-01-20', 'Good for taking exams');

set FOREIGN_KEY_CHECKS = 1;
