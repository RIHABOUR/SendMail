Send Mail
===============================

[![Build Status](https://travis-ci.org/yuriikovalchuk/SendMail.svg?branch=master)](https://travis-ci.org/yuriikovalchuk/SendMail)

===============================
###REST-сервис, принимающий и выполняющий заказы на рассылку email’ов.
===============================
##Использованные технологии:
#SpringBoot, SpringMail, Javax Mail, Greenmail, JUnit, Mockhito, Hibernate Validator, Jirutka, Lombok, Maven, Travis-CI
===============================
##Краткое обоснование данного решения поставленной задачи:

Рест-сервис должен принимать запрос в json формате с набором определенных параметров,
поэтому было принято решение навесить уловия на сам контроллер,
который бы ругался в случае некорректного медиатипа и тд, плюс сразу же перегонял входящий текст в объект типа Order.

Далее в работу включается bean validator от hibernate + небольшая библиотека Jirutka,
которая может сразу проверять целые коллекции на соответствие шаблону адресов электронной почты. Проверяются все
критерии, которые были даны в задании и замечании к нему (contentType письма и длина списка адресов "to").

Если авторизация прошла успешно, заказ отправляется в слой service. Здесь ему устанавливается время заказа,
считается и устанавливается размер заказа, а так же из User Service берется пользователь и устанавливается.
Если такого пользователя в системе нет, выбрасывается специальнео исключение UserNotFoundException.

После устанвоки этих параметров система пытается добавить заказ в специальное хранилище, которое проверяет
не превысил ли данный пользователь ограничение по кол-ву заказов за промежуток времени. Если превысил,
выбрасывается специальное исключение OrderLimitException.

Далее заказ отправляется в слой repository для добавления в очередь, которая имеет ограниченный размер,
дабы предотвратить атаку на отказ в обслуживании. Если очередь переполнена, она бросит IllegalStateException.
Если все в порядке, заказ добавится в очередь и вернется его id в системе.

Заказы от пользователей принимаются максимально быстро, так как самой рассылкой занимается другой сервис.

Сервис по рассылке писем с определенной периодичностью пытается взять из
хранилища заказов один экземпляр и обработать его. Есть два вида таких сервисов: Огранниченный по общему объему заказов
(то как просилось в задании) и ограниченный по кол-ву одновременно обрабатываемых писем. Обе реализации не блокируются
при попытке взять заказ. Такой подход выбран исходя из соображений, что эти сервисы было бы неплохо корректно
останавливать. Так как их принцип работы в том, что они и добавляют заказ на обработку и забирают результаты отправки
писем. Все это происходит пока не будет произведена остановка такого сервиса.

Запускаются такие такие сервисы в методе main в SendMailApplication. Изначально хотел сделать через CommandLineRunner и
с @Async, но так как хотел имет ьвозможность останавливать сревисы, этот вариант не подошел.

Все сценарии развития событий предусмотрены и если возникают какие-либо исключительные ситуации,
то они обрабатываются в контроллере и пользователь получает определенный ответ со статусом и сообщением.

Помимо валидации бинов и входящего тела запроса так же используется проверка на null во всех нуждающихся
методах. Для этого подключена библиотека Lombok. С ее помощью так же уменьшилось кол-ко тривиального кода.
Кконструкторы, геттеры и сеттеры в бинах заменены аннотациями.

Протестированы все слои приложения. Используется JUnit и Mockhito. Для тестирования отправки писем используется
библиотека GreenMail. Создан тест, который реально проверяет, что письмо отправились с указанным contentType.

P.S. чтобы запустить сервер в боевом режиме, укажите в spring-app.xml логин и пароль от gmail (выбран как базовый)