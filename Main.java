import java.util.TreeMap;

public class Main{

    public static void main(String[] args) {
        

        BinaryTree<Integer, String> tree = new BinaryTree<>();
        System.out.println(tree.insert(3, "hi"));
        System.out.println(tree.insert(4, "Bye"));
        System.out.println(tree.insert(1, "sun"));
        System.out.println(tree.insert(9, "bull"));
        System.out.println(tree.insert(7, "redbull"));
        System.out.println(tree.insert(3, "goodbye"));





    }
}