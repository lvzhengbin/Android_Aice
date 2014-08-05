package com.ice.android.common.eventbus;
/**
 * @阅读 ice
 * 承载PendingPost的消息队列，相当于Android中Handle机制中的Queue
 *
 */
final class PendingPostQueue {
    private PendingPost head;
    private PendingPost tail;

    /**
     * 入队列过程
     * @param pendingPost
     */
    synchronized void enqueue(PendingPost pendingPost) {
        if (pendingPost == null) {
            throw new NullPointerException("null cannot be enqueued");
        }
        if (tail != null) {
            tail.next = pendingPost;
            tail = pendingPost;
        } else if (head == null) {
            head = tail = pendingPost;
        } else {
            throw new IllegalStateException("Head present, but no tail");
        }
        notifyAll();
    }

    /**
     * 出队列过程
     * @return
     */
    synchronized PendingPost poll() {
        PendingPost pendingPost = head;
        if (head != null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        }
        return pendingPost;
    }

    synchronized PendingPost poll(int maxMillisToWait) throws InterruptedException {
        if (head == null) {
            wait(maxMillisToWait);
        }
        return poll();
    }

}
