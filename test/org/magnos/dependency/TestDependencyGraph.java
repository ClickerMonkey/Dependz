/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 *              Open Software License (OSL 3.0)
 */

package org.magnos.dependency;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.magnos.dependency.DependencyAnalyzer;
import org.magnos.dependency.DependencyNode;


public class TestDependencyGraph
{
	
	@Test
	public void testValid()
	{
		DependencyNode<String> value0 = new DependencyNode<String>( "value0" );
		DependencyNode<String> value1 = new DependencyNode<String>( "value1" );
		DependencyNode<String> value2 = new DependencyNode<String>( "value2" );
		DependencyNode<String> value3 = new DependencyNode<String>( "value3" );
		DependencyNode<String> value4 = new DependencyNode<String>( "value4" );
		
		value0.addDependency( value1 );
		value2.addDependency( value0 );
		value1.addDependency( value3 );
		value1.addDependency( value4 );
		
		DependencyAnalyzer<String> graph = new DependencyAnalyzer<String>();
		graph.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertTrue( graph.isValid() );
		assertSame( value3, graph.getOrderedNodes()[ 0 ] );
		assertSame( value4, graph.getOrderedNodes()[ 1 ] );
		assertSame( value1, graph.getOrderedNodes()[ 2 ] );
		assertSame( value0, graph.getOrderedNodes()[ 3 ] );
		assertSame( value2, graph.getOrderedNodes()[ 4 ] );
	}
	
	@Test
	public void testWholyCircular()
	{
		DependencyNode<String> value0 = new DependencyNode<String>( "value0" );
		DependencyNode<String> value1 = new DependencyNode<String>( "value1" );
		DependencyNode<String> value2 = new DependencyNode<String>( "value2" );
		DependencyNode<String> value3 = new DependencyNode<String>( "value3" );
		DependencyNode<String> value4 = new DependencyNode<String>( "value4" );
		
		value0.addDependency( value1 );
		value1.addDependency( value2 );
		value2.addDependency( value3 );
		value3.addDependency( value4 );
		value4.addDependency( value0 );
		
		DependencyAnalyzer<String> graph = new DependencyAnalyzer<String>();
		graph.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertFalse( graph.isValid() );
	}
	
	@Test
	public void testInterdependent()
	{
		DependencyNode<String> value0 = new DependencyNode<String>( "value0" );
		DependencyNode<String> value1 = new DependencyNode<String>( "value1" );
		DependencyNode<String> value2 = new DependencyNode<String>( "value2" );
		DependencyNode<String> value3 = new DependencyNode<String>( "value3" );
		DependencyNode<String> value4 = new DependencyNode<String>( "value4" );
		
		value0.addDependency( value1 );
		value1.addDependency( value0 );
		
		DependencyAnalyzer<String> graph = new DependencyAnalyzer<String>();
		graph.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertFalse( graph.isValid() );
	}
	
	@Test
	public void testPartialCircularDependency()
	{
		DependencyNode<String> value0 = new DependencyNode<String>( "value0" );
		DependencyNode<String> value1 = new DependencyNode<String>( "value1" );
		DependencyNode<String> value2 = new DependencyNode<String>( "value2" );
		DependencyNode<String> value3 = new DependencyNode<String>( "value3" );
		DependencyNode<String> value4 = new DependencyNode<String>( "value4" );
		
		value0.addDependency( value1 );
		value1.addDependency( value2 );
		value2.addDependency( value0 );
		value3.addDependency( value1 );
		
		DependencyAnalyzer<String> graph = new DependencyAnalyzer<String>();
		graph.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertFalse( graph.isValid() );
	}
	
	@Test
	public void testGroupingNodes()
	{
		DependencyNode<String> value0 = new DependencyNode<String>( "value0" );
		DependencyNode<String> value1 = new DependencyNode<String>( "value1" );
		DependencyNode<String> value2 = new DependencyNode<String>( "value2" );
		DependencyNode<String> value3 = new DependencyNode<String>( "value3" );
		DependencyNode<String> value4 = new DependencyNode<String>( "value4" );
		
		value0.addDependency( value1 );
		value2.addDependency( value0 );
		value1.addDependency( value3 );
		value1.addDependency( value4 );
		
		DependencyAnalyzer<String> graph = new DependencyAnalyzer<String>();
		graph.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertTrue( graph.isValid() );
		
		List<DependencyNode<String>>[] groups = graph.getLevelNodes();
		
		assertEquals( Arrays.asList( value3, value4 ), groups[ 0 ] );
		assertEquals( Arrays.asList( value1 ), groups[ 1 ] );
		assertEquals( Arrays.asList( value0 ), groups[ 2 ] );
		assertEquals( Arrays.asList( value2 ), groups[ 3 ] );
	}
	
	@Test
	public void testGrouping()
	{
		DependencyNode<String> value0 = new DependencyNode<String>( "value0" );
		DependencyNode<String> value1 = new DependencyNode<String>( "value1" );
		DependencyNode<String> value2 = new DependencyNode<String>( "value2" );
		DependencyNode<String> value3 = new DependencyNode<String>( "value3" );
		DependencyNode<String> value4 = new DependencyNode<String>( "value4" );
		
		value0.addDependency( value1 );
		value2.addDependency( value0 );
		value1.addDependency( value3 );
		value1.addDependency( value4 );
		
		DependencyAnalyzer<String> graph = new DependencyAnalyzer<String>();
		graph.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertTrue( graph.isValid() );
		
		List<String>[] groups = graph.getLevels();
		
		assertEquals( Arrays.asList( "value3", "value4" ), groups[ 0 ] );
		assertEquals( Arrays.asList( "value1" ), groups[ 1 ] );
		assertEquals( Arrays.asList( "value0" ), groups[ 2 ] );
		assertEquals( Arrays.asList( "value2" ), groups[ 3 ] );
	}
	
}
