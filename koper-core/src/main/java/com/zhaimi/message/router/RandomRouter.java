/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhaimi.message.router;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wds
 * @since 1.2
 */
public class RandomRouter implements Router {

    private List<RegItem> mailBoxList = new ArrayList<>();
    private ReentrantReadWriteLock mailBoxLock = new ReentrantReadWriteLock();
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public void registerThreadMailBox(Runnable runnable, BlockingQueue mailBox) {
        mailBoxLock.writeLock().lock();
        try {
            mailBoxList.add(new RegItem(runnable, mailBox));
        } finally {
            mailBoxLock.writeLock().unlock();
        }
    }

    @Override
    public void dispatch(Object msgBean) throws InterruptedException {
        while (mailBoxList.isEmpty()) {
            Thread.sleep(1000);
        }
        mailBoxLock.readLock().lock();
        try {
            BlockingQueue queue = mailBoxList.get(random.nextInt(mailBoxList.size())).getBlockingQueue();
            queue.put(msgBean);
        } finally {
            mailBoxLock.readLock().unlock();
        }
    }

    @Override
    public void deRegister(Runnable runnable) {
        mailBoxLock.writeLock().lock();
        try {
            for (RegItem regItem : mailBoxList) {
                if (runnable.equals(regItem.getRunnable())) {
                    mailBoxList.remove(regItem);
                    break;
                }
            }
        } finally {
            mailBoxLock.writeLock().unlock();
        }
    }


}
