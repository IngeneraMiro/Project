# Project
 End of Spring Web advanced
 
 Приложението има за цел да служи като система за информационен обмен между пациент и неговият личен лекар и медицинската сестра.
 По същество е опростен месинджър.
 Позволява регистрирането и обслужването на повече от една лекарска практика
 Всички прочетени съобщения по стари от един месец се изтриват автоматично.
 Всички съобщения по стари от три месеца се изтриват независимо дали са прочетени.

При начално зареждане се вдига базата и се зареждат ролите . 
Регистрира се администратор с потребителски номер A888888 и парола: password .
Въсможните роли са : ADMIN, DOCTOR, MAIN DOCTOR, NURSE, PATIENT

Всеки новорегистриран потребител получава автоматично генериран регистрационен номер (по подобие на  номера който се издава в медицинските лаборатории - идеята е че пациентите имат навик да получават медицинска информация с регистрационен номер).Първата Буква от номера може да бъде : A, D, N, P   и дава информация за това какъв тип е регистрираният потребител.Логването после става с персоналният регистрационен номер и и паролата която потрбителя си е създал.  

Администраторът може да регистрира лекарски практики, да ги активира и деактивира и да ги редактира.
Администраторът може да назначава персонал в практиките и да създава главен лекар.
Администраторът може да записва и редактира общата информация която трябва да се визуализира за всички пациенти независимо от практиката.
Администраторът има достъп до системния лог.

Главният лекар може да създава персонал в собствената си практика - лекари и сестри.
Главният лекар може да регистрира пациенти в собствената си практика.
Главният лекар може да чете собствените си съобщения и да изпраща съобщения на всички от собствената си практика.

Лекарят може да съсздаде медицинска сестра за себе си, но не може да създава лекари.
Лекарят може да регистрира пациенти в собствената си практика.
Лекарят може да чете собствените си съобщения и да изпраща съобщения на всички от собствената си практика.

Медицинската сестра може да регистрира пациенти в собствената си практика.
Медицинската сестра може да чете соствените си съобщения и да изпраща съобщения на всички от собствената си практика.

Пациентът може да чете собствените си съобщения и да ицпраща съобщения на своята медицинска сестра.
Пациентът получава актуална информация относно полагащите се годишни прегледи , диспансерна дейност и амбулаторна дейност.
