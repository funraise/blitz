# Blitz


## Introduction

Blitz is a mapper library that converts a POJO to another POJO using Java 8 lambdas to implement the mapping logic. It depends on Java 8 and uses [Orika](https://github.com/orika-mapper/orika)
to support default mapping. It is a fork of the NFL's Audible project but removes Spring as a dependency and uses Reflections and Guice to support
discovery of annotated mappings at runtime.

### Why Another Mapper?

There are other POJO mapping libraries out there such as [Dozer](http://dozer.sourceforge.net/), [Orika](https://github.com/orika-mapper/orika),
and [Apache BeanUtils](http://commons.apache.org/proper/commons-beanutils/). One of the assumptions
that these libraries assume is that the source domain contains all necessary information to convert to the
target domain. If you need to call external services such as retrieving additional information from a database
during the conversion process, you would instead have to aggregate it into the source domain before you can use the mapper library.
Blitz provides the ability to have complete control including calling additional service methods during the conversion.
You can also define multiple case-specific mappings for a single type. You can also define mappings for different source domains to one target domain.
 Blitz combines the flexibility of manual conversions with some of the convenience of other mapping libraries.

|       | Blitz   | Dozer   | BeanUtils   | Orika   |
|-------|:---------:|:-------:|:-----------:|:-------:|
|Doesn't Require Java 8 | 		| 	✓	| 	✓	|  ✓ |
|Doesn't Require Spring |	✓	|	✓	|	✓	|  ✓ |
|Type Safe Mapping |✓	|		|	✓	|   ✓ |
|Built-in Concurrent Processing|	✓	|		|		|   |
|Call Other Service Within Mapping|	✓	|		|		|   ✓ |
|Automatic Mapping of Embedded Objects|	✓	|	✓	|	✓	|   ✓ |
|Independently Define Custom Mapping of Nested Objects| 	✓	| 		| 		|   |
|Multiple Mapping Versions|	✓	|		|		|    |
|Multiple Source Type Support|✓	|		|		|   ✓ |
|Post Processor|	✓	|		|		|   |

### When to use Blitz
Blitz is best used when there is a need for complex hierarchy of custom conversions and you don't want to have Spring as a dependency of your
project. Other libraries also support custom mappers and converters, but it is not intuitive to reuse them inside other converters and mappers

### Example
```java
public FromCourse {
    String title;
    List<FromStudent> students;
}
```
```java
public FromStudent {
    String firstName;
    String lastName;
}
```
```java
public ToCourse {
    String name;
    List<ToStudent> registrants;
}
```
```java
public ToStudent {
    String fullName;
}
```
In the above example, it's obvious that we want to convert `FromCourse` to `ToCourse` and from `FromStudent` to `ToStudent`.
Blitz allows to configure the two custom mappers independently while keeping any logic out of converting child in the parent mapper.
Blitz is also capable of handling nested conversions automatically.
What this means is that if you have an Object for which you are defining a converter (e.g. Course) 
and it contains an Object for which you have already defined a converter (e.g. Student), 
there's no need to explicitly call the second converter (the one for Student) inside your Course converter, 
Blitz will this is for you automatically as seen [here in this example for courses and address](#to-student-mapping).

## Configuration

In order to configure this service first provide your DomainMapperConfiguration in a Guice module as shown. All classes that are annotated with `@MappingTo`
should be specified in the search path prefix of the DomainmapperConfiguration object.

### <a name="config-example"></a>[Example] ApplicationConfig.java
```java
package io.funraise.dm.blitz;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import io.funraise.dm.blitz.service.DomainMapperConfig;


public class ApplicationTestConfig implements Module {

    @Override
    public void configure(Binder binder) { }

    @Provides
    DomainMapperConfig getDomainMapperConfig() {
        return new DomainMapperConfig("io.funraise.dm.blitz.domain");
    }
}
```


## Annotations
There are three annotations that are used to denote a mapping of one class to another.

### @MappingTo

The first is the `@MappingTo` annotation takes one parameter designating the class that it maps to. It is used on the declaration of the mapping class. This class should contain all of the methods mapping from objects of other classes to an object of the designated class.

### @Mapping

The second annotation is the `@Mapping` annotation. It is used on the individual methods within the mapping class that differ based on the parameters supplied to the annotation. The parameters this annotation takes are `type`, `name`, `originalClass`, and `parallelCollections`.

* `type`: The type of the mapping, it is a value of the enum `MappingType` with the following possible values:
	* `NORMAL`
	* `EMBEDDED`
	
	The default value is `MappingType.NORMAL`. When invoking a mapping using the `DomainMapper` object it will by default look for a mapping with type `MappingType.NORMAL`. When the mapper is mapping embedded objects, that is objects within an object, it will default to looking for a mapping with type `MappingType.EMBEDDED`. If an `EMBEDDED` mapping is not found it will then use the `NORMAL` mapping. If neither mapping is present then it will throw an exeption
	
* `name`: The name of the mapping, it is represented as a `String`. It does not need to be unique but there should be a unique pair of `originalClass` and `name`. The default value is the empty string `""`.
* `originalClass`: The class of the object that is the source of the mapping. This is a required value.

The method with the `@Mapping` annotation should have the following signature: `public Map<String, java.util.Function<toClass, ?>> funcName()`
One entry in the Map corresponds to a mapping for one field in the destination object. The keys in the map are the names of the setters of the desintation object. The values of the map can be defined with lambda expressions that takes in the source object as the parameter.

```java
@MappingTo(ToStudent.class)
public class ToStudentMapping {

    @Mapping(originalClass = FromStudent.class)
    public Map<String, Function<FromStudent, ?>> getMapping() {
        Map<String, Function<FromStudent, ?>> map = new HashMap<>();
        map.put("firstName", (FromStudent s) -> s.getName().split(" ")[0]);
        map.put("lastName", (FromStudent s) -> s.getName().split(" ")[1]);

        return map;
    }
    
    ...
}
```

### @PostProcessor

The third annotation is the `@PostProcessor` annotation. This annotation is used when you want to execute some function following the mapping of one object to another. It is used inside of the class annotated with `@MappingTo`. 

This annotation takes two parameters, `originalClass` and `mappingName`. The `originalClass` parameter is required and should be the class of the object that is the source of the mapping. The `mappingName` parameter is a `String` and is the name of the mapping. It has a default value of the empty string `""`. 

A `@PostProcessor` method will be invoked after any mapping which has the same `originalClass` and `mappingName` as specified in this annotation. Methods annotated with `@PostProcessor` should have a return type of `void` and take two parameters. The first should have type `toClass` which matches the parameter in `@MappingTo`. The second should have type `fromClass` which matches the `originalClass` parameter in this `@PostProcessor` annotation. 

Modify your `toClass` object in this method.

```java
@MappingTo(ToStudent.class)
public class ToStudentMapping {

    @Mapping(originalClass = FromStudent.class)
    public Map<String, Function<FromStudent, ?>> getMapping() {
        ...
    }
    
    @PostProcessor(originalClass = FromStudent.class)
	public void postProcess(ToStudent toStudent, FromStudent fromStudent) {
		toStudent.setSomeField(doSomethingThatRequiresToStudent(toStudent.getFirstName()));
	}
    
    ...
}
```

## DomainMapper

### Guice
You should use injection to create and a configure your DomainMapper with a DomainMapperConfig object. An example usage  (as seen in the [example](#config-example)) is as follows:

```java
public class ApplicationTestConfig implements Module {

    @Override
    public void configure(Binder binder) { }

    @Provides
    DomainMapperConfig getDomainMapperConfig() {
        return new DomainMapperConfig("io.funraise.dm.blitz.domain");
    }
}

```

The three methods in the builder class that you need to be concerned with are:

* `setAutoMapUsingOrika(boolean autoMapUsingOrika)`: a value of `true` indicates that the mapper will automatically map compatible fields with the same name without having the need for a defined mapping (Default value: `true`)
* `setDefaultEmbeddedMapping(MappingType defaultEmbeddedMapping)`: sets the default mapping type for embedded objects, that is objects within other objects (Default value: `MappingType.EMBEDDED`)
* `setParallelProcessEmbeddedList(boolean parallelProcessEmbeddedList)`: a value of `true` indicates that when mapping embedded lists, it will do so concurrently instead of sequentially (Default value: `false`)


### DomainMapper.map Function
In order to perform the mapping from one object to another an instance of the `DomainMapper` class is used. The primary method used in mapping an object is `DomainMapper.map` which takes between two and four arguments. The method signatures are as follows:

* `public <From, To> To map(Class<To> toClass, From from)`
* `public <From, To> To map(Class<To> toClass, From from, MappingType mappingType)`
* `public <From, To> To map(Class<To> toClass, From from, String mappingName)`
* `public <From, To> To map(Class<To> toClass, From from, String mappingName, MappingType mappingType)`

If the `mappingType` parameter is not present it will default to a value of `MappingType.NORMAL`, if the `mappingName` parameter is not present it will default to the empty string `""`.


## Simple Mapping Example
### <a name="to-student-mapping"></a>ToStudentMapping.java

```java
@MappingTo(ToStudent.class)
public class ToStudentMapping {

   @Inject
   private SSNService ssnService;

   @Mapping(originalClass = FromStudent.class)
    public Map<String, Function<FromStudent, ?>> getMapping() {
        Map<String, Function<FromStudent, ?>> map = new HashMap<>();
        map.put("firstName", (FromStudent s) -> s.getName().split(" ")[0]);
        map.put("lastName", (FromStudent s) -> s.getName().split(" ")[1]);
        //Ability to call any other Spring managed service bean
        map.put("ssn", (FromStudent s) -> ssnService.findByName(s.getName()));
        
        //Note that you are simply passing List<FromCourse>.  Mapping will automatically convert them to List<ToCourse>
        map.put("courses", (FromStudent s) -> s.getCourses()); 

        //Note that you are simply passing FromAddress. Mapping will automatically convert it to ToAddress type.
        map.put("address", (FromStudent s) -> s.getAddress();
        return map;
    }
    
    @PostProcessor(originalClass = FromStudent.class)
	public void postProcess(ToStudent toStudent, FromStudent fromStudent) {
		doSomething();
	}
    
    ...
}
```

### ToCourseMapping.java
```java
@MappingTo(ToCourse.class)
public class ToCourseMapping {

	@Mapping(originalClass = FromCourse.class)
	public Map<String, Function<FromCourse, ?>> getMapping() {
		Map<String, Function<FromStudent, ?>> map = new HashMap<>();
		map.put("name", (FromCourse fc) -> parseCourseTitle(fc.getName()));
		map.put("courseNum", (FromCourse fc) -> parseCourseNumber(fc.getname()));
		map.put("gpa", (FromCourse fc) -> {
			if(fc.getLetterGrade().equals("A") return 4;
			else if(fc.getLetterGrade().equals("B") return 3;
			else if(fc.getLetterGrade().equals("C") return 2;
			else if(fc.getLetterGrade().equals("D") return 1;
			else if(fc.getLetterGrade().equals("F") return 0;
		}
	}
	
	public String parseCourseTitle(String name) {
		...
	}
	
	public String parseCourseNumber(String name) {
		...
	}
}
```

### ToAddressMapping.java

```java
@MappingTo(ToAddress.class)
public class ToAddressMapping {

	@Inject
	private GeoService geoService;

	@Mapping(originalClass = FromAddress.class)
	public Map<String, Function<FromAddress, ?>> getMapping() {
		Map<String, Function<FromAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAddress fa) -> fa.getStreet());
		map.put("zip", (FromAddress fa) -> fa.getZip());
		map.put("state", (FromAddress fa) -> geoService.getStateFromZip(fa.getZip()));
		
		return map;
	}
}

```

### FromStudent.java

```java
public class FromStudent {

    private String name;

    List<FromCourse> courses;
    
    FromAddress address;
    
    ...
}
```

#### FromCourse.java

```java
public class FromCourse {

	private String name;
	
	private String letterGrade
	
	...
}
```

### FromAddress.java

```java
public class FromAddress {

	private String street;
	
	private String zip;
	
	...
}
```

### ToStudent.java

```java
public class ToStudent {

    private String firstName;

    private String lastName;
    
    private String ssn;
    
    private List<ToCourse> courses;
    
    private ToAddress address;
    
    ...
}
```

### ToCourse.java

```java
public class ToCourse {

	private String name;
	
	private String courseNum;
	
	private Integer gpa;
	
	...
}
```

### ToAddress.java

```java
public class ToAddress {
	
	private String street;
	
	private String zip;
	
	private String state;
	
	...
}
```

### Driver.java
```java
public class Driver {

	@Inject
	DomainMapper dm;
	
	public static void main() {
		FromStudent fromStudent = new FromStudent();
		fromStudent.setName("Tom Brady");
		
		Student toStudent = dm.map(ToStudent.class, fromStudent);
		//toStudent.getFirstName() == "Tom"
		//toStudent.getLastName() == "Brady"
	}
}
```
# Advanced

## Embedded Mapping

### ToAddressMapping.java

```java
@MappingTo(ToAddress.class)
public class ToAddressMapping {

	@Inject
	private GeoService geoService;

	@Mapping(originalClass = FromAddress.class)
	public Map<String, Function<FromAddress, ?>> getMapping() {
		Map<String, Function<FromAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAddress fa) -> fa.getStreet());
		map.put("zip", (FromAddress fa) -> fa.getZip());
		map.put("state", (FromAddress fa) -> geoService.getStateFromZip(fa.getZip()));
		
		return map;
	}

	@Mapping(mappingType = MappingType.EMBEDDED, originalClass = FromAddress.class)
	public Map<String, Function<FromAddress, ?>> getEmbeddedMapping() {
		Map<String, Function<FromAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAddress fa) -> fa.getStreet());
		
		return map;
	}

}

```

```
public class Main {

	@Inject
	DomainMapper dm;
	
	public static void main() {
		//MappingType.EMBEDDED == dm.getDefaultEmbeddedMapping()

		FromStudent fromStudent = new FromStudent();
		fromStudent.setName("Tom Brady");
		fromStudent.setAge(38);
		fromStudent.setGpa(4.0);

		FromAddress fromAddress = new FromAddress();
		fromAddress.setAddress("10950 Washington Blvd");
		fromAddress.setZip("80232");
		
		Student toStudent = dm.map(ToStudent.class, fromStudent);
		//toStudent.getFirstName() == "Brady"
		//toStudent.getLastName() == "Tom"

		//toStudent.getAddress().getZip() == null

		ToAddress toAddress = dm.map(ToAddress.class, fromAddress);
		//toAddress.getZip() == 80232
	}
}
```


## Named Mapping Example
### ToStudentMapping.java
```java

@MappingTo(ToStudent.class)
public class ToStudentMapping {

    @Mapping(originalClass = FromStudent.class, name = "reverse")
    public Map<String, Function<FromStudent, ?>> reverseMapping() {
        Map<String, Function<FromStudent, ?>> map = new HashMap<>();
        map.put("lastName", (FromStudent s) -> s.getName().split(" ")[0]);
        map.put("firstName", (FromStudent s) ->  s.getName().split(" ")[1]);

        return map;
    }
    
    @PostProcessor(originalClass = FromStudent.class, mappingName = "reverse")
	public void postProcess(ToStudent toStudent, FromStudent fromStudent) {
		doSomething();
	}
    
    ...
}
```

### Main.java
```java
public class Main {

	@Inject
	DomainMapper dm;
	
	public static void main() {
		FromStudent fromStudent = new FromStudent();
		fromStudent.setName("Tom Brady");
		fromStudent.setAge(38);
		fromStudent.setGpa(4.0);
		
		Student toStudent = dm.map(ToStudent.class, fromStudent, "reverseName");
		//toStudent.getFirstName() == "Brady"
		//toStudent.getLastName() == "Tom"
	}
}
```

## Multiple Source Types

```
public class FromAnotherAddress {

	private String street;
	
	private String zipPlusFour; //of the form ZIP+4 
	
	...
}
```

```java

@MappingTo(ToAddress.class)
public class ToAddressMapping {

	@Inject
	private GeoService geoService;

	@Mapping(originalClass = FromAddress.class)
	public Map<String, Function<FromAddress, ?>> getMapping() {
		Map<String, Function<FromAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAddress fa) -> fa.getStreet());
		map.put("zip", (FromAddress fa) -> fa.getZip());
		map.put("state", (FromAddress fa) -> geoService.getStateFromZip(fa.getZip()));
		
		return map;
	}

	@Mapping(originalClass = FromAnotherAddress.class)
	public Map<String, Function<FromAnotherAddress, ?>> getAnotherMapping() {
		Map<String, Function<FromAnotherAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAnotherAddress fa) -> fa.getStreet());
		map.put("zip", (FromAnotherAddress fa) -> fa.getZipPlusFour().substring(0, fa.getZipPlusFour().indexOf('-')));
		map.put("state", (FromAnotherAddress fa) -> geoService.getStateFromZip(fa.getZip()));
		
		return map;
	}

}

```

### Main.java
```
public class Main {

	@Inject
	DomainMapper dm;
	
	public static void main() {

		FromAddress fromAddress = new FromAddress();
		fromAddress.setAddress("10950 Washington Blvd");
		fromAddress.setZip("80232");
		
		FromAnotherAddress fromAnotherAddress = new FromAnotherAddress();
		fromAnotherAddress.setAddress("10950 Washington Blvd");
		fromAnotherAddress.setZipPlusFour("80232-0000");

		ToAddress toAddress = dm.map(ToAddress.class, fromAddress);
		//toAddress.getZip() == 80232

		ToAddress toAddress = dm.map(ToAddress.class, fromAnotherAddress);
		//toAddress.getZip() == 80232
	}
}
```

## Overriding MappingType
```java

@MappingTo(ToStudent.class)
public class ToStudentMapping {

   @Inject
   private SSNService ssnService;

   @Mapping(originalClass = FromStudent.class)
    public Map<String, Function<FromStudent, ?>> getMapping() {
        Map<String, Function<FromStudent, ?>> map = new HashMap<>();
        ...

        //Override address mapping to NORMAL mappingTYpe
        map.put("address", (FromStudent s) -> CustomMappingWrapper.customMapping(s.getAddress()).withMappingType(MappingType.NORMAL);
        return map;
    }
    
    @PostProcessor(originalClass = FromStudent.class)
	public void postProcess(ToStudent toStudent, FromStudent fromStudent) {
		doSomething();
	}
    
    ...
}
```

```java

@MappingTo(ToAddress.class)
public class ToAddressMapping {

	@Inject
	private GeoService geoService;

	@Mapping(originalClass = FromAddress.class)
	public Map<String, Function<FromAddress, ?>> getMapping() {
		Map<String, Function<FromAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAddress fa) -> fa.getStreet());
		map.put("zip", (FromAddress fa) -> fa.getZip());
		map.put("state", (FromAddress fa) -> geoService.getStateFromZip(fa.getZip()));
		
		return map;
	}

	@Mapping(mappingType = MappingType.EMBEDDED, originalClass = FromAddress.class)
	public Map<String, Function<FromAddress, ?>> getEmbeddedMapping() {
		Map<String, Function<FromAddress, ?>> map = new HashMap<>();
		map.put("street", (FromAddress fa) -> fa.getStreet());
		
		return map;
	}

}

```

### Main.java
```
public class Main {

	@Inject
	DomainMapper dm;
	
	public static void main() {
		//MappingType.EMBEDDED == dm.getDefaultEmbeddedMapping()

		FromStudent fromStudent = new FromStudent();
		fromStudent.setName("Tom Brady");
		fromStudent.setAge(38);
		fromStudent.setGpa(4.0);

		FromAddress fromAddress = new FromAddress();
		fromAddress.setAddress("10950 Washington Blvd");
		fromAddress.setZip("80232");
		
		Student toStudent = dm.map(ToStudent.class, fromStudent);
		
		//NORMAL mapping was used
		//toStudent.getAddress().getZip() == 80232

	}
}
```

## Contributing
Something need changing? Missing feature? [Checkout how to contribute](https://funraise.atlassian.net/wiki/spaces/RD/pages/512229473/How+To+Contribute+to+Blitz)

## License

MIT
