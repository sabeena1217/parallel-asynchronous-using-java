package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayListSpliteratorExampleTest {

    ArrayListSpliteratorExample arrayListSpliteratorExample = new ArrayListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValue() {

        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        List<Integer> resultList = arrayListSpliteratorExample.multiplyEachValue(inputList, 2, false);
        assertEquals(size, resultList.size());

    }


    @RepeatedTest(5)
    void multiplyEachValue_parallel() {

        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        List<Integer> resultList = arrayListSpliteratorExample.multiplyEachValue(inputList, 2, true);
        assertEquals(size, resultList.size());

    }

    /*
        Sequential
        [Test worker] - Total Time Taken : 74
        [Test worker] - Total Time Taken : 61
        [Test worker] - Total Time Taken : 17
        [Test worker] - Total Time Taken : 30
        [Test worker] - Total Time Taken : 53

        Parallel
        [Test worker] - Total Time Taken : 31
        [Test worker] - Total Time Taken : 13
        [Test worker] - Total Time Taken : 84
        [Test worker] - Total Time Taken : 50
        [Test worker] - Total Time Taken : 45

        ArrayList is easy to split into chunks, as it is indexed.
        When you run the code in parallel, we were able to achieve a greater performance compared to sequential stream
    * */
}