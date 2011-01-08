package org.jhove2.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class CopyUtilsTest {

	protected ArrayList<String> beforeList;
	protected String FIRST = "first";
	protected String SECOND = "second";
	protected String THIRD = "third";
	protected Map<String, Integer> beforeMap;
	protected Map<String, String> beforeStringMap;
	protected SortedSet<String> beforeSet;
	protected String FOURTH = "fourth";
	protected String FIFTH = "fifth";
	protected String SIXTH = "sixth";
	protected ArrayList<String> beforeList2;
	Map<String, List<String>> beforeMapList;
	
	@Before
	public void setUp() throws Exception {
		beforeList = new ArrayList<String>();
		beforeList.add(FIRST);
		beforeList.add(SECOND);
		beforeList.add(THIRD);
		beforeMap = new HashMap<String, Integer>();
		beforeMap.put(FIRST, new Integer(1));
		beforeMap.put(SECOND, new Integer(2));
		beforeMap.put(THIRD, new Integer(3));
		beforeStringMap = new HashMap<String, String>();
		beforeStringMap.put(FIRST, SECOND);
		beforeStringMap.put(SECOND, THIRD);
		beforeStringMap.put(THIRD, FIRST);
		beforeSet = new TreeSet<String>();
		beforeSet.add(FIRST);
		beforeSet.add(SECOND);
		beforeSet.add(THIRD);
		beforeList2 = new ArrayList<String>();
		beforeList2.add(FOURTH);
		beforeList2.add(FIFTH);
		beforeList2.add(SIXTH);
		beforeMapList = new HashMap<String, List<String>>();
		beforeMapList.put(FIRST, beforeList);
		beforeMapList.put(SECOND, beforeList2);
	}

	@Test
	public void testCopyAndClearList() {
		List<String> afterList = null;
		afterList = CopyUtils.copyAndClearList(beforeList);
		assertEquals(3,afterList.size());
		assertEquals(0, beforeList.size());
		assertTrue(afterList.contains(FIRST));
		assertTrue(afterList.contains(SECOND));
		assertTrue(afterList.contains(THIRD));
		
		
	}

	@Test
	public void testCopyAndClearIntMap() {
		Map<String, Integer> afterMap = CopyUtils.copyAndClearIntMap(beforeMap);
		assertEquals(3, afterMap.keySet().size());
		assertTrue(afterMap.containsKey(FIRST));
		assertTrue(afterMap.containsKey(SECOND));
		assertTrue(afterMap.containsKey(THIRD));
		assertEquals(new Integer(1), afterMap.get(FIRST));
		assertFalse(beforeMap.containsKey(FIRST));
		assertFalse(beforeMap.containsKey(SECOND));
		assertFalse(beforeMap.containsKey(THIRD));
		assertEquals(0, beforeMap.keySet().size());
	}

	@Test
	public void testCopyAndClearStringMap() {
		Map<String, String> afterMap = CopyUtils.copyAndClearStringMap(beforeStringMap);
		assertEquals(3, afterMap.keySet().size());
		assertTrue(afterMap.containsKey(FIRST));
		assertTrue(afterMap.containsKey(SECOND));
		assertTrue(afterMap.containsKey(THIRD));
		assertEquals(SECOND, afterMap.get(FIRST));
		assertFalse(beforeStringMap.containsKey(FIRST));
		assertFalse(beforeStringMap.containsKey(SECOND));
		assertFalse(beforeStringMap.containsKey(THIRD));
		assertEquals(0, beforeStringMap.keySet().size());
	}

	@Test
	public void testCopyAndClearListMap() {
		Map<String, List<String>> afterMapList = CopyUtils.copyAndClearListMap(beforeMapList);
		assertEquals(2, afterMapList.keySet().size());
		assertTrue(afterMapList.containsKey(FIRST));
		assertTrue(afterMapList.containsKey(SECOND));
		List<String> afterList1 = afterMapList.get(FIRST);
		assertEquals(3, afterList1.size());
		assertTrue(afterList1.contains(FIRST));
		assertTrue(afterList1.contains(SECOND));
		assertTrue(afterList1.contains(THIRD));
		List<String> afterList2 = afterMapList.get(SECOND);
		assertEquals(3, afterList2.size());
		assertTrue(afterList2.contains(FOURTH));
		assertTrue(afterList2.contains(FIFTH));
		assertTrue(afterList2.contains(SIXTH));
		assertEquals(0, beforeMapList.keySet().size());
	}

	@Test
	public void testCopyAndClearSet() {
		SortedSet<String> afterSet = CopyUtils.copyAndClearSet(beforeSet);
		assertEquals(3, afterSet.size());
		assertTrue(afterSet.contains(FIRST));
		assertTrue(afterSet.contains(SECOND));
		assertTrue(afterSet.contains(THIRD));
		assertEquals(0, beforeSet.size());
	}

}
