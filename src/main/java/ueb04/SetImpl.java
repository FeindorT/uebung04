package ueb04;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

class SetImpl<T extends Comparable<T>> implements Set<T> {
	/**
	 * Gibt einen Iterator zurück, welcher alle Elemente des Sets besucht.
	 */
	@Override
	public Iterator<T> iterator() {

		return new Iterator<T>() {

            List<Element> agenda = new LinkedList<>();

            {
                if (root != null)
                    agenda.add(root);
            }

            @Override
            public boolean hasNext() {
                return agenda.size() > 0;
            }

            @Override
            public T next() {

                Element e = agenda.remove(0);

                if (e.left != null)
                    agenda.add(e.left);

                if (e.right != null)
                    agenda.add(e.right);

                return e.val;

            }
        };

	}

	/**
	 * Bonusaufgabe: Gibt einen Iterator zurück, welcher nur die Knoten
	 */
	public Iterator<T> leafIterator(){
	    return new Iterator<T>() {

	        // Für eine Tiefensuche Stack verwenden
            Stack<Element> agenda = new StackImpl<>();

            Element next;

            {
                if (root != null) {
                    agenda.push(root);
                    next = findNextLeaf();
                }
            }

            private Element findNextLeaf(){

                while (agenda.size() > 0){

                    Element element = agenda.pop();

                    if (element.left == null && element.right == null)
                        return element;
                    if (element.left != null)
                        agenda.push(element.left);
                    if (element.right != null)
                        agenda.push(element.right);


                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public T next() {

                Element n = next;

                next = findNextLeaf();

                return n.val;
            }
        };

    }

	private class Element {
		T val;
		Element left, right;
		Element(T v, Element l, Element r) {
			val = v;
			left = l;
			right = r;
		}
		int size() {
			return 1
					+ (left == null ? 0 : left.size())
					+ (right == null ? 0 : right.size());
		}
		public String toString() {
			return val
					+ (left == null ? " - " : " ( " + left + ")")
					+ (right == null ? " - " : " ( " + right + ")");
		}
	}

	private Element root;

	@Override
	public boolean add(T s) {
		return addElement(new Element(s, null, null));
	}

	private boolean addElement(Element e) {
		if (e == null)
			return false;

		if (root == null) {
			root = e;
			return true;
		}

		Element it = root;
		while (it != null) {
			int c = e.val.compareTo(it.val);
			if (c == 0)
				return false;
			else if (c < 0) {
				if (it.left == null) {
					it.left = e;
					return true;
				} else
					it = it.left;
			} else {
				if (it.right == null) {
					it.right = e;
					return true;
				} else
					it = it.right;
			}
		}

		return false;
	}

	@Override
	public boolean contains(T s) {
		if (root == null)
			return false;

		Element it = root;
		while (it != null) {
			int c = s.compareTo(it.val);
			if (c == 0)
				return true;
			else if (c < 0) {
				it = it.left;
			} else {
				it = it.right;
			}
		}

		// nicht gefunden!
		return false;
	}

	@Override
	public int size() {
		if (root == null) {
			return 0;
		} else {
			return root.size();
		}
	}

	@Override
	public String toString() {
		if (root == null) {
			return "()";
		} else {
			return "(" + root.toString() + ")";
		}
	}
}
