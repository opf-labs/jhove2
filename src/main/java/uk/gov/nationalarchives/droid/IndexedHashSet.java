/*
 * © The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * http://www.tessella.com
 *
 * Tessella/NPD/4305
 * PRONOM 4
 */

package uk.gov.nationalarchives.droid;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * A data structure to store unique values indexed by a given key.
 * If an item added is found to duplicate an existing object, the
 * object is not added and no index is assigned.
 *
 * @author zeip
 */
public class IndexedHashSet<K, V> extends HashSet<V> {

    /* Pointers to values in HashSet */
    private Hashtable<K, V> indexedTable;

    /**
     * Create with given capacity
     *
     * @param initialCapacity
     */
    public IndexedHashSet(int initialCapacity) {
        super(initialCapacity);
        indexedTable = new Hashtable<K, V>(initialCapacity);
    }

    /**
     * Create with default size
     */
    public IndexedHashSet() {
        super();
        indexedTable = new Hashtable<K, V>();
    }

    /**
     * Add object
     *
     * @param key   the index to retrieve the object by
     * @param value the object to store
     * @return true if the set did not already contain o
     */
    public synchronized boolean add(K key, V value) {
        if (super.add(value)) {
            // Object did not already exist
            indexedTable.put(key, value);
            return true;
        } else {
            // Object already existed
            // indexedTable.put(key, value);
            return false;
        }
    }

    /**
     * Remove an item from both tables by index
     *
     * @param index index of item to remove
     */
    public void removeByIndex(K index) {
        if (indexedTable.containsKey(index)) {
            V item = indexedTable.get(index);
            super.remove(item);
            indexedTable.remove(index);

        }
    }

    public boolean containsKey(K index) {
        return indexedTable.containsKey(index);
    }

    /**
     * Clear all data
     */
    public void clear() {
        indexedTable.clear();
        super.clear();
    }

    /**
     * Get object by original key
     *
     * @param key
     * @return
     */
    public synchronized Object get(K key) {
        return indexedTable.get(key);
    }

    public Enumeration getIndexKeys() {
        return indexedTable.keys();
    }
}

