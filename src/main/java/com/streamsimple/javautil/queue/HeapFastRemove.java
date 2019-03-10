/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsimple.javautil.queue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MinHeap
 * @param <T>
 */
public class HeapFastRemove<T>
{
  private List<T> heap = new ArrayList<T>();
  private Map<T, Integer> objectToIndex = new HashMap<>();
  private Comparator<T> comparator;

  public HeapFastRemove(Comparator<T> comparator)
  {
    this.comparator = comparator;
  }

  public boolean offer(T element)
  {
    if (objectToIndex.containsKey(element)) {
      return false;
    }

    int index = size();
    heap.add(element);
    objectToIndex.put(element, index);
    percolateUp(index, element);

    return true;
  }

  private void percolateUp(int index, T element)
  {
    while (index != 0) {
      int parentIndex = parent(index);
      T parent = heap.get(parentIndex);

      if (comparator.compare(parent, element) <= 0) {
        return;
      }

      heap.set(index, parent);
      heap.set(parentIndex, element);

      objectToIndex.put(parent, index);
      objectToIndex.put(element, parentIndex);

      index = parentIndex;
    }
  }

  private void percolateDown(int index)
  {
    while (index < size()) {
      final int firstIndex = firstChild(index);
      final int secondIndex = secondChild(index);

      final T first = nullGet(firstIndex);
      final T second = nullGet(secondIndex);

      final int nextIndex;
      final T next;

      if (nullCompare(first, second) <= 0) {
        nextIndex = firstIndex;
        next = first;
      } else {
        nextIndex = secondIndex;
        next = second;
      }

      if (next == null) {
        return;
      }

      heap.set(index, next);
      objectToIndex.put(next, index);

      index = nextIndex;
    }
  }

  private T nullGet(int index)
  {
    return index < size() ? heap.get(index) : null;
  }

  private int nullCompare(T first, T second)
  {
    if (first == null) {
      return 0;
    } else if (second == null) {
      return -1;
    } else {
      return comparator.compare(first, second);
    }
  }

  public int size()
  {
    return heap.size();
  }

  public static int parent(int index)
  {
    return index / 2;
  }

  public static int firstChild(int index)
  {
    return index * 2 + 1;
  }

  public static int secondChild(int index)
  {
    return index * 2 + 2;
  }
}