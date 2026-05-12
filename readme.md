#How to run?
1. Install docker and docker-compose example: https://docs.docker.com/desktop/setup/install/windows-install/
2. Run in command line `mvn clean install -DskipTests && docker-compose down -v && docker-compose up --build`
3. For economy client run LaunchBoardEconomy/ For battle client run LaunchBattle

#How to debug ?
1. Add new Run/Debug configuration
2. Select Remote JVM Debug, default configuration is fine (Transport: Socket, port: 5005, host: localhost)

https://www.spriters-resource.com/pc_computer/heroes3/ - ikonki z
gry http://www.heroesofmightandmagic.com/heroes3/creaturescastle.shtml - opis jednostek, czarów
itd. http://www.heroesofmightandmagic.com/heroes3/democheat.shtml - demo do ściągnięcia.

how to run:
https://www.geeksforgeeks.org/installation-guide/how-to-install-apache-maven-on-windows/
mvn clean install
[LaunchBattle.java](battle-gui%2Fsrc%2Fmain%2Fjava%2Fpl%2Fpsi%2Fgui%2FLaunchBattle.java)
[EconomyBoardStart.java](economy-gui%2Fsrc%2Fmain%2Fjava%2Fpl%2Fpsi%2Fgui%2FEconomyBoardStart.java)

Zaklęcia - Siwiec Konstanty
1. Zadający obrażenia (jest)
2. Buff (jest)
   a) Kończy się po N turach (N wyznacza moc czarów bohatera)
   b) 
3. Debuff
    a) odwrotnoś buffa
4. Wprowadzenie dmg obszarowego
5. Wprowadzenie żywiołow per czar
6. Integracje z innymi
   a) skill - mistrzostwo magi wpływa na czar - lvl 3 rzucam buff/debuff na wszystkie jednostki
   b) artefakt - daje niewrażliwość na wybrany typ magii np. debufy ognia
   c) kafelki specjalne - możliwość rzucenia czaru na kafelek np. ściana ognia czy inna mina
   d) mapa przygody - kapliczka, wchodzę dostaje randomowe zaklęcie do księgi

Maszyny oblężnicze - Dudek Michał, Podołowski Dominik
Integracje
   1. mapa przygody - kuźnia - możłiwość kupna dowolnej masyzny za jakieś tam zasoby
balista
1. skille - od lvl 2 rzucamy na dowolną jednostkę, wcześniej random. lvl 3 dwukrotnie, każdy z ataków wskazuje gracz
katapulta
   1. Mury - kafelki na mapie bitwy
   2. Bohater na mapie przygody ma możliwość spalić swój ruch na stworznie tymczasowego zamku. Jeśli przeciwnik zaatakuje:
      a) 3 kratki przed jego wojskiem jest mur
      b) na środku ( w osi Y) jest jakieś przejście jednostki gracza broniącego się mogą swobodnie przechodzić
3. Integracje:
   1. skille - jakiś defence coś tam, wzmacnia mury, dodaje fosę
namiot medyka
   1. skille - od lvl 2 rzucamy na dowolną jednostkę, wcześniej random. lvl 3 potrafi wskrzeszać jeśli wsytarczy pkt życia
   2. czary - czar powietrza, dający buff dla namiotu medyka, w zależności od poziomu mastery powietrza 25, 50, 75, 100% szans na podwójne leczenie (2x hp) 

   
Mapa przygody - Eryk Bogacki
1. Zamki - nowe jednostki pojawiają się w zamku na początek tygodnia
2. Exp - integracja skill learning. Daje +N% więcej expa - obsługa wyjścia z walk
3. grall
4. mgła wojny - skill + artefak widzę więcej
5. punkty ruchy - dróżki, kary za jednostki. droga koszt ruchu 1, teren, koszt ruchu 2, teren z karą - koszt ruchu 3
integracje:
1. frakcje - kary za ruch
2. skille - widzenie + ruch
3. artefakt - punkty ruchu, generowanei zasobów, zwiększanie przyrostów


****
Frakcje - Makowski Piotr
1. bastion - wszystkie jednostki + fabryka bastrakcyjna kreatur
2. typy ruchy
3. Rozwiązanie buga inifnite move range
Integracje:
1. Mapa przygody - zamek
2. czary - jednorożec, ent
3. artefakty - złoty łuk, brak złamanej strzały na zasięg
4. skille - offence, armorer,  [ bardziej dla skills]
5. Morale + luck


Split na aplikacje serwerową - Swidłowski Jakub
1. economy - spring - inicjalizacja gameEngine (singleton)
2. Inicjalizacja SWAGGER!
3. wystawienie metod get i post jakimś rest api
4. Dockeryzacja


Artefakry - Ratajczak Monika
1. Sloty na artefakty
2. Primary 4 dowolne
3. Set smoka
4. Zasoby - wóz z węglem, worek ze złotem
5. Spells - orb, księga
6. Spells* - odporność na jakieś zaklęcie


Zapis - odczyt gry. Szymański Adrian
1. Przeniesienie statystyk do bazy danych
2. Zapisanie stanu gry - poprawnie wczytana mapa, poprawnie wczytany bohater i jego wszelakie statsy
3. wczytanie stanu gry
4. Inicjalizacja hibernate local
5. inicjalizacja hibernate w springu na serwerze
6. Zapisywanie map z edytora


AI - Krzysztof Niemir
1. AI  potrafi się poruszać i robi sensowne ruchy w fazie ekonomii
2. Ai potrafi się porusząc i robi sensowne ruchy w fazie walki
3. Potrafi korzystać z zaklęć


Pola specjalne - RYbarczyk Bartek, Kubiś Juliusz
1. Pola - zamień, tylko latające mogą ominąć. 
2. Integracja z czarami buff/debuff w tym licznik ile tur ma trwać buff/debuff
3. Edytor mapy, zapisywany do pliku
4. Edytor pola ekonomi, wszystkie dostępne zamk, kapliczki, kopalnie etc

skills - Łyskawa Mateusz, Pablicka Karolina
1. Okienko bohatera
2. Exp, lvl up -> wybieramy 1 z 2 skilli. Slotów 8,10
3. learning
4. Morale + luck
4. Taktyka
5. logistyka
6. pathfinding
7. Elemental masteries
8. Heurystyka losowości