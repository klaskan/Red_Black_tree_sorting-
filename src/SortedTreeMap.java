import com.sun.tools.javac.code.Scope;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.lang.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SortedTreeMap<K extends Comparable<? super K>,V> implements ISortedTreeMap<K ,V>{

    // fields
    private Node<K,V> root;
    private int size = 0;
    private Comparator<? super K> cmp;

    //Konstruktør
    public SortedTreeMap() {
        root = null;
        //cmp = null;
    }
    //Konstruktør
    public SortedTreeMap(Comparator<? super K> c){
        root = null;
        //cmp = c;
    }



    public void leftRotate(Node<K, V> x) { // roter x til venstre
        Node<K, V> y = x.right;
        x.right = y.left;
        if(y.left != null){
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            assert this.root == x;
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        // x flyttes ned
        y.left = x;
        // x flyttes ned
        x.parent = y;
    }


    public void rightRotate(Node<K, V> y) { // roter y til høyre
        Node<K, V> x = y.left;
        y.left = x.right;
        if(x.right != null){
            x.right.parent = y;
        }
        x.parent = y.parent;

        if (y.parent == null) {
            assert this.root == y;
            this.root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        // Sett x sitt høyre barn til y
        x.right = y;
        //sett y sin forelder til x
        y.parent = x;
    }


    public Node<K, V> find(K key){
        Node<K, V> x = this.root;
        while(x != null){
            if(key.compareTo(x.key) == 0){
                return x;
            }else if(key.compareTo(x.key) == -1){
                x = x.left;
            }else{
                x = x.right;
            }
        }
        return null;
    }



    @Override
    public Entry<K, V> min() {
        Node x = this.root;
        if(x == null) return null;
        while(x.left != null) x = x.left;
        Entry test = new Entry(x.key, x.value);
        return test;
    }


    @Override
    public Entry<K,V> max() {
        Node x = this.root;
        if(x == null) return null;
        while(x.right != null) x = x.right;
        Entry test = new Entry(x.key, x.value);
        return test;
    }


    @Override
    public V add(K key, V value) {

            V returnValue = null;

            if (key == null) {
                return null;
            }
            if(containsKey(key)){
                returnValue = getValue(key);
            }


            Node<K, V> y = null;
            Node<K, V> x = this.root;
            while (x != null) {
                y = x;

                if (key.compareTo(x.key) == -1) {
                    x = x.left;
                } else if (key.compareTo(x.key) == 1) {
                    x = x.right;
                } else {
                    x.value = value;
                    return returnValue;
                }
            }

            Node<K, V> z = new Node<>(Color.RED, key, value);
            z.parent = y;

            if (y == null) {
                root = z;
            } else if (z.key.compareTo(y.key) == -1) {
                y.left = z;
            } else {
                y.right = z;
            }
            insertFixup(z);
            this.size++;
            return returnValue;

    }


    public void insertFixup(Node<K, V> z) {
        while (z.parent != null && z.parent.parent != null && z.parent.color == Color.RED) {
            if (z.parent == z.parent.parent.left) {
                Node<K, V> y = z.parent.parent.right;
                if (y != null && y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node<K, V> y = z.parent.parent.left;
                if (y != null && y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        this.root.color = Color.BLACK;
    }



    @Override
    public V add(Entry<K, V> entry) {
        return add(entry.key, entry.value);
    }

    @Override
    public void replace(K key, V value) throws NoSuchElementException {
        if(find(key) != null){
            find(key).value = value;
        }else{
            throw new NoSuchElementException();
        }

    }

    /**
     * IKKE GODKJENT!
     * @param key The key for which we are replacing the value
     * @param f The function to apply to the value
     * @throws NoSuchElementException
     */

    @Override
    public void replace(K key, BiFunction<K,V,V> f) throws NoSuchElementException {
        if(this.containsKey(key)){
            Node<K, V> test = find(key);
            V value = f.apply(test.key, test.value);
            add(key, value);
        }else{
            throw new NoSuchElementException();
        }
    }


    public Node<K, V> getMinimum(Node<K, V> x){
        if(x == null) return null;
        while(x.left != null) x = x.left;
        return x;
    }

    public Node<K, V> getSuccessor(Node<K, V> x){
        if(x == null) return null;
        if(x.right != null){
            return getMinimum(x.right);
        }
        Node<K, V> y = x.parent;
        while(y != null && x == y.right){
            x = y;
            y = y.parent;
        }
        return y;
    }




    @Override
    public V remove(Object key) throws NoSuchElementException {
        K key2 = (K) key;

        if(key == null){
            return null;
        }
        //Node<K, V> z = this.find((K) key);
        Node<K, V> z = this.find(key2);
        if(z == null){
            throw new NoSuchElementException();
        }
        V removedVal = z.value;
        Node<K, V> y = null;
        if(z.left == null || z.right == null){
            y = z;
        }else{
            y = getSuccessor(z);
        }
        Node<K, V> x = null;
        if(y.left != null){
            x = y.left;
        }else{
            x = y.right;
        }
        if(x != null)x.parent = y.parent;

        if(y.parent == null){
            this.root = x;
        }else if(y == y.parent.left){
            y.parent.left = x;
        }else{
            y.parent.right = x;
        }

        if(y != z){
            z.key = y.key;
            z.value = y.value;
        }

        if(y.color == Color.BLACK){
            deleteFixup(x);
        }
        size = size - 1;
        return removedVal;

    }


    public void deleteFixup(Node<K, V> x){
        if(x == null) return;
        Node<K, V> w = null;

        while(x != this.root && x.color == Color.BLACK){
            if(x == x.parent.left){
                w = x.parent.right;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if((w.left == null || w.left.color == Color.BLACK) && (w.right == null || w.right.color == Color.BLACK)){
                    w.color = Color.RED;
                    x = x.parent;
                }else if(w.right == null || w.right.color == Color.BLACK){
                    if(w.left != null)w.left.color = Color.BLACK;
                    w.color = Color.RED;
                    rightRotate(w);
                    w = x.parent.right;
                    x.parent.color = Color.BLACK;
                    if(w.right != null)w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = this.root;
                }
            }else{
                w = x.parent.left;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if((w.right == null ||w.right.color == Color.BLACK) && (w.left == null || w.left.color == Color.BLACK)){
                    w.color = Color.RED;
                    x = x.parent;
                }else if(w.left == null || w.left.color == Color.BLACK){
                    if(w.right != null)w.right.color = Color.BLACK;
                    w.color = Color.RED;
                    leftRotate(w);
                    w = x.parent.left;
                    x.parent.color = Color.BLACK;
                    if(w.left != null)w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = this.root;
                }
            }
        }
        x.color = Color.BLACK;
    }


    @Override
    public V getValue(Object key) throws NoSuchElementException {
        Node<K, V> x = this.root;
        K midl = (K) key;
        while(x != null){
            if(midl.compareTo(x.key) == 0){
                return x.value;
            }else if(midl.compareTo(x.key) == -1){
                x = x.left;
            }else{
                x = x.right;
            }
        }
        throw new NoSuchElementException();

    }

    @Override
    public boolean containsKey(K key) {
        if(find(key) != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * FIKS
     * @param value the value to look for
     * @return
     */

    @Override
    public boolean containsValue(Object value) {
        Iterable values = this.values();
        Iterator it = values.iterator();
        while (it.hasNext()){
            Object verdi = it.next();
            if(verdi.equals(value)){
                return true;
            }

        }
        return false;

    }

    @Override
    public Iterable<K> keys() {
        Stack<K> keys = new Stack<>();
        Node x = root;
        if(x == null){
            return keys;
        }
        Node current = x.getLeftMost();
        while (current != null) {
            keys.push((K) current.key);
            current = current.getNext();
        }
        return keys;
    }


    @Override
    public Iterable<V> values() {
        Stack<V> values = new Stack<>();
        Node x = root;
        if(x==null){
            return values;
        }
        Node current = x.getLeftMost();
        while (current != null){
            values.push((V)current.value);
            current = current.getNext();

        }
        return values;
    }



    @Override
    public Iterable<Entry<K, V>> entries() {
        Stack<Entry<K, V>> entrieStack = new Stack<>();
        Node x = root;
        if(x == null){
            return entrieStack;
        }
        Node current = x.getLeftMost();
        while (current != null) {
            entrieStack.push(new Entry(current.key, current.value));
            current = current.getNext();
        }
        return entrieStack;

    }

    @Override
    public Entry<K,V> higherOrEqualEntry(K key) {
        Node find;
        Stack<Entry<K, V>> stackEntries = new Stack<>();
        Iterable entriesIt = entries();
        Iterator<Entry<K, V>> itre = entriesIt.iterator();

        //Fylller stack med verdier.
        while (itre.hasNext()){
            stackEntries.push(itre.next());
        }

        if(stackEntries.size() == 0){
            return null;
        }

        //Dersom key allerede finnes i tree
        if(find(key) != null){
            find = find(key);
            return new Entry(find.key, find.value);
        }
        //dersom key er over verdien på den høyeste keyen
        if(stackEntries.get(stackEntries.size()-1).key.compareTo(key) < 0){
            return null;
        }

        //dersom key ikke er i listen
        if(find(key) == null){
            Iterable entries = entries();
            Iterator<Entry<K, V>> it = entries.iterator();
            while (it.hasNext()){
                Entry<K, V> ent = it.next();
                if(ent.key.compareTo(key) > 0){
                    return ent;
                }
            }
        }
        return null;

    }


    @Override
    public Entry<K, V> lowerOrEqualEntry(K key) {
        Node find;
        Iterable entriesIt = entries();
        Iterator<Entry<K, V>> itre = entriesIt.iterator();
        Stack<Entry<K, V>> stackEntries = new Stack<>();
        while (itre.hasNext()){
            stackEntries.push(itre.next());
        }

        if(stackEntries.size() == 0){
            return null;
        }

        //Dersom key allerede finnes i tree
        if(find(key) != null){
            find = find(key);
            return new Entry(find.key, find.value);
        }
        //dersom key er over verdien på den høyeste keyen
        if(stackEntries.elementAt(0).key.compareTo(key) >= 0){
            return null;
        }

        //dersom key ikke er i treet
        if(find(key) == null){
            for(int i = stackEntries.size()-1; i >= 0; i--){
                if(stackEntries.get(i).key.compareTo(key) < 0){
                    return stackEntries.get(i);
                }
            }

        }
        return null;
    }

    @Override
    public void merge(ISortedTreeMap<K, V> other) {
        Iterable entries = other.entries();
        Iterator it = entries.iterator();
        while (it.hasNext()){
            this.add((Entry<K, V>)it.next());
        }
    }



//
//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        Iterable entriesIt = entries();
//        Iterator<Entry<K, V>> it = entriesIt.iterator();
//        if(!it.hasNext()){
//            return;
//        }
//        while(it.hasNext()){
//            Entry<K, V> mid = it.next();
//            if(p.test(mid.key, mid.value)){
//                remove(mid.key);
//            }
//            it.next();
//        }
//    }




//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        Iterable entriesIt = entries();
//        Iterator<Entry<K, V>> it = entriesIt.iterator();
//        if(!it.hasNext()){
//            return;
//        }
//        while(it.hasNext()){
//            Entry<K, V> mid = it.next();
//            if(p.test(mid.key, mid.value)){
//                remove(mid.key);
//            }
//            it.next();
//        }
//    }





//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        //Flytter alt over i stack
//        Iterable entriesIt = entries();
//        Iterator<Entry<K, V>> itre = entriesIt.iterator();
//        Stack<Entry<K, V>> stackEntries = new Stack<>();
//        while (itre.hasNext()){
//            stackEntries.push(itre.next());
//        }
//
//        if(stackEntries.size() >= 0){
//            return;
//        }
//
//        while (itre.hasNext()){
//            if(p.test(itre.next().key, itre.next().value)){
//                remove(itre.next().key);
//            }
//        }
//
//
//
//    }



//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        //Flytter alt over i arraylist
//        Iterable entriesIt = entries();
//        Iterator<Entry<K, V>> itre = entriesIt.iterator();
//        Stack<Entry<K, V>> stackEntries = new Stack<>();
//        while (itre.hasNext()){
//            stackEntries.push(itre.next());
//        }
//
//        if(stackEntries.size() >= 0){
//            return;
//        }
//
//        for(int i = 0; i < stackEntries.size(); i++){
//            if(p.test(stackEntries.get(i).key, stackEntries.get(i).value)){
//                remove(stackEntries.get(i).key);
//
//        }
//
//    }



    //Evig loop
//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        //Flytter alt over i Stack
//        Iterable entriesIt = entries();
//        Iterator<Entry<K, V>> itre = entriesIt.iterator();
//        Stack<Entry<K, V>> stackEntries = new Stack<>();
//        while (itre.hasNext()){
//            stackEntries.push(itre.next());
//        }
//
//        for(Entry<K, V> ent : stackEntries){
//            if (p.test(ent.key, ent.value)){
//                remove(ent.key);
//            }
//        }
//    }

//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        //Flytter alt over i stack
//        Iterable entriesIt = entries();
//        Iterator<Entry<K, V>> itre = entriesIt.iterator();
//        Stack<Entry<K, V>> stackEntries = new Stack<>();
//        while (itre.hasNext()){
//            stackEntries.push(itre.next());
//        }
//        if(stackEntries.size() <= 0){
//            return;
//        }
//
//        //Tar i bruk BiPredicate og test om element er true, for så å fjerne det elementet som er lik true.
//        for(int i = 0; i < stackEntries.size(); i++){
//            if(p.test(stackEntries.get(i).key, stackEntries.get(i).value)){
//                stackEntries.remove(i);
//            }
//        }
//        //Klarerer treet
//        clear();
//        //Fyller på med nye elementer i treet.
//        for(Entry<K,V> t: stackEntries) {
//            add(t);
//        }
//    }


    @Override
    public void removeIf(BiPredicate<K, V> p) {
        //Flytter alt over i arraylist
        Iterable entriesIt = entries();
        Iterator<Entry<K, V>> itre = entriesIt.iterator();
        Stack<Entry<K, V>> stackEntries = new Stack<>();

        //Testet. legger alle elementene fra treet i stakEntries.
        while (itre.hasNext()){
            stackEntries.push(itre.next());
        }
        //Testet. Legger alle elementene fra stackEntery til i stackEntry2
        Stack<Entry<K, V>> stackEntries2 = new Stack<>();
        for(int i = 0; i < stackEntries.size(); i++){
            stackEntries2.push(stackEntries.get(i));
        }
        //Tar i bruk BiPredicate og test om element er true, for så å fjerne det elementet som er lik true.
        for(Entry<K, V> e : stackEntries2){
            if(p.test(e.key, e.value)){
                stackEntries.remove(e);
            }
        }

        clear();
        for(Entry<K, V> e : stackEntries){
            add(e);
        }



    }





//    @Override
//    public void removeIf(BiPredicate<K, V> p) {
//        Iterable entriesI = entries();
//        Iterator<Entry<K,V>> it = entriesI.iterator();
//        Stack<Entry<K, V>> stackEntry = new Stack<>();
//
//        while (it.hasNext()){
//            stackEntry.add(it.next());
//        }
//
//        Iterator stackIt = stackEntry.iterator();
//
//        while (stackIt.hasNext()) {
//            if(p.test(stackI))
//        }
//
//    }


    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int size() {
            return this.size;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;

    }

}
