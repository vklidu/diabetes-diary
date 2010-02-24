/*
 *   Copyright (C) 2006-2007 Jiri Majer. All Rights Reserved.
 *   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   This code is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License version 2 only, as
 *   published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.diabetesdiary.print.pdf;

import com.google.common.collect.ImmutableList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Jirka Majer
 */
public final class TreeContainer<T> {

    private List<TreeContainer<T>> children = new ArrayList<TreeContainer<T>>();
    private TreeContainer<T> parent;
    private final T value;

    public TreeContainer(T value) {
        this(value, null);
    }

    private TreeContainer(T value, TreeContainer<T> parent) {
        this.value = value;
        this.parent = parent;
    }

    public List<TreeContainer<T>> getChildren() {
        return ImmutableList.copyOf(children);
    }

    public TreeContainer<T> getParent() {
        return parent;
    }

    public int getNumberOfSisters() {
        return parent == null ? 0 : parent.getChildren().size() - 1;
    }

    public int getMaxWidth() {
        if (children.size() == 0) {
            return 1;
        } else {
            int ret = 0;
            for (TreeContainer cont : children) {
                ret += cont.getMaxWidth();
            }
            return ret;
        }
    }

    public int getMaxDepth() {
        if (children.size() == 0) {
            return 0;
        } else {
            int ret = 0;
            for (TreeContainer cont : children) {
                ret = Math.max(cont.getMaxDepth(), ret);
            }
            return ret + 1;
        }
    }
    
    public int getHeight() {
        int res = 0;
        TreeContainer<T> node = this;
        while(node.parent != null) {
            node = node.parent;
            res++;
        }
        return res;
    }

    public int getMaxDepthOfSisters() {
        final int height = getHeight();
        final int[] res = new int[]{0};
        getRoot().walkBreathFirst(new RecursiveWalker<T>() {
            @Override
            public void onAction(TreeContainer<T> node) {
                if (node.getHeight() == height) {
                    res[0] = Math.max(res[0], node.getMaxDepth());
                }
            }
        });
        return res[0];
    }

    public TreeContainer<T> getRoot() {
        TreeContainer<T> result = this;
        while(result.getParent() != null) {
            result = result.getParent();
        }
        return result;
    }

    public TreeContainer<T> addChild(T value) {
        TreeContainer<T> child = new TreeContainer<T>(value, this);
        children.add(child);
        return child;
    }

    public TreeContainer<T> addChild(TreeContainer<T> value) {
        value.parent = this;
        children.add(value);
        return value;
    }

    /**
     * walk recursive to width
     * @param walker
     */
    public void walkBreathFirst(RecursiveWalker<T> walker) {
        Queue<TreeContainer<T>> queue = new ArrayDeque<TreeContainer<T>>();
        queue.add(this);
        while (!queue.isEmpty()) {
            TreeContainer<T> item = queue.remove();
            walker.onAction(item);
            queue.addAll(item.getChildren());
        }
    }

    public T getValue() {
        return value;
    }

    public static interface RecursiveWalker<T> {

        public void onAction(TreeContainer<T> node);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
