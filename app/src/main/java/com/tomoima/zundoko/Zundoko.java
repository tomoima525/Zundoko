package com.tomoima.zundoko;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by tomoaki on 3/11/16.
 */
public class Zundoko {
    private static final String ZUN = "ズン";
    private static final String DOKO = "ドコ";
    HashMap<String, Integer> map = new HashMap();
    Integer[] zundoko = {0, 0, 0, 0, 1};
    Random rand = new Random();
    boolean isKiyoshi = false;

    public void run(){
        map.put(ZUN, 0);
        map.put(DOKO, 1);
        LinkedList<String> list = new LinkedList<>();
        while (!isKiyoshi) {
            list.add(rand.nextInt() % 2 == 0 ? ZUN : DOKO);
            if (list.size() > zundoko.length) {
                list.removeFirst();
            }
            System.out.println("¥¥ " + list.getLast());
            doLambdaKiyoshi(list);
        }
        System.out.println("¥¥キ・ヨ・シ！");
    }

    public void doKiyoshi(List<String> list) {
        Observable.from(list)
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return map.get(s);
                    }
                }).toList().map(new Func1<List<Integer>, Boolean>() {
            @Override
            public Boolean call(List<Integer> integers) {
                if (integers.size() < zundoko.length) return false;
                return Arrays.equals(integers.toArray(new Integer[zundoko.length]), zundoko);
            }
        })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean matched) {
                        isKiyoshi = matched;
                    }
                });
    }

    public void doLambdaKiyoshi(List<String> list) {
        Observable.from(list)
                .map(s -> map.get(s))
                .toList().map(zunInts ->
                {
                    if (zunInts.size() < zundoko.length) return false;
                    return Arrays.equals(zunInts.toArray(new Integer[zundoko.length]), zundoko);
                }
        ).subscribe(matched -> isKiyoshi = matched);
    }
}
