/*
 * Iterator.h
 *
 *  Created on: 15/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef LIST_ITERATOR_H_
#define LIST_ITERATOR_H_

#include "Iterator.h"
#include <list>

using namespace std;

namespace cpp{
namespace util{

template<typename T>
class ListIterator : public Iterator<T>{

public:
	ListIterator(list<T> *theList);
	~ListIterator();

	virtual bool hasNext();
	virtual T next();
	virtual void remove();

	void add(const T &e);

	void set(const T &e);

protected:
	_List_iterator<T> it;
	_List_iterator<T> prevIt;
	list<T> *theList;

private:
	void init(list<T> *theList);
};

template<typename T>
ListIterator<T>::ListIterator(list<T> *theList){
	init(theList);
}

template<typename T>
ListIterator<T>::~ListIterator(){

}

template<typename T>
bool ListIterator<T>::hasNext(){
	return it != theList->end();
}

template<typename T>
T ListIterator<T>::next(){
	T value = *it;
	prevIt = it++;
	return value;
}

template<typename T>
void ListIterator<T>::remove(){
	theList->erase(prevIt);
}

template<typename T>
void ListIterator<T>::add(const T &e){
	theList->insert(prevIt, e);
}

template<typename T>
void ListIterator<T>::set(const T &e){
	theList->erase(prevIt);
	theList->insert(prevIt, e);
}

template<typename T>
void ListIterator<T>::init(list<T> *theList){
	it = theList->begin();
	prevIt = it;
	this->theList = theList;
}

}
}
#endif /* LIST_ITERATOR_H_ */
