import java.util.Scanner;

class NumbersFilter extends Thread {

    /* use it to read numbers from the standard input */
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        // implement this method
        while (scanner.hasNext()) {
            int n = Integer.parseInt(scanner.nextLine());
            if (n == 0) {
                return;
            }
            if (n % 2 == 0) {
                System.out.println(n);
            }
        }
    }
}