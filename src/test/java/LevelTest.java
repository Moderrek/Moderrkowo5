public class LevelTest {

    public static void main(String[] args) {
        for (int i = 1; i <= 20; i += 1) {
            double exp = Math.round(Math.pow(Math.pow(i, 3), 0.33) * 80);
            System.out.println(exp);
        }
    }

}
