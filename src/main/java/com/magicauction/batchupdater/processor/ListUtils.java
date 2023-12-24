package com.magicauction.batchupdater.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ListUtils {

    //Partes con tamanio N
    //Restos van a una lista extra
    public static <T> List<List<T>> nSizeParts(List<T> objs, final int N) {
        return new ArrayList<>(IntStream.range(0, objs.size()).boxed().collect(
                Collectors.groupingBy(e->e/N,Collectors.mapping(e->objs.get(e), Collectors.toList())
                )).values());
    }
    //N partes iguales
    //Restos se reparten en orden
    public static <T> List<List<T>> nTimeParts(List<T> objs, final int N) {
        return new ArrayList<>(IntStream.range(0, objs.size()).boxed().collect(
                Collectors.groupingBy(e->e%N,Collectors.mapping(e->objs.get(e), Collectors.toList())
                )).values());
    }
}
