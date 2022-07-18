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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.dayflower.mock.NodeMockA;
import org.dayflower.mock.NodeMockB;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class NodeCacheUnitTests {
	public NodeCacheUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddNode() {
		final NodeCache nodeCache = new NodeCache();
		
		assertThrows(NullPointerException.class, () -> nodeCache.add(null));
	}
	
	@Test
	public void testAddNodeNodeFilter() {
		final NodeCache nodeCache = new NodeCache();
		
		nodeCache.add(new NodeMockA("A"), node -> false);
		
		assertThrows(NullPointerException.class, () -> nodeCache.add(new NodeMockA("A"), null));
		assertThrows(NullPointerException.class, () -> nodeCache.add(null, NodeFilter.any()));
	}
	
	@Test
	public void testClear() {
		final Node nodeA = new NodeMockA("1");
		final Node nodeB = new NodeMockA("1");
		final Node nodeC = new NodeMockB("2");
		final Node nodeD = new NodeMockB("2");
		
		final
		NodeCache nodeCache = new NodeCache();
		nodeCache.add(nodeA);
		nodeCache.add(nodeB);
		nodeCache.add(nodeC);
		nodeCache.add(nodeD);
		nodeCache.clear();
		
		final List<NodeMockA> nodeMockAs = nodeCache.getAll(NodeMockA.class);
		
		assertEquals(0, nodeMockAs.size());
	}
	
	@Test
	public void testGetAll() {
		final Node nodeA = new NodeMockA("1");
		final Node nodeB = new NodeMockA("1");
		final Node nodeC = new NodeMockB("2");
		final Node nodeD = new NodeMockB("2");
		
		final
		NodeCache nodeCache = new NodeCache();
		nodeCache.add(nodeA);
		nodeCache.add(nodeB);
		nodeCache.add(nodeC);
		nodeCache.add(nodeD);
		
		final List<Node> nodes = nodeCache.getAll(Node.class);
		final List<NodeMockA> nodeMockAs = nodeCache.getAll(NodeMockA.class);
		
		assertEquals(0, nodes.size());
		assertEquals(2, nodeMockAs.size());
		
		assertEquals(nodeA, nodeMockAs.get(0));
		assertEquals(nodeB, nodeMockAs.get(1));
		
		assertThrows(NullPointerException.class, () -> nodeCache.getAll(null));
	}
	
	@Test
	public void testGetAllDistinct() {
		final Node nodeA = new NodeMockA("1");
		final Node nodeB = new NodeMockA("1");
		final Node nodeC = new NodeMockB("2");
		final Node nodeD = new NodeMockB("2");
		
		final
		NodeCache nodeCache = new NodeCache();
		nodeCache.add(nodeA);
		nodeCache.add(nodeB);
		nodeCache.add(nodeC);
		nodeCache.add(nodeD);
		
		final List<Node> nodes = nodeCache.getAllDistinct(Node.class);
		final List<NodeMockA> nodeMockAs = nodeCache.getAllDistinct(NodeMockA.class);
		
		assertEquals(0, nodes.size());
		assertEquals(1, nodeMockAs.size());
		
		assertEquals(nodeA, nodeMockAs.get(0));
		
		assertThrows(NullPointerException.class, () -> nodeCache.getAllDistinct(null));
	}
}