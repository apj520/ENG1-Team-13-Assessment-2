import com.mygdx.game.MyUnit;
import org.junit.Test;
import static org.junit.Assert.*;

//Test to check if CI works:
public class MyUnitTest {


    @Test
    public void testConcatenate() {
        MyUnit myUnit = new MyUnit();

        String result = myUnit.concatenate("one", "two");

        assertEquals("onetw", result);

    }
}