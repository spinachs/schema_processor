package com.jd.datamill9n.component.processor.post;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-30
 */
public abstract class Post {
    public abstract <T> T process(T table);
}
