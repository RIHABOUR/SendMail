Send Mail
===============================

[![Build Status](https://travis-ci.org/yuriikovalchuk/SendMail.svg?branch=master)](https://travis-ci.org/yuriikovalchuk/SendMail)

===============================
###REST-сервис, принимающий и выполняющий заказы на рассылку email’ов.
===============================
##Использованные технологии:
#SpringBoot, SpringMail, Javax Mail, JUnit, Mockhito, Hibernate Validator, Jirutka, Lombok, Maven, Travis-CI
===============================
##Краткое обоснование данного решения поставленной задачи:

Рест-сервис должен принимать запрос в json формате с набором определенных параметров,
поэтому было принято решение навесить уловия на сам контроллер,
который бы ругался в случае некорректного медиатипа и тд, плюс сразу же перегонял входящий текст в объект типа Order.

Далее в работу включается bean validator от hibernate + небольшая библиотека Jirutka,
которая может сразу проверять целые коллекции на соответствие шаблону адресов электронной почты.

Далее объект типа Order перегоняется в data transfer object, который содержит в себе немного другие свойства,
нужные для установки в очередь заказов.

После конвертации заказа проверяется зарегистрирован ли пользователь в системе.
Для этого был создан user service и user repository.

Если авторизация прошла успешно, заказ отправляется в очередь, которых в системе две:

1-я очередь существует для каждого юзера и ограничивается кол-вом запросов за определенный отрезок времени,
в случае если лимит превышен - выбрасывается исключение с подробным описанием того, когда можно повторить запрос.

2-я предназначена для регулирования общего объема заказов на сервере и работает по другому принципу:
сюда приходят все запросы, которые прошли через первую очередь и пытаются попасть в буфер,
в случае переполнения буфера поток выполения ожидает, пока место не освободится.

После того как заказ успешно прошел обе очереди и добавился в буферы, объект Order конвертируется в объект письма
и отправляется с сервера. В качестве реализации отправки писем выбран SpringMail и Javax.Mail.
После отправки заказ удаляется из второго общего буфера. Первый буфер запросов пользователя очищается только тогда,
когда старые заказы уже не попадают в указанный интервал времени.

Все сценарии развития событий предусмотрены и если возникают какие-либо исключительные ситуации,
то они обрабатываются в контроллере и пользователь получает определенный ответ со статусом и сообщением.

Помимо валидации бинов и входящего тела запроса так же используется проверка на null во всех нуждающихся
методах. Для этого подключена библиотека Lombok. С ее помощью так же уменьшилось кол-ко тривиального кода.
Кконструкторы, геттеры и сеттеры в бинах заменены аннотациями.

Протестированы все слои приложения. Используется JUnit и Mockhito. Тесты покрывают различные
варианты использования сервиса.

Минусами и недостатками своего решения считаю отсутствие логгирования, отсутствие более подробных коментариев в коде,
отсутствие хороших интеграционных тестов. Это все можно доработать, если потратить больше времени.