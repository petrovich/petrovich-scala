# petrovich-scala

Петрович склоняет имена, фамилии и отчества на русском языке.
Это порт библиотеки [petrovich-js](https://github.com/petrovich/petrovich-js) на Scala.
Библиотека совместима со Scala 2.11 и Scala.js 0.6.5.

# Использование

Для начала просто добавить библиотеку в зависимости вашего проекта

```scala
libraryDependencies += "com.github.fomkin" %% "petrovich-scala" % "0.1.0"
```

В случае Scala.js зависимость следует указывать так

```scala
libraryDependencies += "com.github.fomkin" %%% "petrovich-scala" % "0.1.0"
```

Теперь Петрович готов к работе. Давайте напишем немного кода.

```scala
import petrovich._

val person =
 LastName("Сидоров") ::
 FirstName("Иван") ::
 MiddleName("Петрович")

val res = petrovich(person, Case.Genitive)
res.last // Some("Сидорова")
res.first // Some("Ивана")
res.middle // Some("Петровича")
```

Если отчество не указано, то нужно обязательно указать пол иначе
будет выброшено исключение `PetrovichException`.

```scala
val person =
 Gender.Female ::
 LastName("Сидорова") ::
 FirstName("Мария")
```

Можно удобно получать имя в виде строки. 

```scala
val person =
 LastName("Бонч-Бруевич") ::
 FirstName("Виктор") ::
 MiddleName("Леопольдович")

person.firstLast // Виктор Бонч-Бруевич 
person.lastFirst // Бонч-Бруевич Виктор
person.lastFirstMiddle // Бонч-Бруевич Виктор Леопольдович
person.firstMiddleLast // Виктор Леопольдович Бонч-Бруевич
```

А еще есть альтернативный синтаксис, если вы понимаете о чем я.

```scala
petrovich.
  male.
  first("Лев").
  last("Щаранский").
  prepositional.
  firstLast

// Льве Щаранском
```

`Person` это список частей имени и пола человека.
Мы можете этим воспользоваться

```scala
val middleNames = persons flatMap { person => 
  person collect { case MiddleName(s) => s }   
}
```

На этом все. Приятного использования!

## Разрабатываем вместе

Я всегда рад вашим пулл-реквестам. Обратите внимание, что
в проекте содержится субмодуль, так что не забудьте
склонировать его тоже. Эта команда склонирует проект вместе
с субмодулем.

```sh
git clone --recursive git@github.com:fomkin/petrovich-scala.git
```
