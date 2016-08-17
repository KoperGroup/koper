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

import java.util.concurrent.BlockingQueue;

/**
 * @author wds
 * @since 1.2
 */
public class RegItem {

    private Runnable runnable;
    private BlockingQueue blockingQueue;

    public RegItem(){}

    public RegItem(Runnable runnable,BlockingQueue blockingQueue){
        this.runnable = runnable;
        this.blockingQueue = blockingQueue;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public BlockingQueue getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegItem regItem = (RegItem) o;

        return !(runnable != null ? !runnable.equals(regItem.runnable) : regItem.runnable != null);

    }

    @Override
    public int hashCode() {
        return runnable != null ? runnable.hashCode() : 0;
    }
}
