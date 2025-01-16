package org.telosys.eclipse.plugin.core.commons;

public class Tuple2<T1, T2> {

	private final T1 object1;
	private final T2 object2;

	public Tuple2(T1 object1, T2 object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

	public T1 getElement1() {
		return object1;
	}

	public T2 getElement2() {
		return object2;
	}
	
}
