package lightriders.engine;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeAllocationTest {

	private TimeAllocation allocator;

	@BeforeEach
	void setup() {
		allocator = new TimeAllocation();
	}

	@Test
	void testLessThanGivenPerMove() {
		assertEquals(1, allocator.allocateEvenly(50, 1, 2, 16, 16));
		assertEquals(2, allocator.allocateEvenly(51, 2, 2, 16, 16));
	}

	@Test
	void testTwoRoundGame() {
		assertEquals(6, allocator.allocateEvenly(0, 10, 2, 6, 1));
		assertEquals(6, allocator.allocateEvenly(1, 6, 2, 6, 1));
	}

	@Test
	void testFourRoundGame() {
		assertEquals(4, allocator.allocateEvenly(0, 10, 2, 5, 2));
		assertEquals(4, allocator.allocateEvenly(1, 8, 2, 5, 2));
		assertEquals(4, allocator.allocateEvenly(2, 6, 2, 5, 2));
		assertEquals(4, allocator.allocateEvenly(3, 4, 2, 5, 2));
	}

	@Test
	void testUnevenSplit() {
		Set<Integer> times = new HashSet<>();
		Set<Integer> expected = new HashSet<>(Arrays.asList(4, 4, 5));
		int t1 = allocator.allocateEvenly(0, 10, 2, 8, 1);
		times.add(t1);
		int t2 = allocator.allocateEvenly(1, 10 + 2 - t1, 2, 8, 1);
		times.add(t2);
		int t3 = allocator.allocateEvenly(2, 10 + 4 - t1 - t2, 2, 8, 1);
		times.add(t3);
		assertEquals(expected, times);
	}

}
