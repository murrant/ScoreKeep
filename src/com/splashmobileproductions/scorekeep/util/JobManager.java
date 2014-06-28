package com.splashmobileproductions.scorekeep.util;

import java.util.ArrayList;

/**
 * Created by murrant on 6/27/14.
 */
public class JobManager {
    private ArrayList<PendingJob> mJobs = new ArrayList<PendingJob>();

    public void addJob(PendingJob job) {
        mJobs.add(job);
    }

    public void cancelJob(PendingJob job) {
        job.cancel();
        mJobs.remove(job);
    }

    public void cancelAllJobs() {
        for(PendingJob job : mJobs) job.cancel();
        mJobs.clear();
    }

    public void commitJob(PendingJob job) {
        job.commit();
        mJobs.remove(job);
    }

    public void commitAllJobs() {
        for(PendingJob job : mJobs) job.commit();
        mJobs.clear();
    }

    public static abstract class PendingJob implements Runnable{
        private Thread mThread;
        private final int mSleepTime;
        public static final int DEFAULT_SLEEP_TIME = 5000;  //default sleep time 5s
        private volatile boolean mCancelled = false;

        public PendingJob() {
            this(DEFAULT_SLEEP_TIME);
        }

        public PendingJob(int sleepTime) {
            mSleepTime = sleepTime;
            mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public final void run() {
            try {
                Thread.sleep(mSleepTime);
            } catch (InterruptedException e) {
                    if (mCancelled) {
                        return;
                    }
            }
            execute();
        }

        /**
         * Implement job in this method.
         */
        public abstract void execute();

        public final void commit() {
            mThread.interrupt();
        }

        public final void cancel() {
            mCancelled = true;
            mThread.interrupt();
        }
    }

}