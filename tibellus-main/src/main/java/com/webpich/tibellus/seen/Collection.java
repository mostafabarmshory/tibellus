package com.webpich.tibellus.seen;

import java.util.Set;

public class Collection<T extends Model> {

	private Set<T> items;

	public Set<T> getItems() {
		return items;
	}

	public void setItems(Set<T> items) {
		this.items = items;
	}

}
