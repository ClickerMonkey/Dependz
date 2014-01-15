package org.magnos.dependency;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.magnos.dependency.DependencyGraph;
import org.magnos.dependency.DependencyNode;


public class TestDependencyMap
{
	
	@Test
	public void testValid()
	{
	    DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
	    map.add( "value0", 0 );
	    map.add( "value1", 1 );
	    map.add( "value2", 2 );
	    map.add( "value3", 3 );
	    map.add( "value4", 4 );
	    map.addDependency( "value1", "value0" );
	    map.addDependency( "value0", "value2" );
	    map.addDependency( "value3", "value1" );
	    map.addDependency( "value4", "value1" );
		
		DependencyGraph<Integer> analyzer = new DependencyGraph<Integer>();
		analyzer.analyze( map.getDependencyNodes() );
		
		assertTrue( analyzer.isValid() );
		assertEquals( 3, analyzer.getOutput().get( 0 ).intValue() );
		assertEquals( 4, analyzer.getOutput().get( 1 ).intValue() );
		assertEquals( 1, analyzer.getOutput().get( 2 ).intValue() );
		assertEquals( 0, analyzer.getOutput().get( 3 ).intValue() );
		assertEquals( 2, analyzer.getOutput().get( 4 ).intValue() );
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
		
		DependencyGraph<String> analyzer = new DependencyGraph<String>();
		analyzer.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertFalse( analyzer.isValid() );
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
		
		DependencyGraph<String> analyzer = new DependencyGraph<String>();
		analyzer.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertFalse( analyzer.isValid() );
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
		
		DependencyGraph<String> analyzer = new DependencyGraph<String>();
		analyzer.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertFalse( analyzer.isValid() );
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
		
		DependencyGraph<String> analyzer = new DependencyGraph<String>();
		analyzer.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertTrue( analyzer.isValid() );
		
		List<List<DependencyNode<String>>> groups = analyzer.getDepthGroupNodes();
		
		assertEquals( Arrays.asList( value3, value4 ), groups.get( 0 ) );
		assertEquals( Arrays.asList( value1 ), groups.get( 1 ) );
		assertEquals( Arrays.asList( value0 ), groups.get( 2 ) );
		assertEquals( Arrays.asList( value2 ), groups.get( 3 ) );
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
		
		DependencyGraph<String> analyzer = new DependencyGraph<String>();
		analyzer.analyze( Arrays.asList( value0, value1, value2, value3, value4 ) );
		
		assertTrue( analyzer.isValid() );
		
		List<List<String>> groups = analyzer.getDepthGroups();
		
		assertEquals( Arrays.asList( "value3", "value4" ), groups.get( 0 ) );
		assertEquals( Arrays.asList( "value1" ), groups.get( 1 ) );
		assertEquals( Arrays.asList( "value0" ), groups.get( 2 ) );
		assertEquals( Arrays.asList( "value2" ), groups.get( 3 ) );
	}
	
}
