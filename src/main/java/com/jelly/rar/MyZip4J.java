package com.jelly.rar;

import net.lingala.zip4j.ZipFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MyZip4J {

    public static void main(String[] args) {
        String filePath = "F:\\log-湖南\\2020-12-25_log\\autoUpdate.log_2020-12-17.zip";
        long start = System.currentTimeMillis();
        long password = verifyPassword(filePath,0,10000,100);
        long now = System.currentTimeMillis();
        System.out.println(now - start);
        System.out.println(password);

    }

    public static long verifyPassword(String filePath,long startIndex,long endIndex,long threadCount) {
        ZipFile zipFile = new ZipFile(filePath);
        long start = 0;
        long end = 0;
        long total = endIndex-startIndex;
//        int cnt = 100;
        List<ScheduledFuture<Long>> list = new ArrayList<>();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor((int) threadCount);
        executor.setRemoveOnCancelPolicy(true);
        for (int i = 0; i < threadCount; i++) {
            start = end;
            end = start + total / threadCount;
            UnZipCallable unZipCallable = new UnZipCallable();
            unZipCallable.setStartIndex(start);
            unZipCallable.setEndIndex(end);
            unZipCallable.setZipFile(zipFile);
            ScheduledFuture<Long> futureTask = executor.schedule(unZipCallable, 0, TimeUnit.SECONDS);
            list.add(futureTask);
        }

        long password = 0;
        while (!list.isEmpty()) {
            for (int i = list.size() - 1; i >= 0; i--) {
                ScheduledFuture<Long> task = list.get(i);
                if (!task.isDone()) {
                    continue;
                }
                Long obj = null;
                try {
                    obj = task.get();
//                    System.out.println(task + " is done " + obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                list.remove(i);
                if (null == obj) {
                    continue;
                }
                if (0 == obj.intValue()) {
                    continue;
                }
                password = obj;
                break;
            }
            if (0 != password) {
                list.stream().forEach(task -> {
                    boolean flag = task.cancel(true);
//                    System.out.println(task + " cancel " + flag+"\t"+task.isCancelled());
                });
                break;
            }
        }
        return password;
    }


}
