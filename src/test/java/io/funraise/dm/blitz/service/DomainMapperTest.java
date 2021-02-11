package io.funraise.dm.blitz.service;

import static java.util.Collections.singletonList;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.funraise.dm.blitz.ApplicationTestConfig;
import io.funraise.dm.blitz.CustomConverter;
import io.funraise.dm.blitz.domain.auto.source.FromAuto;
import io.funraise.dm.blitz.domain.auto.source.FromNotMapped;
import io.funraise.dm.blitz.domain.auto.target.ToAuto;
import io.funraise.dm.blitz.domain.complete.source.Animal;
import io.funraise.dm.blitz.domain.complete.source.FromFriend;
import io.funraise.dm.blitz.domain.complete.source.FromJob;
import io.funraise.dm.blitz.domain.complete.source.FromPerson;
import io.funraise.dm.blitz.domain.complete.target.ToFriend;
import io.funraise.dm.blitz.domain.complete.target.ToPerson;
import io.funraise.dm.blitz.domain.converter.source.FromDateTime;
import io.funraise.dm.blitz.domain.converter.target.ToDateTime;
import io.funraise.dm.blitz.domain.cycle.source.FromChild;
import io.funraise.dm.blitz.domain.cycle.source.FromParent;
import io.funraise.dm.blitz.domain.cycle.target.ToParent;
import io.funraise.dm.blitz.domain.cycle_collection.source.ToEmployee;
import io.funraise.dm.blitz.domain.cycle_collection.target.FromEmployee;
import io.funraise.dm.blitz.domain.dummy.MyList;
import io.funraise.dm.blitz.domain.dummy.Numbers;
import io.funraise.dm.blitz.domain.dummy.Student1;
import io.funraise.dm.blitz.domain.dummy.Student2;
import io.funraise.dm.blitz.domain.empty_lists.source.FromArtist;
import io.funraise.dm.blitz.domain.empty_lists.source.FromPainting;
import io.funraise.dm.blitz.domain.empty_lists.target.ToArtist;
import io.funraise.dm.blitz.domain.enums.source.FromQuestion;
import io.funraise.dm.blitz.domain.enums.source.FromQuestionType;
import io.funraise.dm.blitz.domain.enums.target.ToQuestion;
import io.funraise.dm.blitz.domain.enums.target.ToQuestion.ToQuestionType;
import io.funraise.dm.blitz.domain.explicit.source.FromPet;
import io.funraise.dm.blitz.domain.explicit.target.ToPet;
import io.funraise.dm.blitz.domain.fluent_setters.source.FromCar;
import io.funraise.dm.blitz.domain.fluent_setters.source.FromDriver;
import io.funraise.dm.blitz.domain.fluent_setters.target.ToDriver;
import io.funraise.dm.blitz.domain.multi_source.source.FromAddress;
import io.funraise.dm.blitz.domain.multi_source.source.FromAnotherAddress;
import io.funraise.dm.blitz.domain.multi_source.target.ToAddress;
import io.funraise.dm.blitz.domain.nested_mapper.source.FromIllness;
import io.funraise.dm.blitz.domain.nested_mapper.source.FromPatient;
import io.funraise.dm.blitz.domain.nested_mapper.source.FromTreatment;
import io.funraise.dm.blitz.domain.nested_mapper.target.ToIllness;
import io.funraise.dm.blitz.domain.nested_mapper.target.ToPatient;
import io.funraise.dm.blitz.domain.nested_mapper.target.ToTreatment;
import io.funraise.dm.blitz.domain.orika.source.FromCampaignPage;
import io.funraise.dm.blitz.domain.orika.source.FromCampaignSite;
import io.funraise.dm.blitz.domain.orika.source.OtherObject;
import io.funraise.dm.blitz.domain.orika.source.SourceObject;
import io.funraise.dm.blitz.domain.orika.target.ToCampaignSite;
import io.funraise.dm.blitz.domain.orikaforce.source.Person;
import io.funraise.dm.blitz.domain.remap_orika.source.FromCoach;
import io.funraise.dm.blitz.domain.remap_orika.source.FromPlayer;
import io.funraise.dm.blitz.domain.remap_orika.source.FromPosition;
import io.funraise.dm.blitz.domain.remap_orika.target.ToCoach;
import io.funraise.dm.blitz.domain.remap_orika.target.ToPlayer;
import io.funraise.dm.blitz.domain.remap_orika.target.ToPosition;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * Created by chi.kim on 6/2/15.
 */

@Test
@Guice(modules=ApplicationTestConfig.class)
public class DomainMapperTest {

    @Inject
    private DomainMapper dm;

    @Inject
    private UnitConverter uc;

    private Student1 s1;

    private Student1 student;

    private List<Student1> s1List;

    private HashSet<Student1> set;

    private FromPerson fp;

    @BeforeTest
    public void setUp() {

        s1 = new Student1();
        s1.setId(1);
        s1.setName("Tom Brady");
        s1.setAge(21);
        s1.setGpa(3.99);
        s1.setId(1);

        s1List = new ArrayList<>();

        s1List.add(s1);
        s1List.add(s1);
        s1List.add(s1);
        s1List.add(s1);
        s1List.add(s1);

        student = new Student1();
        student.setName("Peyton Manning");
        student.setAge(100);
        student.setGpa(2.10);
        set = new HashSet<>();
        set.add(s1);
        set.add(student);

        fp = new FromPerson();

        FromJob fj = new FromJob();
        fj.setAnnualPay(125000);
        fj.setPosition("Associate Engineer");
        fj.setYearsExperience(5);

        FromFriend friend1 = new FromFriend();
        friend1.setName("Chi");
        friend1.setAge(35);
        friend1.setFavoriteColor("Red");

        Set<Animal> pets = new HashSet<>();
        Animal cat = new Animal();
        Animal dog = new Animal();
        cat.setAge(3);
        cat.setKind("Cat");
        cat.setName("Sammy");
        dog.setAge(5);
        dog.setKind("Dog");
        dog.setName("Dexter");

        pets.add(cat);
        pets.add(dog);

        fp.setAge(21);
        fp.setName("Jackson");
        fp.setWeightLbs(185);
        fp.setHeightInches(73);
        fp.setJob(fj);
        fp.addFriend(friend1);
        fp.setPets(pets);
    }

    @Test
    public void testAutoMapping() {
        FromAuto fa = new FromAuto();
        fa.setA(1);
        fa.setB(2);
        fa.setC(3);
        ToAuto ta = dm.map(ToAuto.class, fa);
        Assert.assertEquals(ta.getA(), fa.getA());
        Assert.assertEquals(ta.getB(), fa.getB());
        Assert.assertEquals(ta.getC(), fa.getC());
    }

    /**
     * A mapping file for to ToClass exists, but the FromClass does not have an explicit mapping
     */
    @Test
    public void testAutoMappingNotDefined() {
        FromNotMapped fa = new FromNotMapped();
        fa.setA(1);
        fa.setB(2);
        fa.setC(3);
        ToAuto ta = dm.map(ToAuto.class, fa);
        Assert.assertEquals(ta.getA(), fa.getA());
        Assert.assertEquals(ta.getB(), fa.getB());
        Assert.assertEquals(ta.getC(), fa.getC());
    }

    @Test
    public void testEmbedded() {
        ToPerson tp = dm.map(ToPerson.class, fp);
        Assert.assertEquals(tp.getJob().getYearsExperience(), fp.getJob().getYearsExperience());
        Assert.assertEquals(tp.getJob().getTitle(), fp.getJob().getPosition());
    }



    @Test
    public void testMapWithExternalService() {
        ToPerson tp = dm.map(ToPerson.class, fp);
        Assert.assertEquals(tp.getWeightKgs(), uc.lbsToKgs(fp.getWeightLbs()));
        Assert.assertEquals(tp.getHeightCentimeters(), (int) uc.inchesToCentimeters(fp.getHeightInches()));
    }

    @Test
    public void testCollectionMapping() {
        ToPerson tp = dm.map(ToPerson.class, fp);
        for(ToFriend tf: tp.getFriends()) {
            FromFriend temp = new FromFriend();
            temp.setName(tf.getName());
            temp.setAge(tf.getAge());
            temp.setFavoriteColor(tf.getFavoriteColor());
            Assert.assertTrue(fp.getFriends().contains(temp));
        }
    }

    @Test
    public void testDomainMapper() throws Exception {
        Student2 s2 = dm.map(Student2.class, s1);
        Assert.assertEquals(s1.getName(), s2.getFirstName() + " " + s2.getLastName());
        Assert.assertEquals(s1.getAge(), s2.getNums().getAge());
        Assert.assertEquals(s1.getGpa(), s2.getNums().getGpa());
    }

    @Test
    public void testNamedMapping() throws Exception {
        Student2 s2 = dm.map(Student2.class, s1, "reverse");
        Assert.assertEquals(s1.getName(), s2.getLastName() + " " + s2.getFirstName());
        Assert.assertEquals(s1.getAge(), s2.getNums().getAge());
        Assert.assertEquals(s1.getGpa(), s2.getNums().getGpa());
    }

    @Test(enabled = false)
    public void timeTest() throws Exception {
        for (int j = 0; j < 20; j++) {
            Instant start = Instant.now();
            for (int i = 0; i < 1000000; i++) {
                dm.map(Student2.class, s1);
            }
            Instant stop = Instant.now();

            System.out.println(Duration.between(start, stop).toString().replaceAll("[^\\d.]", ""));
        }
    }

    @Test
    public void testNamedListMapping() throws Exception {

        List<Student2> s2List = dm.mapList(Student2.class, s1List, "reverse");

        s2List.stream().forEach(s2 -> {
            Assert.assertEquals(s1.getName(), s2.getLastName() + " " + s2.getFirstName());
            Assert.assertEquals(s1.getAge(), s2.getNums().getAge());
            Assert.assertEquals(s1.getGpa(), s2.getNums().getGpa());
        });
    }

    @Test
    public void testListMapping() throws Exception {

        List<Student2> s2List = dm.mapList(Student2.class, s1List);

        s2List.parallelStream().forEach(s2 -> {
            Assert.assertEquals(s1.getName(), s2.getFirstName() + " " + s2.getLastName());
            Assert.assertEquals(s1.getAge(), s2.getNums().getAge());
            Assert.assertEquals(s1.getGpa(), s2.getNums().getGpa());
        });
    }

    @Test
    public void testInnerCollections() throws Exception {
        MyList list = dm.map(MyList.class, set);
        set.stream().forEach(s -> {
            Student2 temp = new Student2();
            temp.setFirstName(s.getName().split(" ")[0]);
            temp.setLastName(s.getName().split(" ")[1]);
            Numbers n = new Numbers();
            n.setAge(s.getAge());
            n.setGpa(s.getGpa());
            temp.setNums(n);
            Assert.assertTrue(list.getData().contains(temp));
        });
    }

    @Test
    public void testDifferentSources() {
        FromAddress fa = new FromAddress();
        FromAnotherAddress faa= new FromAnotherAddress();

        fa.setStreet("10950 W Washington Blvd");
        fa.setZip("90232");

        faa.setStreet("10950 W Washington Blvd");
        faa.setZipPlusFour("90232-4026");

        ToAddress ta1 = dm.map(ToAddress.class, fa);
        ToAddress ta2 = dm.map(ToAddress.class, faa);

        Assert.assertEquals(ta1.getStreet(), ta2.getStreet());
        Assert.assertEquals(ta1.getZip(), ta2.getZip());


    }

    @Test
    public void testForceOrika() {
        Person person = new Person();
        person.setName("someName");
        person.setAddress1("address1");
        person.setAddress2("address2");

        io.funraise.dm.blitz.domain.orikaforce.target.Person targetPerson = dm.map(io.funraise.dm.blitz.domain.orikaforce.target.Person.class, person);

        Assert.assertEquals(person.getAddress1(), targetPerson.getAddress().getAddress1());
    }

    @Test
    public void testFluentSetters() {
        io.funraise.dm.blitz.domain.fluent.Student1 fluentS1 = new io.funraise.dm.blitz.domain.fluent.Student1()
            .setId(1).setName("Tom Brady")
            .setAge(21)
            .setGpa(3.99)
            .setId(1);

        io.funraise.dm.blitz.domain.fluent.Student2 fluentS2 = dm.map(
            io.funraise.dm.blitz.domain.fluent.Student2.class,
            fluentS1
        );
        Assert.assertEquals(fluentS1.getName(), fluentS2.getFirstName() + " " + fluentS2.getLastName());
        Assert.assertEquals(fluentS1.getAge(), fluentS2.getNums().getAge());
        Assert.assertEquals(fluentS1.getGpa(), fluentS2.getNums().getGpa());
    }

    @Test
    public void testCycle() {
        FromParent fromParent = new FromParent();
        fromParent.setName("Parent Name");

        FromChild fromChild = new FromChild();
        fromChild.setName("Child Name");
        fromParent.setChild(
          fromChild
        );

        ToParent toParent = dm.map(ToParent.class, fromParent);
        Assert.assertEquals(fromParent.getName(), toParent.getName());
        Assert.assertEquals(fromParent.getChild().getName(), toParent.getChild().getName());
    }

    @Test
    public void testCycleCollections() {
        FromEmployee fromEmployee = new FromEmployee();
        fromEmployee.setName("Employee Name");

        FromEmployee directReport = new FromEmployee();
        directReport.setName("Direct Report");

        fromEmployee.setDirectReports(Arrays.asList(directReport));

        ToEmployee toEmployee = dm.map(ToEmployee.class, fromEmployee);
        Assert.assertEquals(fromEmployee.getName(), toEmployee.getName());
        Assert.assertEquals(fromEmployee.getDirectReports().get(0).getName(), toEmployee.getDirectReports().get(0).getName());
    }

    @Test
    public void testUnmappedFields() {
        FromDriver fromDriver = new FromDriver();
        FromCar fromCar = new FromCar();
        fromCar.setMake("Toyota");
        fromDriver.setCar(fromCar);

        ToDriver toDriver = dm.map(ToDriver.class, fromDriver);
        Assert.assertEquals(toDriver.getCar().getMake(), fromDriver.getCar().getMake());
    }

    @Test
    public void testNestedMapper() {
        FromPatient fromPatient = new FromPatient()
            .setName("john")
            .setIllness(new FromIllness()
                .setName("fever")
                .setTreatment(new FromTreatment()
                    .setName("tylenol")));

        ToPatient expectedToPatient = new ToPatient()
            .setName("JOHN")
            .setIllness(new ToIllness()
                .setName("fever")
                .setTreatment(new ToTreatment()
                    .setName("TYLENOL")));

        ToPatient toPatient = dm.map(ToPatient.class, fromPatient);

        Assert.assertEquals(toPatient.toString(), expectedToPatient.toString());
    }

    @Test
    public void testEnumString() {
        FromQuestion fromQuestion = new FromQuestion();
        fromQuestion.setType(FromQuestionType.FREE_RESPONSE);
        fromQuestion.setNumber(2L);
        fromQuestion.setToType("X");
        ToQuestion toQuestion = dm.map(ToQuestion.class, fromQuestion);
        Assert.assertEquals(toQuestion.getType(), FromQuestionType.FREE_RESPONSE.toString());
        Assert.assertEquals(toQuestion.getNumber(), (Integer) 2);
        Assert.assertNull(toQuestion.getToType());

        fromQuestion = new FromQuestion();
        fromQuestion.setToType("A");
        toQuestion = dm.map(ToQuestion.class, fromQuestion);
        Assert.assertEquals(toQuestion.getToType(), ToQuestionType.A);
    }

    @Test
    public void testOrikaRemap() {
        FromCoach fromCoach = new FromCoach()
            .setPlayers(
                Arrays.asList(
                    new FromPlayer()
                        .setPosition(
                            new FromPosition()
                                .setName("Shortstop")
                        ),
                    new FromPlayer()
                        .setPosition(
                            new FromPosition()
                                .setName("Second Base")
                        )
                )
            );

        ToCoach expectedCoach = new ToCoach()
            .setAthletes(
                Arrays.asList(
                    new ToPlayer()
                        .setPosition(
                            new ToPosition()
                                .setName("Shortstop")
                        ),
                    new ToPlayer()
                        .setPosition(
                            new ToPosition()
                                .setName("Second Base")
                        )
                )
            );

        ToCoach toCoach = dm.map(ToCoach.class, fromCoach);

        Assert.assertEquals(toCoach.toString(), expectedCoach.toString());
    }

    @Test
    public void testMerge() {
        Student2 ontoStudent2 = new Student2();

        Student2 s2 = dm.merge(Student2.class, s1, ontoStudent2);
        Assert.assertEquals(s2.toString(), ontoStudent2.toString());
    }

    @Test
    public void testConfigConverter() {
        FromDateTime fromDateTime = new FromDateTime().setDateTime(LocalDateTime.now());

        ToDateTime toDateTime = dm.map(ToDateTime.class, fromDateTime);
        Assert.assertEquals(toDateTime.getDateTime().getNano(), fromDateTime.getDateTime().getNano());
    }

    @Test
    public void testEmptyList() {
        FromArtist fromArtist = new FromArtist().setArtistPaintings(Collections.emptyList());

        ToArtist toArtist = dm.map(ToArtist.class, fromArtist);

        Assert.assertNotNull(toArtist.getListOfPaintings());
        Assert.assertTrue(toArtist.getSetOfPaintings().isEmpty());

        Assert.assertNotNull(toArtist.getSetOfPaintings());
        Assert.assertTrue(toArtist.getSetOfPaintings().isEmpty());
    }

    @Test
    public void testSingleElementList() {
        final String name = "Mona Lisa";

        FromArtist fromArtist = new FromArtist().setArtistPaintings(singletonList(new FromPainting().setName(name)));

        ToArtist toArtist = dm.map(ToArtist.class, fromArtist);

        Assert.assertTrue(toArtist.getListOfPaintings().size() == 1);
        Assert.assertTrue(toArtist.getListOfPaintings().get(0).getName().equals(name));

        Assert.assertTrue(toArtist.getSetOfPaintings().size() == 1);
        Assert.assertTrue(toArtist.getSetOfPaintings().iterator().next().getName().equals(name));
    }

    @Test
    public void testConverter() {
        DomainMapperConfig domainMapperConfig = new DomainMapperConfig(
            "io.funraise.dm.blitz.domain",
            Thread.currentThread().getContextClassLoader(),
            singletonList(
                new CustomConverter<FromIllness, ToIllness>() {
                    @Override
                    public ToIllness convert(FromIllness source,
                        Type<? extends ToIllness> destinationType, MappingContext mappingContext) {
                        return new ToIllness()
                            .setName("flu")
                            .setTreatment(new ToTreatment()
                                .setName("bed rest"));
                    }

                    @Override
                    public boolean skipMapping() {
                        return true;
                    }
                }
            )
        );

        Injector injector = com.google.inject.Guice.createInjector(
            binder -> binder.bind(DomainMapperConfig.class).toInstance(domainMapperConfig)
        );

        FromPatient fromPatient = new FromPatient()
            .setName("john")
            .setIllness(new FromIllness()
                .setName("fever")
                .setTreatment(new FromTreatment()
                    .setName("tylenol")));

        ToPatient expectedToPatient = new ToPatient()
            .setName("JOHN")
            .setIllness(new ToIllness()
                .setName("flu")
                .setTreatment(new ToTreatment()
                    .setName("bed rest")));
        DomainMapper domainMapper = injector.getInstance(DomainMapper.class);

        ToPatient toPatient = domainMapper.map(ToPatient.class, fromPatient);

        Assert.assertEquals(toPatient.toString(), expectedToPatient.toString());
    }

    @Test
    public void testOrika_excludes() {
        FromCampaignPage fromCampaignPage = new FromCampaignPage();
        fromCampaignPage.setHomepage(true);

        FromCampaignSite fromCampaignSite = new FromCampaignSite();
        fromCampaignSite.setHomepage(fromCampaignPage);

        ToCampaignSite toCampaignSite = dm.map(ToCampaignSite.class, fromCampaignSite);

        Assert.assertTrue(toCampaignSite.isHomepage());
    }

    @Test
    public void testOrika_excludesWorksWithDifferentToClass() {
        SourceObject sourceObject = new SourceObject();
        sourceObject.setOtherField("other field");

        OtherObject otherObject = dm.map(OtherObject.class, sourceObject);

        Assert.assertEquals(otherObject.getOtherField(), "other field");
    }

    @Test
    public void testExplicit() {
        FromPet fromPet = new FromPet();
        fromPet.setNickname("Perderder");
        fromPet.setName("Potato");
        fromPet.setType("Feline");
        fromPet.setPetType(FromPet.Type.CAT);

        ToPet expected = new ToPet();
        expected.setType(FromPet.Type.CAT);
        expected.setName(fromPet.getNickname());

        ToPet actual = dm.map(ToPet.class, fromPet);

        Assert.assertEquals(actual.getName(), expected.getName(), "Should successfully map due to the explicit mapper");
        Assert.assertEquals(actual.getType(), expected.getType(), "Should successfully map due to the explicit mapper");
    }
}
