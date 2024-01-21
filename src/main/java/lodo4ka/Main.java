package lodo4ka;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        System.out.println(Arrays.toString(args));

        Util util = new Util();
        util.filter(args);

    }
}