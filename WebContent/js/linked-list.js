//----------------------------------------------------------------------------
// LinkedListElement
//----------------------------------------------------------------------------
function LinkedListElement(element) {
	
	/**
	 * The pointer to the next element,
	 * or <code>null</code> if the element
	 * is in the tail of the list.
	 */
	this.next = null;
	
	/**
	 * The pointer to the previous element,
	 * or <code>null</code> if the element
	 * is in the head of the list.
	 */
	this.prev = null;
	
	/**
	 * The element object inserted in the list.
	 */
	this.element = null;
	
	//-----------------------------------------
	// Constructor  code
	//-----------------------------------------
	this.element = element;
};

/**
 * Returns the element object hold by the list.
 * 
 * @return The element object hold by the list.
 */
LinkedListElement.prototype.getElement = function() {
	return this.element;
};

/**
 * Returns the list element that follows this one
 * or <code>null</code> if this element is in the
 * tail of the list. 
 * 
 * @return The list element that follows this one
 * or <code>null</code> if this element is in the
 * tail of the list.
 */
LinkedListElement.prototype.getNext = function() {
	return this.next;
};

/**
 * Returns the list element that precedes this one
 * or <code>null</code> if this element is in the
 * head of the list. 
 * 
 * @return The list element that precedes this one
 * or <code>null</code> if this element is in the
 * head of the list.
 */
LinkedListElement.prototype.getPrev = function() {
	return this.prev;
};

/**
 * Sets the element object hold by the list.
 * 
 * @param element The element object that will be hold by the list.
 */
LinkedListElement.prototype.setElement = function(element) {
	this.element = element;
};

/**
 * Sets the pointer to the next element in the list.
 * 
 * @param next The pointer to the next element in the list.
 */
LinkedListElement.prototype.setNext = function(next) {
	this.next = next;
};

/**
 * Sets the pointer to the previous element in the list.
 * 
 * @param next The pointer to the previous element in the list.
 */
LinkedListElement.prototype.setPrev = function(prev) {
	this.prev = prev;
};

//----------------------------------------------------------------------------
// LinkedListIterator
//----------------------------------------------------------------------------
function LinkedListIterator(element, list, forward) {
	
	/**
	 * The {@link LinkedListElement} pointed by this iterator.
	 */
	this.element = null;
	
	/**
	 * The {@link LnkedList} object on which this iterator moves.
	 */
	this.list = null;
	
	/**
	 * A boolean switch to indicate if the iterator was last used
	 * for a forwad or backward movement.
	 */
	this.forward = true;
	
	//-----------------------------------------
	// Constructor  code
	//-----------------------------------------
	this.element = element;
	this.list = list;
	if(forward == null)
		this.forward = true;
	else
		this.forward = forward;
};

/**
 * Returns <code>true</code> if this pointer is followed
 * by another element in the list, <code>false</code> otherwise.
 * @return <code>true</code> if this pointer is followed
 * by another element in the list, <code>false</code> otherwise.
 */
LinkedListIterator.prototype.hasNext = function() {
	return this.element != null;
};

/**
 * Returns <code>true</code> if this pointer is preceded
 * by another element in the list, <code>false</code> otherwise.
 * @return <code>true</code> if this pointer is preceded
 * by another element in the list, <code>false</code> otherwise.
 */
LinkedListIterator.prototype.hasPrev = function() {
	return this.element != null;
};

/**
 * Returns the element object pointed by this iterator and moves it on the
 * next element, or <code>null</code> if this iterator is not followed
 * by another element.
 * 
 * @return The element object pointed by this iterator, or <code>null</code>
 * if this iterator is not followed by another element.
 */
LinkedListIterator.prototype.next = function() {
	this.forward = true;
	if(!this.hasNext())
		return null;
	else{
		var ret = this.element.getElement();
		this.element = this.element.getNext();
		return ret;
	}
};

/**
 * Returns the element object pointed by this iterator and moves it on the
 * previous element, or <code>null</code> if this iterator is not preceded
 * by another element.
 * 
 * @return The element object pointed by this iterator, or <code>null</code>
 * if this iterator is not preceded by another element.
 */
LinkedListIterator.prototype.prev = function() {
	this.forward = false;
	if(!this.hasPrev())
		return null;
	else{
		var ret = this.element.getElement();
		this.element = this.element.getPrev();
		return ret;
	}
};

/**
 * Removes the element pointed by this iterator from the list
 * and returns it. If the list is empty, returns <code>null</code>.
 * If the last action on this iterator moved it forward, the element
 * pointed after the remove will be the element that follows the one
 * removed.
 * 
 * @return The element pointed by this iterator from the list
 * and returns it. If the list is empty, returns <code>null</code>.
 */
LinkedListIterator.prototype.remove = function() {
	if(this.element == null)
		return null;
	
	var ret = this.element.getElement();
	var prev = this.element.getPrev();
	var next = this.element.getNext();
	
	if(prev == null){
		this.head = next;
	}
	else{
		prev.setNext(next);
	}
	if(next == null){
		this.tail = prev;
	}
	else{
		next.setPrev(prev);
	}
	
	if(this.forward)
		this.element = next;
	else
		this.element = prev;
	
	this.list.size--;
	
	this.element.setNext(null);
	this.element.setPrev(null);
	
	return ret;
};

/**
 * Inserts the provided element in the list after the one pointed
 * by this iterator.
 * 
 * @param element The element that will be added to the list.
 */
LinkedListIterator.prototype.insertAfter = function(element) {
	if(this.element == null)
		this.list.add(element);
	else{
		var next = this.element.getNext();
		if(next == null)
			this.list.add(element);
		else{
			var embeddedEl = new LinkedListElement(element);
			embeddedEl.setPrev(this.element);
			embeddedEl.setNext(next);
			next.setPrev(embeddedEl);
			this.element.setNext(embeddedEl);
			this.list.size++;
		}
	}
};

/**
 * Inserts the provided element in the list before the one pointed
 * by this iterator.
 * 
 * @param element The element that will be added to the list.
 */
LinkedListIterator.prototype.insertBefore = function(element) {
	if(this.element == null)
		this.list.push(element);
	else{
		var prev = this.element.getPrev();
		if(prev == null)
			this.list.push(element);
		else{
			var embeddedEl = new LinkedListElement(element);
			embeddedEl.setNext(this.element);
			embeddedEl.setPrev(prev);
			prev.setNext(embeddedEl);
			this.element.setPrev(embeddedEl);
			this.list.size++;
		}
	}
};

/**
 * Inserts the provided element in the list. If the last action
 * on this iterator moved it forward, the element will be put
 * after this element, otherwise it will be put before it.
 *  
 * @param element The element that will be added to the list.
 */
LinkedListIterator.prototype.insert = function(element) {
	if(this.forward)
		this.insertAfter(element);
	else
		this.insertBefore(element);
};

//----------------------------------------------------------------------------
// LinkedList
//----------------------------------------------------------------------------
function LinkedList() {
	
	/**
	 * The head pointer.
	 */
	this.head = null;
	
	/**
	 * The tail pointer.
	 */
	this.tail = null;
	
	/**
	 * The size of the list.
	 */
	this.size = 0;
	
	//-----------------------------------------
	// Constructor  code
	//-----------------------------------------
	this.size = 0;
};

/**
 * Adds an element to the tail of the list.
 * 
 * @param element The element that will be added to the
 * tail of the list.
 */
LinkedList.prototype.add = function(element){
	var embeddedEl = new LinkedListElement(element);
	if(this.tail != null){
		this.tail.setNext(embeddedEl);
		embeddedEl.setPrev(this.tail); 
	}
	this.tail = embeddedEl;
	
	if(this.head == null){
		this.head = embeddedEl;
	}
	
	this.size++;
};

/**
 * Returns the element object present in the tail of the
 * list, without removing it. If the list is empty,
 * this method returns <code>null</code>.
 * 
 * @return The element object present in the tail of the
 * list. If the list is empty, this method returns
 * <code>null</code>.
 */
LinkedList.prototype.getLast = function(){
	if(this.tail == null)
		return null;
	else
		return this.tail.getElement();
};

/**
 * Returns the element object present in the tail of the
 * list and removes it. If the list is empty,
 * this method returns <code>null</code>.
 * 
 * @return The element object present in the tail of the
 * list. If the list is empty, this method returns
 * <code>null</code>.
 */
LinkedList.prototype.extract = function(){
	var ret = this.getLast();
	if(ret != null){
		var last = this.tail;
		this.tail = last.getPrev();
		if(last.getPrev() != null)
			last.getPrev().setNext(null);
		else
			this.head = null;
		this.size--;
	}
	return ret;
};

/**
 * Adds an element to the head of the list.
 * 
 * @param element The element that will be added to the
 * head of the list.
 */
LinkedList.prototype.push = function(element){
	var embeddedEl = new LinkedListElement(element);
	if(this.head != null){
		this.head.setPrev(embeddedEl);
		embeddedEl.setNext(this.head); 
	}
	this.head = embeddedEl;
	
	if(this.tail == null){
		this.tail = embeddedEl;
	}
	
	this.size++;
};

/**
 * Returns the element object present in the head of the
 * list, without removing it. If the list is empty,
 * this method returns <code>null</code>.
 * 
 * @return The element object present in the head of the
 * list. If the list is empty, this method returns
 * <code>null</code>.
 */
LinkedList.prototype.peek = function(){
	if(this.head == null)
		return null;
	else
		return this.head.getElement();
};

/**
 * Returns the element object present in the head of the
 * list and removes it. If the list is empty,
 * this method returns <code>null</code>.
 * 
 * @return The element object present in the head of the
 * list. If the list is empty, this method returns
 * <code>null</code>.
 */
LinkedList.prototype.pop = function(){
	var ret = this.peek();
	if(ret != null){
		var first = this.head;
		this.head = first.getNext();
		if(first.getNext() != null)
			first.getNext().setPrev(null);
		else
			this.tail = null;
		this.size--;
	}
	return ret;
};

/**
 * Returns the size of this list.
 * 
 * @return The size of this list.
 */
LinkedList.prototype.getSize = function(){
	return this.size;
};

/**
 * Returns the {@link LinkedListIterator} that
 * spans this list starting from the head.
 * @return The {@link LinkedListIterator} that
 * spans this list starting from the head.
 */
LinkedList.prototype.iterator = function(){
	return new LinkedListIterator(this.head, this);
};

/**
 * Returns the {@link LinkedListIterator} that
 * spans this list starting from the tail.
 * @return The {@link LinkedListIterator} that
 * spans this list starting from the tail.
 */
LinkedList.prototype.backIterator = function(){
	return new LinkedListIterator(this.tail, this, false);
};

/**
 * Removes all the elements from the list.
 */
LinkedList.prototype.clear = function(){
	while(this.head != null){
		this.pop();
	}
};

/**
 * Sorts the elements in the list. This method first backups
 * the element of the list in an array, than it sorts the array,
 * it clears the list and finally it puts the elements of the array
 * back in the list.
 * 
 * @param comparator An optional comparator function to sort elements.
 */
LinkedList.prototype.sort = function(comparator) {
	var backArray = new Array(this.getSize());
	var i = 0;
	for(var it = this.iterator(); it.hasNext(); ){
		backArray[i] = it.next();
		i++;
	}
	if(comparator != null && comparator != undefined)
		backArray.sort(comparator);
	else
		backArray.sort();
	
	this.clear();
	
	for(i = 0; i < backArray.length; i++){
		this.add(backArray[i]);
	}
};

LinkedList.prototype.toString = function() {
    var txt = '[';
    for(var it = this.iterator(); it.hasNext(); ){
        var el = it.next();
        txt += el;
        if(it.hasNext())
            txt += ', ';
    }
    txt += ']';
    return txt;
}