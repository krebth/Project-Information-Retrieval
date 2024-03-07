# Project-Information-Retrieval

Athanasios Krevvatas, 4958 Konstantinos Koutsidis, 5114

1) To συστήμα μας αποτελεί μια μηχανή αναζήτησης τραγουδιών με βάση κάποιες λέξεις – κλειδιά που θα δίνει ο
χρήστης. Η αναζήτηση αυτή μπορεί να γίνει σε διάφορα σημεία με βάση τις επιλογές του χρήστη.
Πιο συγκεκριμένα, η αναζήτηση μπορεί να γίνει είτε με βάση τον τίτλο των τραγουδιών ,το νούμερο του
τραγουδιού, το όνομα του καλλιτέχνη, το άλμπουμ, την χρονιά κυκλοφορίας ή την ακριβή ημερομηνία
και τους στίχους του εκάστοτε τραγουδιού είτε συνδυασμούς των παραπάνω λέξεων-κλειδιών.
Ο χρήστης πρέπει να επιλέξει ένα αρχείο της μορφής .csv (file -> open) π.χ σαν το songs.csv που έχει δοθεί στο κατάλογο files.
Το σύστημα αναζητά και επιστρέφει τα τραγούδια που περιέχουν τις λέξεις που δίνονται από τον
χρήστη ως input. Τέλος ο χρήστης έχει την δυνατότητα να δει το ιστορικό των αναζητήσεων του καθώς
και να ομαδοποιήσει τα αποτελέσματα της αναζήτησης όχι με βάση την συνάφεια αυτή την φορά αλλά
με βάση την χρονολογία ή τον αριθμό του τραγουδιού.Τα αποτελασματα της αναζητησης παρουσιάζονται ανα δεκα 
με δυνατοτητα αλλαγης σελιδας(Next,Pev).

2) Προσπαθήσαμε να δώσουμε την δυνατότητα στο χρήστη να πραγματοποιεί αναζήτηση με διάφορους
τρόπους επιτρέποντας τον να αναζητεί τραγούδια με βάση τα πεδία(field,value). 
Αναλυτικότερα δημιουργήσαμε ένα interface μέσω του οποίου αρχικά ο χρήστης πρέπει να φορτώσει ένα αρχείο της μορφής .csv (file -> open)
π.χ σαν το songs.csv που έχει δοθεί στο κατάλογο files.  
Στη συνέχεια ο χρήστης έχει την δυνατότητα να προσθέσει κάποια πεδία _**fields**_ π.χ Title,Artist,Year χωρισμένα με κόμμα και κάποιες
τιμές (λέξεις-κλειδιά) π.χ Let,ariana,2015 και πατώντας το κουμπί _**search**_ θα του εμφανιστούν **bold** τα
αποτελέσματα στα οποία εμφανίζονται αυτά που έχει επιλέξει να αναζητήσει κάθε φορά. Ουσιαστικά
παρέχουμε την δυνατότητα στον χρήστη να αναζητεί ταυτόχρονα **περισσότερα από ένα πεδία** κάθε
φορά. Ακόμα κατά την αναζήτηση ο χρήστης _μπορεί να γράψει όπως εκείνος θέλει_ τα πεδία και τις
**λέξεις-κλειδιά** χωρίς να επηρεάζεται το αποτέλεσμα της αναζήτησης. Για παράδειγμα τα πεδία **_fields_**
μπορούν να γραφτούν έτσι : TiTLE,ARtist,YEAR και οι **_λέξεις-κλειδιά(Value)_** έτσι : LeT,arIaNa,2015 χωρίς
να αλλάζουν τα αποτελέσματα της αναζήτησης. Επίσης παρέχουμε και ένα **ιστορικό αναζητήσεων**
ώστε ο χρήστης να έχει την δυνατότητα να ανατρέξει σε προηγούμενες αναζητήσεις ή να μπορεί να
ελέγξει και να διορθώσει τυχόν ορθογραφικά λάθη κατά την αναζήτηση που πραγματοποίησε. 
Σε περίπτωση που ο χρήστης δεν συμπληρώσει κάποιο πεδίο αναζήτησης(fields , value) η αναζήτηση
θα ολοκληρωθεί κανονικά χωρίς κάποιο πρόβλημα. Απλά αναμενόμενα δεν θα εμφανιστεί τίποτα. Ο χρήστης έχει την δυνατότητα
να αλλάξει το ήδη επιλεγμένο αρχείο (file -> open) και να πραγματοποιήσει αναζήτηση σε κάποιο άλλο.
**Τέλος πρέπει να επισημάνουμε οτι τα πεδία Fields και Values θα πρέπει να χωρίζονται υποχρεωτικά με κόμμα.**


1) Our system is a song search engine based on some keywords that will be given by the
user. This search can be done in different places based on the user's choices.
More specifically, the search can be done either based on the title of the songs ,the number of the
song number, the name of the artist, the album, the year of release or the exact date
and the lyrics of each song or combinations of the above keywords.
The user has to select a file of the .csv format (file -> open) e.g. like songs.csv given in the files directory.
The system searches for and returns the songs containing the words given by the
user as input. Finally the user has the possibility to see the history of his searches as
and to group the search results not by relevance this time but
the results of the search are presented in ten different ways, according to the date or number of the song. 
with the possibility of changing pages (Next,Pev).

2) We have tried to give the user the ability to search with various
ways by allowing the user to search for songs by field(field,value). 
More specifically we created an interface through which the user must first load a .csv file (file -> open)
e.g. as songs.csv given in the files directory.  
Then the user is able to add some _**fields**_ e.g. Title,Artist,Year separated by comma and some
values (keywords) e.g. Let,ariana,2015 and pressing the _**search**_ button will show him **bold** the
results showing the ones he has chosen to search each time. Essentially
provide the user with the ability to search **more than one field** at a time for each
at a time. Even when searching, the user _may_ write as he or she wishes the fields and
**keywords** without affecting the result of the search. For example, the **_fields_**
can be written like this : TiTLE,ARtist,YEAR and **_keywords(Value)_** like this : LeT,arIaNa,2015 without
without changing the search results. We also provide a **search history**
so that the user has the ability to refer to previous searches or can
check and correct any spelling mistakes during the search he/she has made.
In case the user does not fill in a search field (fields , value) the search
will be completed normally without any problem. Just as expected nothing will be displayed. The user has the possibility
to change the already selected file (file -> open) and search in another one.
**Finally we have to point out that the Fields and Values fields must be separated by commas.


