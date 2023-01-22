package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkedListSpliteratorExampleTest {

    LinkedListSpliteratorExample linkedListSpliteratorExample = new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValue() {

        int size = 1000000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);

        List<Integer> resultList = linkedListSpliteratorExample.multiplyEachValue(inputList, 2, false);
        assertEquals(size, resultList.size());

    }

    @RepeatedTest(5)
    void multiplyEachValue_parallel() {

        int size = 1000000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);

        List<Integer> resultList = linkedListSpliteratorExample.multiplyEachValue(inputList, 2, true);
        assertEquals(size, resultList.size());

    }

    /*
        Sequential
        [Test worker] - Total Time Taken : 99
        [Test worker] - Total Time Taken : 80
        [Test worker] - Total Time Taken : 67
        [Test worker] - Total Time Taken : 17
        [Test worker] - Total Time Taken : 87

        Parallel
        [Test worker] - Total Time Taken : 243
        [Test worker] - Total Time Taken : 415
        [Test worker] - Total Time Taken : 457
        [Test worker] - Total Time Taken : 365
        [Test worker] - Total Time Taken : 362

        When using parallelstreams, under the hood, Splitting , Executing and Combining happens
        LinkedList is difficult to split into chunks
        Hence invoking parallel streams doesn't guarantee faster performance of your code
    * */
}