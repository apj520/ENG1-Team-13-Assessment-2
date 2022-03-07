import com.mygdx.game.MyUnit;
import org.junit.Test;
import static org.junit.Assert.*;

//Test to check if CI works:
//Failing test will cause CI build to fail:
public class MyUnitTest {


    @Test
    public void testConcatenate() {
        MyUnit myUnit = new MyUnit();

        String result = myUnit.concatenate("one", "two");

        assertEquals("onetwo", result);

    }
}
