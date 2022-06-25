/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dayflower.mock.NodeFilterMock;
import org.dayflower.mock.NodeMockA;
import org.dayflower.mock.NodeMockB;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class NodeFilterUnitTests {
	public NodeFilterUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAnd() {
		final NodeFilter nodeFilter = NodeFilter.and(NodeFilter.regex("A"), NodeFilter.regex("A"));
		
		final Node nodeA = new NodeMockA("A");
		final Node nodeB = new NodeMockB("B");
		
		assertTrue(nodeFilter.isAccepted(nodeA));
		
		assertFalse(nodeFilter.isAccepted(nodeB));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.and(null, null));
		assertThrows(NullPointerException.class, () -> NodeFilter.and((NodeFilter[])(null)));
		assertThrows(NullPointerException.class, () -> nodeFilter.isAccepted(null));
	}
	
	@Test
	public void testAny() {
		final NodeFilter nodeFilter = NodeFilter.any();
		
		final Node nodeA = new NodeMockA("A");
		final Node nodeB = new NodeMockB("B");
		
		assertTrue(nodeFilter.isAccepted(nodeA));
		assertTrue(nodeFilter.isAccepted(nodeB));
		
		assertThrows(NullPointerException.class, () -> nodeFilter.isAccepted(null));
	}
	
	@Test
	public void testFilterAll() {
		final List<NodeMockA> nodeMockAs = NodeFilter.filterAll(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("B"), new NodeMockB("C"), new NodeMockB("C"))), NodeMockA.class);
		
		assertEquals(5, nodeMockAs.size());
		assertEquals(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("B"), new NodeMockB("C"), new NodeMockB("C"))), nodeMockAs.get(0));
		assertEquals(new NodeMockA("A"), nodeMockAs.get(1));
		assertEquals(new NodeMockA("A"), nodeMockAs.get(2));
		assertEquals(new NodeMockA("B"), nodeMockAs.get(3));
		assertEquals(new NodeMockA("B"), nodeMockAs.get(4));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.filterAll(new NodeMockA(""), null));
		assertThrows(NullPointerException.class, () -> NodeFilter.filterAll(null, NodeMockA.class));
	}
	
	@Test
	public void testFilterAllDistinct() {
		final List<NodeMockA> nodeMockAs = NodeFilter.filterAllDistinct(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("B"), new NodeMockB("C"), new NodeMockB("C"))), NodeMockA.class);
		
		assertEquals(3, nodeMockAs.size());
		assertEquals(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("B"), new NodeMockB("C"), new NodeMockB("C"))), nodeMockAs.get(0));
		assertEquals(new NodeMockA("A"), nodeMockAs.get(1));
		assertEquals(new NodeMockA("B"), nodeMockAs.get(2));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.filterAllDistinct(new NodeMockA(""), null));
		assertThrows(NullPointerException.class, () -> NodeFilter.filterAllDistinct(null, NodeMockA.class));
	}
	
	@Test
	public void testFilterNode() {
		final List<Node> nodes = NodeFilter.filter(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("B"))));
		
		assertEquals(3, nodes.size());
		assertEquals(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("B"))), nodes.get(0));
		assertEquals(new NodeMockA("A"), nodes.get(1));
		assertEquals(new NodeMockA("B"), nodes.get(2));
		
		assertThrows(NodeTraversalException.class, () -> NodeFilter.filter(new NodeMockA(""), new NodeFilterMock(true, true)));
		assertThrows(NullPointerException.class, () -> NodeFilter.filter(null));
	}
	
	@Test
	public void testFilterNodeNodeFilter() {
		final List<Node> nodes = NodeFilter.filter(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("B"))), NodeFilter.any());
		
		assertEquals(3, nodes.size());
		assertEquals(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("B"))), nodes.get(0));
		assertEquals(new NodeMockA("A"), nodes.get(1));
		assertEquals(new NodeMockA("B"), nodes.get(2));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.filter(new NodeMockA(""), null));
		assertThrows(NullPointerException.class, () -> NodeFilter.filter(null, NodeFilter.any()));
	}
	
	@Test
	public void testFilterNodeNodeFilterClass() {
		final List<NodeMockA> nodeMockAs = NodeFilter.filter(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("B"), new NodeMockB("C"))), NodeFilter.any(), NodeMockA.class);
		
		assertEquals(3, nodeMockAs.size());
		assertEquals(new NodeMockA("Root", Arrays.asList(new NodeMockA("A"), new NodeMockA("B"), new NodeMockB("C"))), nodeMockAs.get(0));
		assertEquals(new NodeMockA("A"), nodeMockAs.get(1));
		assertEquals(new NodeMockA("B"), nodeMockAs.get(2));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.filter(new NodeMockA(""), NodeFilter.any(), null));
		assertThrows(NullPointerException.class, () -> NodeFilter.filter(new NodeMockA(""), null, NodeMockA.class));
		assertThrows(NullPointerException.class, () -> NodeFilter.filter(null, NodeFilter.any(), NodeMockA.class));
	}
	
	@Test
	public void testMapDistinctToOffsetsList() {
		final List<Node> distinctNodes = Arrays.asList(new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("C"));
		
		final Map<Node, Integer> distinctToOffsets = NodeFilter.mapDistinctToOffsets(distinctNodes);
		
		assertEquals(Integer.valueOf(0), distinctToOffsets.get(new NodeMockA("A")));
		assertEquals(Integer.valueOf(1), distinctToOffsets.get(new NodeMockA("B")));
		assertEquals(Integer.valueOf(2), distinctToOffsets.get(new NodeMockA("C")));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(null));
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(Arrays.asList(null, null)));
	}
	
	@Test
	public void testMapDistinctToOffsetsListInt() {
		final List<Node> distinctNodes = Arrays.asList(new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("C"));
		
		final Map<Node, Integer> distinctToOffsets = NodeFilter.mapDistinctToOffsets(distinctNodes, 2);
		
		assertEquals(Integer.valueOf(0), distinctToOffsets.get(new NodeMockA("A")));
		assertEquals(Integer.valueOf(2), distinctToOffsets.get(new NodeMockA("B")));
		assertEquals(Integer.valueOf(4), distinctToOffsets.get(new NodeMockA("C")));
		
		assertThrows(IllegalArgumentException.class, () -> NodeFilter.mapDistinctToOffsets(distinctNodes, 0));
		assertThrows(IllegalArgumentException.class, () -> NodeFilter.mapDistinctToOffsets(distinctNodes, Integer.MAX_VALUE));
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(null, 2));
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(Arrays.asList(null, null), 2));
	}
	
	@Test
	public void testMapDistinctToOffsetsListToIntFunction() {
		final List<Node> distinctNodes = Arrays.asList(new NodeMockA("A"), new NodeMockA("B"), new NodeMockA("C"));
		
		final Map<Node, Integer> distinctToOffsets = NodeFilter.mapDistinctToOffsets(distinctNodes, node -> 2);
		
		assertEquals(Integer.valueOf(0), distinctToOffsets.get(new NodeMockA("A")));
		assertEquals(Integer.valueOf(2), distinctToOffsets.get(new NodeMockA("B")));
		assertEquals(Integer.valueOf(4), distinctToOffsets.get(new NodeMockA("C")));
		
		assertThrows(IllegalArgumentException.class, () -> NodeFilter.mapDistinctToOffsets(distinctNodes, node -> -1));
		assertThrows(IllegalArgumentException.class, () -> NodeFilter.mapDistinctToOffsets(distinctNodes, node -> Integer.MAX_VALUE));
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(distinctNodes, null));
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(null, node -> 2));
		assertThrows(NullPointerException.class, () -> NodeFilter.mapDistinctToOffsets(Arrays.asList(null, null), node -> 2));
	}
	
	@Test
	public void testOr() {
		final NodeFilter nodeFilter = NodeFilter.or(NodeFilter.regex("A"), NodeFilter.regex("B"));
		
		final Node nodeA = new NodeMockA("A");
		final Node nodeB = new NodeMockB("B");
		
		assertTrue(nodeFilter.isAccepted(nodeA));
		assertTrue(nodeFilter.isAccepted(nodeB));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.or(null, null));
		assertThrows(NullPointerException.class, () -> NodeFilter.or((NodeFilter[])(null)));
		assertThrows(NullPointerException.class, () -> nodeFilter.isAccepted(null));
	}
	
	@Test
	public void testRegex() {
		final NodeFilter nodeFilter = NodeFilter.regex("A");
		
		final Node nodeA = new NodeMockA("A");
		final Node nodeB = new NodeMockB("B");
		
		assertTrue(nodeFilter.isAccepted(nodeA));
		
		assertFalse(nodeFilter.isAccepted(nodeB));
		
		assertThrows(NullPointerException.class, () -> NodeFilter.regex(null));
		assertThrows(NullPointerException.class, () -> nodeFilter.isAccepted(null));
	}
}