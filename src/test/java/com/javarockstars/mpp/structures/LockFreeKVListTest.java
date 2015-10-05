package com.javarockstars.mpp.structures;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public class LockFreeKVListTest extends Assert {

    private LockFreeKVList<String, String> list;

    @Before
    public void setUp() {
        list = new LockFreeKVList<>();
    }

    @After
    public void tearDown() {
        list = null;
    }

    @Test
    public void testInsert() {
        assertThat(list.contains("one"), is(false));
        Node<String, String> node = new Node<>("one".hashCode(), "one", "one is better than zero");
        assertThat(list.insert(node), is(true));
        assertThat(list.contains("one"), is(true));

        assertThat(list.contains("two"), is(false));
        node = new Node<>("two".hashCode(), "two", "two tops one");
        assertThat(list.insert(node), is(true));
        assertThat(list.contains("two"), is(true));
    }

    @Test
    public void testDelete() {
        assertThat(list.contains("one"), is(false));
        Node<String, String> node = new Node<>("one".hashCode(), "one", "one is better than zero");
        list.insert(node);
        assertThat(list.contains("one"), is(true));

        assertThat(list.contains("two"), is(false));
        node = new Node<>("two".hashCode(), "two", "two tops one");
        list.insert(node);
        assertThat(list.contains("two"), is(true));

        assertThat(list.delete("one"), is(true));

        assertThat(list.contains("one"), is(false));
        assertThat(list.contains("two"), is(true));

        assertThat(list.delete("two"), is(true));

        assertThat(list.contains("one"), is(false));
        assertThat(list.contains("two"), is(false));
    }

    @Test
    public void testContains() {
        assertThat(list.contains("one"), is(false));
        Node<String, String> node = new Node<>("one".hashCode(), "one", "one is better than zero");
        list.insert(node);
        assertThat(list.contains("one"), is(true));
        list.delete("one");
        assertThat(list.contains("one"), is(false));
    }

    @Test
    public void testGet() {
        assertThat(list.contains("one"), is(false));
        Node<String, String> node = new Node<>("one".hashCode(), "one", "one is better than zero");
        list.insert(node);
        assertThat(list.contains("one"), is(true));

        Node<String, String> result = list.get("one");
        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo(node)));

        list.delete("one");
        assertThat(list.contains("one"), is(false));
        result = list.get("one");
        assertThat(result, is(nullValue()));
    }
}