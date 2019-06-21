package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

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
			s.setChildAtIndex(r, 0);
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
	
	public void splitChild(MyBTreeNode<K,V> toBeSplit , int index) {
		MyBTreeNode<K, V> split2 = new MyBTreeNode<K,V> ();
		MyBTreeNode<K, V> split1 = (MyBTreeNode<K, V>) toBeSplit.getChildAtIndex(index);
		split2.setLeaf(split1.isLeaf());
		split2.setNumOfKeys(minimumDegree-1);
		split2.setNumOfKeys(minimumDegree - 1);
		
		for (int i=0 ; i<minimumDegree - 1 ; i++) {
			split2.setKeyAtIndex(split1.getKeyAtIndex(i+minimumDegree), i);
			split1.getKeys().remove(i+minimumDegree);
			
			split2.setValueAtIndex(split1.getValueAtIndex(i+minimumDegree), i);
			split1.getValues().remove(i+minimumDegree);
		}
		
		if (!split1.isLeaf()) {
			for (int i=0 ; i<minimumDegree -1 ; i++) {
				split2.setChildAtIndex(split1.getChildAtIndex(i+minimumDegree),i);
				split1.getChildren().remove(i+minimumDegree);
			}
		}
		
		split1.setNumOfKeys(minimumDegree-1);
		
		for (int i=toBeSplit.getNumOfKeys()+1 ; 1>index+1 ; i--) {
			toBeSplit.setChildAtIndex(toBeSplit.getChildAtIndex(i), i+1);
		}
		
		toBeSplit.setChildAtIndex(split2, index+1);
		
		for (int i=toBeSplit.getNumOfKeys(); i>index ; i--) {
			toBeSplit.setKeyAtIndex(toBeSplit.getKeyAtIndex(i+1), i);
		}
		
		toBeSplit.setKeyAtIndex(split1.getKeyAtIndex(minimumDegree), index);
		
		toBeSplit.setNumOfKeys(toBeSplit.getNumOfKeys()+1);
	}
	
	public void insertNonFull(MyBTreeNode<K,V> toBeInserted , K key , V value ) {
		int index = toBeInserted.getNumOfKeys();
		
		if (toBeInserted.isLeaf()) {
			while(index>0 && key.compareTo(toBeInserted.getKeyAtIndex(index))<0) {
				toBeInserted.setKeyAtIndex(toBeInserted.getKeyAtIndex(index), index+1);
				toBeInserted.setValueAtIndex(toBeInserted.getValueAtIndex(index), index+1);
				index--;
			}
			toBeInserted.setKeyAtIndex(key, index+1);
			toBeInserted.setValueAtIndex(value, index+1);
			toBeInserted.setNumOfKeys(toBeInserted.getNumOfKeys()+1);
		}
		else {
			while(index>0 && key.compareTo(toBeInserted.getKeyAtIndex(index))<0) {
				index--;
			}
			index++;
			if (toBeInserted.getChildAtIndex(index).getNumOfKeys() == (2*minimumDegree -1)) {
				splitChild(toBeInserted, index);
				if(key.compareTo(toBeInserted.getKeyAtIndex(index))>0) {
					insertNonFull((MyBTreeNode<K, V>) toBeInserted.getChildAtIndex(index), key, value);
				}
			}
		}
	}
	
	public V searchInNode (MyBTreeNode<K,V> node , K key) {
		int index = 0;
		
		while(index < node.getNumOfKeys() && key.compareTo(node.getKeyAtIndex(index))>0) {
			index++;
		}
		
		if(index < node.getNumOfKeys() && key.compareTo((K) node.getKeyAtIndex(index))==0) {
			return node.getValueAtIndex(index);
		}
		else if(node.isLeaf())
		return null;
		else {
			return searchInNode((MyBTreeNode<K, V>) node.getChildAtIndex(index), key);
		}
	}
	
	
}