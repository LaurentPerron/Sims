/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.*;

//Tas binaire générique
public class Heap<T> {

    private T[] H;
    private int last;
    private int lengthInit = 1000;
    private final Comparator<T> COMP;

    // On fera plutôt une implantation sans pointeur avec un tableau
    // pour un noeud i, le parent est a [i/2] et son enfant gauche à [2i]
    // droit à [2i + 1]
    @SuppressWarnings("unchecked") //Tableau générique
    public Heap(Comparator<T> c) {
        H = (T[]) new Object[lengthInit];
        last = 0;
        COMP = c;
    }

    // le premier index occupe du tableau est l'index 1
    public void insert(T o) {
        if (last == 0) {
            H[1] = o;
        } else
            swim(o, last + 1);

        last++;
        realloc();
    }

    public void swim(T o, int i) {
        int p = (int) i / 2;

        // Si élément en place est plus grand que l'element a ajouter je continu
        // Ou j'arrive a la racine
        while (p != 0 && (this.COMP.compare(H[p], o)) > 0) {

            // L'élément a la position p descend dans l'arbre a
            // la position de son enfant en partant par la fin de l'arbre
            // on s'assure que le premier passage envoie H[p]
            // a la position de last+1 qui est vide
            H[i] = H[p];
            i = p;
            p = (int) i / 2;
        }
        H[i] = o;
    }

    public T getMin() {
        return H[1];
    }

    public T deleteMin() {
        if (!isEmpty()) {
            // Sort la position 1 -> contient le min
            T result = H[1];

            // On enleve l'element a la derniere place pour pouvoir le replacer
            // et pouvoir replacer le nouveau minimum.
            T last = H[this.last];
            H[this.last] = null;

            if (this.last-- > 1) {
                sink(last, 1);
            }
            realloc();
            return result;
        } else
            return null;
    }

    public void sink(T o, int i) {
        int min = minChild(i);

        // Tant que l'element a la position i a des enfants ou que ou que
        // son plus petit enfant est plus petit que l'objet a inserer
        // on continue
        while (min != 0 && this.COMP.compare(H[min], o) < 0) {

            // l'enfant minimal prend la place de son parent et on descend
            // dans l'arbre a la recherche d'un encroit ou deposer l'objet
            H[i] = H[min];
            i = min;
            min = minChild(i);
        }
        H[i] = o;
    }

    // Retourne l'enfant le plus petit si l'element n'a
    // pas d'enfant on retourne 0
    public int minChild(int i) {

        // int result;
        if (2 * i > last)
            return 0;

        // On veut savoir si l'element a 2i+1 < 2i
        else if (2 * i + 1 <= last
                 && this.COMP.compare((T) H[2 * i + 1], (T) H[2 * i]) < 0)
            return 2 * i + 1;

        else
            return 2 * i;
    }

    public boolean isEmpty() {
        if (last == 0)
            return true;

        else
            return false;
    }

    // Méthode qui gère la longueur du tableau de travail
    // Double la capacité si overflow
    // Divise la capacité par deux si seulement la moitié du tableau est
    // remplie et si le tableau est plus grand que la capacité initiale
    public void realloc() {
        if (last >= H.length - 1) {
            H = Arrays.copyOf(H, H.length * 2);
        } else if (H[H.length / 4] == null && H.length > lengthInit) {
            H = Arrays.copyOf(H, H.length / 2);
        }
    }

    //Retourne le pointeur vers le tableau des éléments
    public T[] getArray() {
        return H;
    }

    public int getLast() {
        return last;
    }
}
