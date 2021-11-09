/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

public class MyLinkedList {
	public class Node {
		private Integer key;
		private Integer data;
		public Node next;
		public Node previous;

		public Node(Integer key, Integer data) {
			this.setKey(key);
			this.data = data;
			next = null;
			previous = null;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public Integer getData() {
			return data;
		}

		public void setData(Integer data) {
			this.data = data;
		}

	}

	public Node head = null;

	public boolean insert(Integer key, Integer data) {
		Node node = new Node(key, data);
		node.next = null;
		if (head == null)
			head = node;
		else {
			Node last = head;
			while (last.next != null && last.next != head) {
				// if ( last.key.equals(key))
				// return false;// unique key instead of search
				last = last.next;
			}
			// if(last.item.key.equals(key))
			// return false;
			last.next = node;
			node.previous = last;
			head.previous = node;
			node.next = head;
		}
		return true;
	}

	public boolean delete(Integer key) {
		if (head == null)
			return false;
		if (head.getKey().equals(key)) {
			head = head.next;
			return true;
		}
		Node prev = head;
		Node pointer = head.next;
		while (pointer != null) {
			if (pointer.getKey().equals(key)) {
				prev.next = pointer.next;
				return true;
			}
			prev = pointer;
			pointer = pointer.next;
		}
		return false;
	}

	public Integer search(Integer key) {
		Node pointer = head;
		while (pointer != null) {
			if (pointer.getKey().equals(key)) {
				return pointer.data;
			}
			pointer = pointer.next;
		}
		return null;
	}

	public void print() {
		Node pointer = head;
		while (pointer != null) {
			System.out.print(pointer.getKey() + " -> " + pointer.data + " ");
			pointer = pointer.next;
		}
		System.out.println();
	}
}
