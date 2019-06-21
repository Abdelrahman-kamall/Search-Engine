package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import java.util.List;

import eg.edu.alexu.csd.filestructure.btree.IBTreeNode;

public class MyBTreeNode<K extends Comparable<K>, V> implements IBTreeNode<K, V> {

	private int numOfKeys;
	private boolean isLeaf;
	private List<K> keys;
	private List<V> values;
	private List<IBTreeNode<K, V>> children;
	
	@Override
	public int getNumOfKeys() {
		int numOfKeys = this.numOfKeys;
		return numOfKeys;
	}

	@Override
	public void setNumOfKeys(int numOfKeys) {
		this.numOfKeys = numOfKeys;
	}

	@Override
	public boolean isLeaf() {
		boolean isLeaf = this.isLeaf;
		return isLeaf;
	}

	@Override
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;

	}

	@Override
	public List<K> getKeys() {
		List<K> keys = this.keys;
		return keys;
	}

	@Override
	public void setKeys(List<K> keys) {
		this.keys = keys;

	}

	@Override
	public List<V> getValues() {
		List<V> values = this.values;
		return values;
	}

	@Override
	public void setValues(List<V> values) {
		this.values = values;
	}

	@Override
	public List<IBTreeNode<K, V>> getChildren() {
		List<IBTreeNode<K, V>> children = this.children;
		return children;
	}

	@Override
	public void setChildren(List<IBTreeNode<K, V>> children) {
		this.children = children;

	}
	
	public void setChildAtIndex (IBTreeNode<K,V> child , int index) {
		this.children.add(index, child);
	}
	public IBTreeNode<K,V> getChildAtIndex (int index){
		IBTreeNode<K,V> child = this.children.get(index);
		return child; 
	}
	public void setKeyAtIndex (K key , int index) {
		this.keys.add(index, key);
	}
	public K getKeyAtIndex (int index){
		K key = this.keys.get(index);
		return key; 
	}
	public void setValueAtIndex (V value , int index) {
		this.values.add(index, value);
	}
	public V getValueAtIndex (int index){
		V value = this.values.get(index);
		return value; 
	}
	
	}