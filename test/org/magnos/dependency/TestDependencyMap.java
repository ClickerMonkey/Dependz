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
	    map.addDependency( "value0", "value1" );
	    map.addDependency( "value2", "value0" );
	    map.addDependency( "value1", "value3" );
	    map.addDependency( "value1", "value4" );
		
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
	    DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
        map.add( "value0", 0 );
        map.add( "value1", 1 );
        map.add( "value2", 2 );
        map.add( "value3", 3 );
        map.add( "value4", 4 );
        map.addDependency( "value0", "value1" );
        map.addDependency( "value1", "value2" );
        map.addDependency( "value2", "value3" );
        map.addDependency( "value3", "value4" );
        map.addDependency( "value4", "value0" );
	    
		DependencyGraph<Integer> analyzer = new DependencyGraph<Integer>();
		analyzer.analyze( map.getDependencyNodes() );
		
		assertFalse( analyzer.isValid() );
	}
	
	@Test
	public void testInterdependent()
	{
	    DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
        map.add( "value0", 0 );
        map.add( "value1", 1 );
        map.add( "value2", 2 );
        map.add( "value3", 3 );
        map.add( "value4", 4 );
        map.addDependency( "value1", "value0" );
        map.addDependency( "value0", "value1" );
	    
		DependencyGraph<Integer> analyzer = new DependencyGraph<Integer>();
		analyzer.analyze( map.getDependencyNodes() );
		
		assertFalse( analyzer.isValid() );
	}
	
	@Test
	public void testPartialCircularDependency()
	{
	    DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
        map.add( "value0", 0 );
        map.add( "value1", 1 );
        map.add( "value2", 2 );
        map.add( "value3", 3 );
        map.add( "value4", 4 );
        map.addDependency( "value0", "value1" );
        map.addDependency( "value1", "value2" );
        map.addDependency( "value2", "value0" );
        map.addDependency( "value3", "value1" );
		
		DependencyGraph<Integer> analyzer = new DependencyGraph<Integer>();
		analyzer.analyze( map.getDependencyNodes() );
		
		assertFalse( analyzer.isValid() );
	}
	
	@Test
	public void testGrouping()
	{
	    DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
        map.add( "value0", 0 );
        map.add( "value1", 1 );
        map.add( "value2", 2 );
        map.add( "value3", 3 );
        map.add( "value4", 4 );
        map.addDependency( "value0", "value1" );
        map.addDependency( "value2", "value0" );
        map.addDependency( "value1", "value3" );
        map.addDependency( "value1", "value4" );
	    
		DependencyGraph<Integer> analyzer = new DependencyGraph<Integer>();
		analyzer.analyze( map.getDependencyNodes() );
		
		assertTrue( analyzer.isValid() );
		
		List<List<Integer>> groups = analyzer.getDepthGroups();
		
		assertEquals( Arrays.asList( 3, 4 ), groups.get( 0 ) );
		assertEquals( Arrays.asList( 1 ), groups.get( 1 ) );
		assertEquals( Arrays.asList( 0 ), groups.get( 2 ) );
		assertEquals( Arrays.asList( 2 ), groups.get( 3 ) );
	}
	
}
