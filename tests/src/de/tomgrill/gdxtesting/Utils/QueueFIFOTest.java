package de.tomgrill.gdxtesting.Utils;
import com.mygdx.utils.QueueFIFO;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(GdxTestRunner.class)
public class QueueFIFOTest {

    @Test
    public void size() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));
        assertAll(() -> assertEquals(tQueueSize0.size(), 0, "Failed to find Empty Queue size"),
                () -> assertEquals(tQueueSize3.size(), 3, "Failed to find size for Queue (1,2,3)"));
    }

    @Test
    public void isEmpty() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        assertAll(() -> assertTrue(tQueueSize0.isEmpty(), "Empty List is not empty"),
                () -> assertFalse(tQueueSize3.isEmpty(), "List of items (1,2,3) is empty"));
    }

    @Test
    public void contains() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        assertAll(() -> assertTrue(tQueueSize3.contains(1), "FIFO Queue (1,2,3) doesn't contain 1"),
                () -> assertTrue(tQueueSize3.contains(2), "FIFO Queue (1,2,3) doesn't contain 2"),
                () -> assertTrue(tQueueSize3.contains(3), "FIFO Queue (1,2,3) doesn't contain 3"),
                () -> assertFalse(tQueueSize3.contains(7), "FIFO Queue (1,2,3) contains 7"),
                () -> assertFalse(tQueueSize0.contains(1), "FIFO Queue () contains 1"));
    }

    @Test
    public void add() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());
        tQueueSize0.add(1);

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));
        tQueueSize3.add(4);

        assertAll(() -> assertEquals(tQueueSize0.get(), new ArrayList<>(Collections.singletonList(1)), "FIFO Queue () did not have value 1 added to it."),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2, 3, 4)), "FIFO Queue (1,2,3) did not have value 4 added to it."));
    }

    @Test
    public void removeItem() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList("x", "y", "z")));

        String x = "x";
        String a = "a";

        assertAll(() -> assertTrue(tQueueSize3.remove(x), "Can't remove present value."),
                () -> assertEquals(new ArrayList<>(Arrays.asList("y", "z")), tQueueSize3.get(), "Value was not removed from list."),
                () -> assertFalse(tQueueSize3.remove(a), "Can remove nonexistent value."),
                () -> assertEquals(new ArrayList<>(Arrays.asList("y", "z")), tQueueSize3.get(), "Queue is different after an unsuccessful remove attempt."),
                () -> assertFalse(tQueueSize0.remove(x), "Empty List shouldn't have anything to remove."),
                () -> assertEquals(new ArrayList<>(Arrays.asList()), tQueueSize0.get(), "Empty array has changed after a failed removal."));


    }

    @Test
    public void RemoveViaIndex() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        tQueueSize3.remove(2);
        tQueueSize0.remove(1);

        assertAll(() -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2)), "Can't remove present value via index."),
                () -> assertEquals(tQueueSize0.get(), new ArrayList<>(), "Empty list removal error."),
                () -> assertEquals(1, tQueueSize3.getI()),
                () -> assertEquals(-1, tQueueSize0.getI()));
    }


    @Test
    public void addAll() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        Collection<Object> toAdd = new ArrayList<>(Arrays.asList(1, 2, 3));

        assertAll(() -> assertTrue(tQueueSize0.addAll(toAdd), "Values were not added to empty FIFO Queue"),
                () -> assertEquals(tQueueSize0.get(), new ArrayList<>(Arrays.asList(1, 2, 3)), "Empty list with new values not equal to expected list (1,2,3)"),
                () -> assertTrue(tQueueSize3.addAll(toAdd), "Values were not added to FIFO Queue size 3"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2, 3, 1, 2, 3)), "Empty list with new values not equal to expected list (1,2,3,1,2,3)"));

    }

    @Test
    public void removeAll() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        Collection<Object> toRemove = new ArrayList<>(Arrays.asList(1, 2));

        assertAll(() -> assertFalse(tQueueSize0.removeAll(toRemove), "All values were somehow removed from an empty list"),
                () -> assertEquals(tQueueSize0.get(), new ArrayList<>(), "Empty list with values removed not empty"),
                () -> assertTrue(tQueueSize3.removeAll(toRemove), "Values were not removed from FIFO Queue size 3"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(new ArrayList<>(Collections.singletonList(3))), "List size 3 not reduced to list (3)"));

    }

    @Test
    public void retainAll() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        QueueFIFO<Object> tQueueSize5 = new QueueFIFO<>();
        tQueueSize5.set(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));

        Collection<Object> toRetain = new ArrayList<>(Arrays.asList(1, 2, 4));

        assertAll(() -> assertFalse(tQueueSize0.retainAll(toRetain), "Values empty list never had were retained"),
                () -> assertTrue(tQueueSize3.retainAll(toRetain), "Values list size 3 had were not retained"),
                () -> assertTrue(tQueueSize5.retainAll(toRetain), "Values list size 3 had were not retained"),
                () -> assertEquals(tQueueSize0.get(), new ArrayList<>(), "Empty List is not empty"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2)), "Values (1,2) were not retained"),
                () -> assertEquals(tQueueSize5.get(), new ArrayList<>(Arrays.asList(1, 2, 4)), "Values (1,2,4) were not retained"));
    }

    @Test
    public void clear() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        tQueueSize0.clear();
        tQueueSize3.clear();

        assertAll(() -> assertEquals(tQueueSize0.get(), new ArrayList<>(), "Cleared Empty List is not empty"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(), "Cleared List is not empty"));
    }


    @Test
    public void pop() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));


        try {
            tQueueSize0.pop();
        } catch (Throwable t) {
            assertInstanceOf(RuntimeException.class, t, "Runtime exception not thrown under correct circumstances");
        }
        assertAll(() -> assertEquals(tQueueSize3.pop(), 3, "Incorrect value popped"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2)), "Queue not correct after popping"),
                () -> assertEquals(tQueueSize3.getI(), 1, "Index not correct post-pop"));
    }

    @Test
    public void poll() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        assertAll(() -> assertNull(tQueueSize0.poll(), "Incorrect value popped"),
                () -> assertEquals(tQueueSize3.poll(), 3, "Queue not correct after popping"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2)), "Queue not correct after popping"),
                () -> assertEquals(tQueueSize3.getI(), 1, "Index not correct post-pop"));
    }

    @Test
    public void element() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        try {
            tQueueSize0.element();
        } catch (Throwable t) {
            assertInstanceOf(RuntimeException.class, t, "Runtime exception in incorrect circumstances");
        }
        assertAll(() -> assertEquals(tQueueSize3.element(), 3, "Incorrect Value"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2, 3)), "Queue modified at incorrect point"));
    }

    @Test
    public void peek() {
        QueueFIFO<Object> tQueueSize0 = new QueueFIFO<>();
        tQueueSize0.set(new ArrayList<>());

        QueueFIFO<Object> tQueueSize3 = new QueueFIFO<>();
        tQueueSize3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        try {
            tQueueSize0.element();
        } catch (Throwable t) {
            assertInstanceOf(RuntimeException.class, t, "Runtime exception in incorrect circumstances");
        }
        assertAll(() -> assertNull(tQueueSize0.peek(), "empty list not empty"),
                () -> assertEquals(tQueueSize3.peek(), 3, "Incorrect Value"),
                () -> assertEquals(tQueueSize3.get(), new ArrayList<>(Arrays.asList(1, 2, 3)), "Queue modified at incorrect point"));
    }
}
