Crestus Adelin 336CA

Cerinta:

Pentru aceasta aveam de implementat un procesator de comenzi de Black Friday in limbajul de programare Java, care sa foloseasca mecanisme de paralelizare,

In main am preluat argumentele si am deschis fisierele.

Am creat semafoarele si bariera de care aveam nevoie si am pornit primul nivel de Threaduri.

Threadurile de la primul nivel citesc in paralel folosind toate acelas Scanner, fiecare thread citeste cate o linie,
in ordine, astfel nu citeste fiecare thread tot fisierul.

Fiecare thread de lvl 1 are o tabela hash in care tine id ul comenzii si nr de produse nelivrate, si alta cu id ul comenzii
si nr total de produse.
Citim o linie din fisierul cu comenzi(practic o comanda) cat tp mai exista linii de citit. Toate threadurile de lvl 1 impart acelasi scanner
Parsam id ul comenzii si nr de produse si adaugam informatiile in tabelele hash.
Apoi adaugam in pool numberOfProducts taskuri.
Bariera ne asigura ca apucam sa punem toate taskurile posibile in ea inainte sa incercam sa inchidem poolul.
La final pentru fiecare intrare din tabela hash verificam daca s-au livrat toate produsele, si daca da, marcam comanda ca si livrata.

La threadurile de lvl 2  citim fisierul cu produsele pana gasim produsul sau pana cand ajungem la EOF
Trb sa avem grija la al catelea produs s-a ajuns de livrat dintr-o anumita comanda si daca l-am gasit scriem in fisier.
si actualizam datele in tabela hash. La final decrementam nr de taskuri din pool
