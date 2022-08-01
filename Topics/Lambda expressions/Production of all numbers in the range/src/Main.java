import javax.imageio.stream.ImageInputStream;
import java.util.function.LongBinaryOperator;


class Operator {
    public static LongBinaryOperator binaryOperator = (x, y) -> {
        if (x == y) {
            return x;
        }
        long product = 1;
        while (x <= y) {
            product *= x;
            x++;
        }
        return product;
    };
}
