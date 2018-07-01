package lightriders.ai;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

class TreeHeightSearchTest {

	@Test
	void testAllTied() {
		TreeHeightSearch<Integer> search = new TreeHeightSearch<>(1, i -> {
			if (i != 6) {
				fail();
			}
			return new Integer[] { 1, 2, 3, 4, 5 };
		}, i -> 5);
		int maxIndex = search.highestSubtree(6);
		assertTrue(0 <= maxIndex);
		assertTrue(maxIndex <= 4);
	}

	@Test
	void testBaseCaseDepth1() {
		TreeHeightSearch<Integer> search = new TreeHeightSearch<>(1, i -> {
			if (i != 6) {
				fail();
			}
			return new Integer[] { 1, 2, 3, 4, 5 };
		}, Function.identity());
		int maxIndex = search.highestSubtree(6);
		assertEquals(4, maxIndex);
	}

	@Test
	void testNoDepthCutoff() {
		TreeHeightSearch<Integer> search = new TreeHeightSearch<>(0, i -> {
			if (i == 6) {
				return new Integer[] { 1, 2, 5, 4, 3 };
			} else if (i == 0) {
				return new Integer[] {};
			}
			return new Integer[] { i - 1 };
		}, i -> {
			fail();
			return 1;
		});
		int maxIndex = search.highestSubtree(6);
		assertEquals(2, maxIndex);
	}

}
