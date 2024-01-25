package com.magicauction.batchupdater.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListUtilsTest {

    List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9);

    @Test void nSizeParts_whenIsOk(){
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(1,2,3),Arrays.asList(4,5,6),Arrays.asList(7,8,9));
        List<List<Integer>> actual = ListUtils.nSizeParts(numbers, 3);
        assertEquals(expected, actual);
    }

    @Test void nTimeParts_whenIsOk(){
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(1,2,3),Arrays.asList(4,5,6),Arrays.asList(7,8,9));
        List<List<Integer>> actual = ListUtils.nSizeParts(numbers, 3);
        assertEquals(expected, actual);
    }

}