Dependz
=======

![Stable](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_stable.png)

A dependency analysis utility in Java which uses topological sorting to order values based on their dependencies.

There are two methods to build a DependencyGraph, one with DependencyNodes (which requires all DependencyNode references to be created ahead of time) or by using DependencyMap which can use an arbitrary key to map dependencies between values.

#### Example

```java
DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
map.put( "value0", 0 );
map.put( "value1", 1 );
map.put( "value2", 2 );
map.put( "value3", 3 );
map.put( "value4", 4 );
map.addDependency( "value0", "value1" ); // value0 depends on value1
map.addDependency( "value2", "value0" ); 
map.addDependency( "value1", "value3" );
map.addDependency( "value1", "value4" );
        
DependencyAnalyzer<Integer> analyzer = new DependencyAnalyzer<Integer>();
analyzer.analyze( map.getDependencyNodes() );

assertTrue( analyzer.isValid() );
assertEquals( Arrays.asList( 3, 4, 1, 0, 2 ), graph.getOrdered() );
```

#### Builds
- [dependz-1.0.jar](https://github.com/ClickerMonkey/Dependz/raw/master/build/dependz-1.0.jar)
- [dependz-src-1.0.jar](https://github.com/ClickerMonkey/Dependz/raw/master/build/dependz-1.0-src.jar) - includes source code

#### Projects using Dependz:

- [Rekord](https://github.com/ClickerMonkey/Rekord)
