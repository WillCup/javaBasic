package com.will.tooljars.monitor.trove4j;

import gnu.trove.list.linked.TLinkedList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Trove还提供了开放寻址法的Map,Set,LinkedList实现.
 * 可以参考Enhance Collection Performance with this Treasure Trove 的做法.
 * 类似于此例子。
 * 
 * @author Will
 * @created at 2014-8-8 上午10:44:59
 */
public class SimpleCollectionFactory {
  static boolean useTrove = true;

    /**
  *  Return a hashmap based on the properties
  */
  public static Map getHashMap() {
      if ( useTrove ) return new THashMap();
      else            return new HashMap();
  }

  /**
  *  Return a hashset based on the properties
  */
  public static Set getHashSet() {
      if ( useTrove ) return new THashSet();
      else            return new HashSet();
  }

  /**
  *  Return a linkedlist based on the properties
  */
  public static List getLinkedList() {
      if ( useTrove ) return new TLinkedList();
      else            return new LinkedList();
  }
}