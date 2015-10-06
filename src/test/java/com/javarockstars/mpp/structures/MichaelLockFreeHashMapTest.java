package com.javarockstars.mpp.structures;

import com.javarockstars.mpp.structures.test.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public class MichaelLockFreeHashMapTest {
    private MichaelLockFreeHashMap<Integer, Object> map;

    @Before
    public void setUp() {
        map = new MichaelLockFreeHashMap<>();
    }

    @After
    public void tearDown() {
        map = null;
    }

    @Test
    public void testSize() {
        assertThat(map.size(), is(equalTo(0)));
        Person andrej = new Person(1, "Andrej");
        Person shivam = new Person(2, "Shivam");
        map.put(andrej.getId(), andrej);
        assertThat(map.size(), is(equalTo(1)));
        map.put(shivam.getId(), shivam);
        assertThat(map.size(), is(equalTo(2)));
    }

    @Test
    public void testGet() {
        assertThat(map.size(), is(equalTo(0)));
        Person andrej = new Person(1, "Andrej");
        Person shivam = new Person(2, "Shivam");
        map.put(andrej.getId(), andrej);
        map.put(shivam.getId(), shivam);
        Object get = map.get(-1);
        assertThat(get, is(nullValue()));
        get = map.get(andrej.getId());
        assertThat(get, is(notNullValue()));
        assertThat(andrej, is(equalTo(get)));
        get = map.get(shivam.getId());
        assertThat(get, is(notNullValue()));
        assertThat(shivam, is(equalTo(get)));
    }

    @Test
    public void testPut() {
        assertThat(map.size(), is(equalTo(0)));
        Person andrej = new Person(1, "Andrej");
        Person shivam = new Person(2, "Shivam");
        assertThat(map.put(andrej.getId(), andrej), is(true));
        assertThat(map.put(shivam.getId(), shivam), is(true));
        assertThat(map.put(andrej.getId(), new Object()), is(false));
    }

    @Test
    public void testContains() {
        assertThat(map.size(), is(equalTo(0)));
        Person andrej = new Person(1, "Andrej");
        Person shivam = new Person(2, "Shivam");
        map.put(andrej.getId(), andrej);
        assertThat(map.contains(andrej.getId()), is(true));
        assertThat(map.contains(shivam.getId()), is(false));
        map.put(shivam.getId(), shivam);
        assertThat(map.contains(andrej.getId()), is(true));
        assertThat(map.contains(shivam.getId()), is(true));
    }

    @Test
    public void testRemove() {
        assertThat(map.size(), is(equalTo(0)));
        Person andrej = new Person(1, "Andrej");
        Person shivam = new Person(2, "Shivam");
        map.put(andrej.getId(), andrej);
        assertThat(map.contains(andrej.getId()), is(true));
        assertThat(map.remove(andrej.getId()), is(true));
        assertThat(map.contains(andrej.getId()), is(false));
        map.put(shivam.getId(), shivam);
        assertThat(map.contains(shivam.getId()), is(true));
        assertThat(map.remove(shivam.getId()), is(true));
        assertThat(map.contains(shivam.getId()), is(false));
        assertThat(map.remove(-1), is(false));
    }
}