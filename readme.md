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




do wyboru
1. skills
2. artefakty
3. frakcje
4. pola specjalne + edytor, jako libka mapa przygody + walka
5. AI? Bot go gry jako player2
6. gra sieciowa
7. przeniesienie do weba server + client webowy
8. zapis odczyt stanu gry - baza danych