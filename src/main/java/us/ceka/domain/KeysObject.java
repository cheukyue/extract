package us.ceka.domain;

import java.util.ArrayList;
import java.util.List;

public class KeysObject extends AbstractObject<KeysObject> implements Comparable<KeysObject>{
	
    private List<Object> keys;

    public KeysObject( Object... objs ) {
        keys = new ArrayList<Object>();
        for (int i = 0; i < objs.length; i++)  keys.add( objs[i] );
        
    }
    
    public boolean equals(Object obj) {
    	if(obj == null) return false;
    	if(!(obj instanceof KeysObject)) return false;
    	KeysObject keyObj = (KeysObject) obj;
    	for(int i=0; i < keyObj.getKeys().size(); i++) {
    		if(!keyObj.getKeys().get(i).equals(keys.get(i))) return false;
    	}
    	return true;
    }
    
    
    public int hashCode() {
    	int hash = 1;
    	for(Object obj : keys) hash = 31 * hash + obj.hashCode();
    	return hash;
    }

	public List<Object> getKeys() {
		return keys;
	}

	public void setKeys(List<Object> keys) {
		this.keys = keys;
	}

	@Override
	public int compareTo(KeysObject obj) {
		int result = 0;
		for(int i=0; i < keys.size(); i++) {
			if(keys.get(i) instanceof String) {
				result = ((String)keys.get(i)).compareTo((String)obj.getKeys().get(i));
				if(result != 0) return result;
			}
			if(keys.get(i) instanceof Integer) {
				result = ((Integer)keys.get(i)).compareTo((Integer)obj.getKeys().get(i));
				if(result != 0) return result;
			}
		}
		return 0;
	}

}
