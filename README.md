# Statistics error

Create file with statistics of error from *.log files.

### Files
The example *.log file:

```2019-10-10T10:13:22.102;INFO;Информация 1
2019-10-11T10:23:22.102;ERROR;Ошибка 1
;
2019-10-10T10:45:00.152;ERROR;Ошибка 2
2019-10-10T10:45:00.152;ERROR;Ошибка 2
2019-10-10T11:11:11.111;ERROR;Ошибка 3
   ; ;
2019-10-10T11:25:27.015;WARN;Предупреждение 1
2019-10-10T11:47:05.342;ERROR;Ошибка 4
2019-10-10T13:13:13.113;ERROR;Ошибка 5
2019-10-10T17:08:22.102;ERROR;Ошибка 6
2019-10-10T18:34:54.536;INFO;Предупреждение 2
2019-10-10T18:37:31.262;ERROR;Ошибка 7
2019-10-10T18:48:04.728;INFO;Предупреждение 3
```
Result:

```2019-10-10, 10.00-11.00 Количество ошибок: 6
2019-10-10, 11.00-12.00 Количество ошибок: 6
2019-10-10, 13.00-14.00 Количество ошибок: 3
2019-10-10, 17.00-18.00 Количество ошибок: 3
2019-10-10, 18.00-19.00 Количество ошибок: 3
2019-10-11, 10.00-11.00 Количество ошибок: 3
```

### How to run
#####To run from console

`mvn package`

`mvn install`

`java -jar C:\errorstatistics\target\error-statistics-1.0-SNAPSHOT.jar <package with *.log files> <package for save the statistics file>`    

#####For example:

`java -jar C:\errorstatistics\target\error-statistics-1.0-SNAPSHOT.jar C:\server\log\ C:\statistics\`    
#### Important
To run from IntellijIdea have to install the Lombok plugin.
