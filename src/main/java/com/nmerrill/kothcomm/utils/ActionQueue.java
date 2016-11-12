package com.nmerrill.kothcomm.utils;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;

public class ActionQueue {
    private Queue<Action> queue;
    private int tiebreaker;
    private int time;
    public ActionQueue(){
        queue = new PriorityQueue<>();
        time = 0;
    }

    public void enqueue(Supplier<Integer> function, int delay){
        queue.add(new Action(function, time+delay, tiebreaker++));
    }

    public int getTime() {
        return time;
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public boolean tick(){
        if (!queue.isEmpty()){
            Action next = queue.poll();
            time = next.time;
            int delay = next.function.get();
            if (delay >= 0){
                enqueue(next.function, delay);
            }
            return true;
        }
        return false;
    }

    private class Action implements Comparable<Action> {
        private final Supplier<Integer> function;
        private final int time, tiebreaker;

        private Action(Supplier<Integer> function, int time, int tiebreaker){
            this.function = function;
            this.time = time;
            this.tiebreaker = tiebreaker;
        }

        @Override
        public int compareTo(Action o) {
            if (time != o.time){
                return time - o.time;
            }
            return tiebreaker - o.tiebreaker;
        }
    }


}
