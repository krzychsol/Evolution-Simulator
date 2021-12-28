# Symulator Ewolucyjny
### Opis
Jest to symulator naturalnego środowiska zbierający i na bieżąco wyświetlający aktualne informacje o obiektach znajdujących się na mapie 
takie jak np. ilość zwierząt ,czy roślin. Symulacja przebiega na dwóch mapach - lewej i prawej. Lewa mapa jest "zawinięta" tzn. że wyjście zwierzęcia poza mapę z jednej strony
powoduje jego pojawienie się z przeciwnej strony mapy. Prawa mapa natomiast posiada "mur" ,czyli próba wyjścia za mapę powoduje zatrzymaniem się zwierzęcia na skraju mapy i utratą
kolejki. W trakcie przebiegu symulacji zwięrzęta zjadają losowo generowaną trawę, poruszają się, kopulują oraz giną, gdy stracą całą swoją energię. Program umożliwia zatrzymanie 
przebiegu symulacji w dowolnym momencie na mapie, podświetlenie zwierząt o najczęściej występującym genotypie, śledzenie jednego ze zwierząt widząc na bieżąco ilość jego dzieci
i potomków oraz numer dnia symulacji ,w którym owe zwierzę zginęło. Ponadto statystyki można wyeksportować do pliku CSV.

### O projekcie 
Projekt został wykonany na zajęcia z programowania obiektowego prowadzone na Akademii Górniczo-Hutniczej im. Stanisława Staszica w Krakowie
na Wydziale Informatyki,Elektroniki i Telekomiunikacji na keirunku Informatyka.

### Uruchomienie 
Projekt został stworzony z użyciem narzędzia Gradle. Główna funkcja Main.java znajduje się w lokalizacji src/main/java/agh/ics/darwin/Setup/Main.
Porgram posiada podstawowe testy jednostkowe. Domyślne parametry wejściowe programu znajdują się w pliku parameters.json bezpośrednio w katalogu z projektem. 
Interfejs GUI umożliwia użytkownikowi wprowadzenie własnych parametrów symulacji.

### Znaczenie kolorów
Zwierzęta początkowo są koloru zielonego ,w miarę tracenia energi ich kolor się zmienia na czerwony ,przechodząc przez różne odcienie zieleni i czerwieni.
Kolor trawy jest jednakowy przez cały okres trwania symulacji. W centrum mapy znajduje się obszar jungli, która jest wyróżniona nieco ciemniejszym odcieniem zieleni w stosunku do
pozostałego obszaru będącego stepem. 

### Technologie 
Java 16 + JavaFX + Swing 

### Biblioteki 
awt, json-simple, FileReader, JUnit

### Wykorzystane materiały źródłowe
https://docs.oracle.com/javase/8/docs/api/java/awt/package-frame.html <br/>
https://docs.oracle.com/javase/8/docs/api/index.html?javax/swing/package-summary.html <br/>
https://javastart.pl/baza-wiedzy/grafika_awt_swing/zdarzenia-przyciski <br/>
https://mkyong.com/java/how-to-export-data-to-csv-file-java/ <br/>
https://mkyong.com/java/json-simple-example-read-and-write-json/ <br/>
https://programuj.pl/blog/java-formatowanie-liczb-decimal-format <br/>
https://javastart.pl/baza-wiedzy/grafika_awt_swing/proste-rysowanie-jpanel-i-jcomponent <br/>
https://stackoverflow.com/questions/15544549/how-does-paintcomponent-work <br/>
https://docs.oracle.com/javase/7/docs/api/javax/swing/JComponent.html <br/>
http://tutorials.jenkov.com/javafx/index.html <br/>

### Autor 
Krzysztof Solecki





