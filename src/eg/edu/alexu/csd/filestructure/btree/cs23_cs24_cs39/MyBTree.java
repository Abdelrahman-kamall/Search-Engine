package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import java.util.ArrayList;

import eg.edu.alexu.csd.filestructure.btree.IBTree;
import eg.edu.alexu.csd.filestructure.btree.IBTreeNode;

public class MyBTree<K extends Comparable<K>, V> implements IBTree<K, V> {
	private int minimumDegree;
	private MyBTreeNode<K,V> root;
	
	public MyBTree ( int minimumDegree) {
		this.minimumDegree = minimumDegree;
		this.root = new MyBTreeNode<K, V>();
		root.setChildren(null);
		root.setKeys(null);
		root.setLeaf(true);
		root.setNumOfKeys(0);
		root.setValues(null);
	}

	@Override
	public int getMinimumDegree() {
		int minimumDegree = this.minimumDegree;
		return minimumDegree;
	}

	@Override
	public IBTreeNode<K, V> getRoot() {
		IBTreeNode<K, V> root = this.root;
		return root;
	}

	@Override
	public void insert(K key, V value) {
		MyBTreeNode<K,V> r = (MyBTreeNode<K, V>) root;
		if(root.getNumOfKeys() == (2*minimumDegree - 1)) {
			MyBTreeNode<K,V> s = (MyBTreeNode<K, V>) r;
			s.setLeaf(false);
			s.setNumOfKeys(0);
			setChildAtIndex(r, 0,s);
			splitChild(s, 0);
			insertNonFull(s, key, value);
			root = s;
		}
		else {
			insertNonFull((MyBTreeNode<K, V>) root, key, value);
		}

	}

	@Override
	public V search(K key) {
		return searchInNode(root, key);
	}

	@Override
	public boolean delete(K key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void splitChild(MyBTreeNode<K,V> toBeSplit , int index) {
		MyBTreeNode<K, V> split2 = new MyBTreeNode<K,V> ();
		MyBTreeNode<K, V> split1 = (MyBTreeNode<K, V>) getChildAtIndex(index,toBeSplit);
		split2.setLeaf(split1.isLeaf());
		split2.setNumOfKeys(minimumDegree-1);
		split2.setNumOfKeys(minimumDegree - 1);
		
		for (int i=0 ; i<minimumDegree - 1 ; i++) {
			setKeyAtIndex(getKeyAtIndex(i+minimumDegree,split1), i,split2);
			split1.getKeys().remove(i+minimumDegree);
			
			setValueAtIndex(getValueAtIndex(i+minimumDegree,split1), i,split2);
			split1.getValues().remove(i+minimumDegree);
		}
		
		if (!split1.isLeaf()) {
			for (int i=0 ; i<minimumDegree -1 ; i++) {
				setChildAtIndex(getChildAtIndex(i+minimumDegree,split1),i,split2);
				split1.getChildren().remove(i+minimumDegree);
			}
		}
		
		split1.setNumOfKeys(minimumDegree-1);
		
		for (int i=toBeSplit.getNumOfKeys()+1 ; 1>index+1 ; i--) {
			setChildAtIndex(getChildAtIndex(i,toBeSplit), i+1,toBeSplit);
		}
		
		setChildAtIndex(split2, index+1,toBeSplit);
		
		for (int i=toBeSplit.getNumOfKeys(); i>index ; i--) {
			setKeyAtIndex(getKeyAtIndex(i+1,toBeSplit), i,toBeSplit);
		}
		
		setKeyAtIndex(getKeyAtIndex(minimumDegree,split1), index,toBeSplit);
		
		toBeSplit.setNumOfKeys(toBeSplit.getNumOfKeys()+1);
	}
	
	private void insertNonFull(MyBTreeNode<K,V> toBeInserted , K key , V value ) {
		int index = toBeInserted.getNumOfKeys();
		
		if (toBeInserted.isLeaf()) {
			while(index>0 && key.compareTo(getKeyAtIndex(index,toBeInserted))<0) {
				setKeyAtIndex(getKeyAtIndex(index,toBeInserted), index+1,toBeInserted);
				setValueAtIndex(getValueAtIndex(index,toBeInserted), index+1,toBeInserted);
				index--;
			}
			setKeyAtIndex(key, index+1,toBeInserted);
			setValueAtIndex(value, index+1,toBeInserted);
			toBeInserted.setNumOfKeys(toBeInserted.getNumOfKeys()+1);
		}
		else {
			while(index>0 && key.compareTo(getKeyAtIndex(index,toBeInserted))<0) {
				index--;
			}
			index++;
			if (getChildAtIndex(index,toBeInserted).getNumOfKeys() == (2*minimumDegree -1)) {
				splitChild(toBeInserted, index);
				if(key.compareTo(getKeyAtIndex(index,toBeInserted))>0) {
					insertNonFull((MyBTreeNode<K, V>) getChildAtIndex(index,toBeInserted), key, value);
				}
			}
		}
	}
	
	private  V searchInNode (MyBTreeNode<K,V> node , K key) {
		int index = 0;
		
		while(index < node.getNumOfKeys() && key.compareTo(getKeyAtIndex(index,node))>0) {
			index++;
		}
		
		if(index < node.getNumOfKeys() && key.compareTo((K) getKeyAtIndex(index,node))==0) {
			return getValueAtIndex(index,node);
		}
		else if(node.isLeaf())
		return null;
		else {
			return searchInNode((MyBTreeNode<K, V>) getChildAtIndex(index,node), key);
		}
	}
	
	private void setChildAtIndex (IBTreeNode<K,V> child , int index , IBTreeNode<K,V> node) {
		ArrayList<IBTreeNode<K,V>> list = (ArrayList<IBTreeNode<K, V>>) node.getChildren();
		list.add(index, child);

	}
	private IBTreeNode<K,V> getChildAtIndex (int index , IBTreeNode<K,V> node){
		ArrayList<IBTreeNode<K,V>> list = (ArrayList<IBTreeNode<K, V>>) node.getChildren();
		return list.get(index); 
	}
	private void setKeyAtIndex (K key , int index ,IBTreeNode<K,V> node) {
		ArrayList<K> keys = (ArrayList<K>) node.getKeys();
		keys.add(index, key);
	}
	private K getKeyAtIndex (int index ,IBTreeNode<K,V> node){
		ArrayList<K> keys = (ArrayList<K>) node.getKeys();
		return keys.get(index); 
	}
	private void setValueAtIndex (V value , int index, IBTreeNode<K,V> node) {
		ArrayList<V> values = (ArrayList<V>) node.getValues();
		values.add(index, value);
	}
	private V getValueAtIndex (int index,IBTreeNode<K,V> node){
		ArrayList<V> values = (ArrayList<V>) node.getValues();
		
		return values.get(index); 
	}
	
	
}