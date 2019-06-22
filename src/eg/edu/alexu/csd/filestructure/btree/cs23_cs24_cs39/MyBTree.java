package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import eg.edu.alexu.csd.filestructure.btree.IBTree;
import eg.edu.alexu.csd.filestructure.btree.IBTreeNode;

public class MyBTree<K extends Comparable<K>, V> implements IBTree<K, V> {
	private int minimumDegree;
	private IBTreeNode<K,V> root;
	
	public MyBTree ( int minimumDegree) {
		if(minimumDegree < 2) {
			throw new RuntimeErrorException(null);
		}
		this.minimumDegree = minimumDegree;
		this.root = new MyBTreeNode<K, V>();
		root.setNumOfKeys(0);
		root.setLeaf(true);
	}

	@Override
	public int getMinimumDegree() {
		
		int minimumDegree = this.minimumDegree;
		return minimumDegree;
	}

	@Override
	public IBTreeNode<K, V> getRoot() {
		if(this.root.getNumOfKeys()==0) {
			return null;
		}
		IBTreeNode<K, V> root = this.root;
		return root;
	}

	@Override
	public void insert(K key, V value) {
		
		if(key == null || value == null) {
			throw new RuntimeErrorException(null);
		}
		
		if(root.getNumOfKeys() == (2*minimumDegree - 1)) {
			IBTreeNode<K,V> s = new MyBTreeNode<K,V>();
			s.setLeaf(false);
			s.setNumOfKeys(0);
			setChildAtIndex(root, 0,s);
			root = s;
			splitChild(s, 0);
			insertNonFull(s, key, value);
			
		}
		else {
			insertNonFull( root, key, value);
		}

	}

	@Override
	public V search(K key) {
		if(key == null) {
			throw new RuntimeErrorException(null);
			}
		return searchInNode(root, key);
	}

	@Override
	public boolean delete(K key) {
		if(key == null) {
			throw new RuntimeErrorException(null);
		}
		return false;
	}
	
	private void splitChild(IBTreeNode<K,V> toBeSplit , int index) {
		IBTreeNode<K, V> split2 = new MyBTreeNode<K,V> ();
		IBTreeNode<K, V> split1 = (MyBTreeNode<K, V>) getChildAtIndex(index,toBeSplit);
		split2.setLeaf(split1.isLeaf());
		split2.setNumOfKeys(minimumDegree-1);
		
		for (int i=0 ; i<minimumDegree-1 ; i++) {
			setKeyAtIndex(getKeyAtIndex(i+minimumDegree,split1), i,split2);
			setValueAtIndex(getValueAtIndex(i+minimumDegree,split1), i,split2);
			
		}
		
		
		if (!split1.isLeaf()) {
			for (int i=0 ; i<minimumDegree  ; i++) {
				setChildAtIndex(getChildAtIndex(i+minimumDegree,split1),i,split2);
				
			}
			for (int i=0 ; i<minimumDegree  ; i++) {
				split1.getChildren().remove(minimumDegree);
			}
			
		}
		
		for (int i=0 ; i<minimumDegree-1 ; i++) {
			split1.getKeys().remove(minimumDegree);
			split1.getValues().remove(minimumDegree);
			
		}
		
		
		
		
		
		setChildAtIndex(split2, index+1,toBeSplit);
		
		
		setKeyAtIndex(getKeyAtIndex(minimumDegree-1,split1), index,toBeSplit);
		setValueAtIndex(getValueAtIndex(minimumDegree-1,split1), index,toBeSplit);
		
		split1.getKeys().remove(minimumDegree-1);
		split1.getValues().remove(minimumDegree-1);
		
		toBeSplit.setNumOfKeys(toBeSplit.getNumOfKeys()+1);
		split1.setNumOfKeys(minimumDegree-1);
	}
	
	private void insertNonFull(IBTreeNode<K,V> toBeInserted , K key , V value ) {
		int index = toBeInserted.getNumOfKeys()-1;
		
		if (toBeInserted.isLeaf() ) {
			while(index>=0 && key.compareTo(getKeyAtIndex(index,toBeInserted))<=0) {
				if(key.compareTo(getKeyAtIndex(index,toBeInserted))==0) {
					return;
				}
				index--;
			}
			setKeyAtIndex(key, index+1,toBeInserted);
			setValueAtIndex(value, index+1,toBeInserted);
			toBeInserted.setNumOfKeys(toBeInserted.getNumOfKeys()+1);
			
			}else {
			while(index>=0 && key.compareTo(getKeyAtIndex(index,toBeInserted))<=0) {
				if(key.compareTo(getKeyAtIndex(index,toBeInserted))==0) {
					return;
				}
				index--;
			}
			index++;
			if (getChildAtIndex(index,toBeInserted).getNumOfKeys() == (2*minimumDegree -1)) {
				splitChild(toBeInserted, index);
				if(key.compareTo(getKeyAtIndex(index,toBeInserted))>0) {
					index++;
				}
				
			}
			insertNonFull((MyBTreeNode<K, V>) getChildAtIndex(index,toBeInserted), key, value);
		}
	}
	
	private  V searchInNode (IBTreeNode<K,V> node , K key) {
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