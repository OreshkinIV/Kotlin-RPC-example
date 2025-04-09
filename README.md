# Kotlin RPC example

Пример использования Kotlin RPC в Kotlin Multiplatform проекте.

## Структура

### server модуль

Cерверное приложение Ktor с обращением к PostgreSQL и JWT авторизацией. 
Токен авторизации валиден 1 минуту, refresh token валиден 10 минут. 
Если токен авторизации не валиден, то запрос возвращает 401 ошибку, 
а клиент запрашивает новую пару токенов путем отправки refresh токена и повторяет запрос с новыми токенами.
Если и refresh токен не валиден, то происходит разлогин на стороне клиента.
Для запуска сервера локально вызвать main функцию [Application](server/src/main/kotlin/Application.kt).
Обращение к базе данных закомментировано в таблице [Users](server/src/main/kotlin/ktor/backend/db/postgresql/tables/users/Users.kt), т.к. закончился пробный период Railway и пользователи никуда не сохраняются.
Серверное приложение содержит 2 rpc маршрута:

1) auth - по данному пути находятся методы:
   * registerNewUser - регистрация пользователя и получение auth и refresh токенов
   * login - авторизация пользователя и получение auth и refresh токенов
   * refreshToken - запрос новой пары auth и refresh токенов

2) user - по данному пути находятся методы, требующие токен авторизации:
   * getUserJwtPayload - получение jwt payload текущего пользователя
   * sendMessage - отправка сообщения пользователем
   * listenMessages - прослушивание потока сообщений, отправленных методом sendMessage
   * loadFile - отправка файла от клиента как ByteArray

### shared модуль

Модуль содержит общие модели запросов и ответов, а также интерфейсы rpc сервисов для сервера и клиента.

### composeApp модуль

Клиентское приложение android и desktop (запуск десктопа из терминала .\gradlew :composeApp:run; 
ios не запускал, т.к. нужен mac и xcode, а web отключен).
Токены авторизации хранятся в DataStore<Preferences>.
В качестве DI используется Koin.
Если обращаться к серверу, запущенному локально из терминала на вашем компьютере, то в [KrpcClient](composeApp/src/commonMain/kotlin/org/example/krpc/data/network/KrpcClient.kt)
нужно использовать port = TLS_PORT и host = getLocalHost(). Если обращаться к серверу, развернотому в Railway, то использовать
host = RAILWAY_DOMAIN и port = 0.



