package com.jelly.rar;

import net.lingala.zip4j.ZipFile;

import java.util.concurrent.Callable;
import java.util.stream.LongStream;

public class UnZipCallable implements Callable<Long> {
    private long startIndex;
    private long endIndex;
    private ZipFile zipFile;
    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public void setZipFile(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public Long call() throws Exception {
//        System.out.println(startIndex + "----" + endIndex);
        Thread currentThread = Thread.currentThread();
        currentThread.setName(startIndex + "----" + endIndex);
        long password = LongStream.rangeClosed(startIndex, endIndex)
//                .parallel()
                .filter(i -> !currentThread.isInterrupted() && MyUnZipUtils.isPasswordCorrect(zipFile, String.valueOf(i)))
                .findAny()
                .orElse(0);
        return password;
    }
}
