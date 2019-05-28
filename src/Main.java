import org.omg.CORBA.SetOverrideType;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.function.BiPredicate;


public class Main {

    public static void main(String[] args) {

        SortedTreeMap<Integer, String> sTreeMap = new SortedTreeMap<>();

        sTreeMap.add(50, "femti");
        sTreeMap.add(100, "hundre");
        sTreeMap.add(200, "2hundre");
        sTreeMap.add(300, "3hundre");
        sTreeMap.add(400, "4hundre");

        //sTreeMap.remove(100);
        //System.out.println(sTreeMap.containsKey(100));
        //System.out.println(sTreeMap.size());

        BiPredicate<Integer, String> bi = (x, y) -> x <= 100;//y.equals("hundre");


            //Flytter alt over i arraylist
            Iterable entriesIt = sTreeMap.entries();
            Iterator<Entry<Integer, String>> itre = entriesIt.iterator();
            Stack<Entry<Integer, String>> stackEntries = new Stack<>();

            //Testet. legger alle elementene fra treet i stakEntries.
            while (itre.hasNext()){
                stackEntries.push(itre.next());
            }
            //Testet. Legger alle elementene fra stackEntery til i stackEntry2
            Stack<Entry<Integer, String>> stackEntries2 = new Stack<>();
            for(int i = 0; i < stackEntries.size(); i++){
                stackEntries2.push(stackEntries.get(i));
            }


            //Tar i bruk BiPredicate og test om element er true, for så å fjerne det elementet som er lik true.
            for(Entry<Integer, String> e : stackEntries2){
                if(bi.test(e.key, e.value)){
                    stackEntries.remove(e);
                }
            }
        System.out.println(stackEntries.size());




        for(Entry<Integer,String> t: stackEntries){
            System.out.println(t.key + " : " + t.value);
        }







//        Entry test = sTreeMap.higherOrEqualEntry(230);
//        System.out.println(test.value);

//        BiPredicate<Integer, String> bi = (x,y) -> x > 200;
//
//        sTreeMap.removeIf(bi);
//
//        Iterator it2 = test2.iterator();
//        while (it2.hasNext()){
//            System.out.println(it.next());
//        }


//        /**
//         * iterator test
//         */
//        Stack<String> test2 = new Stack<>();
//
//        test2.push("hei");
//        test2.push("hei1");
//        test2.push("hei2");
//        test2.push("hei3");
//
//        Iterator it = test2.iterator();
//
//        System.out.println(test2.get(test2.size()-1));

//        while(it.hasNext()){
//            String k = (String)it.next();
//            System.out.println(k);
//            if(k.equals("hei2")){
//                System.out.println("OKOKOKOO");
//            }
//
//        }






    }
}
