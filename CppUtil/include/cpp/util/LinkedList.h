/*
 * LinkedList.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef LINKEDLIST_H_
#define LINKEDLIST_H_

#include <list>
#include <sstream>
#include "List.h"
#include "cpp/lang/ArrayIndexOutOfBoundException.h"
#include "cpp/lang/NullPointerException.h"

namespace cpp{
namespace util{

template<typename T>
class LinkedList : public List<T>{
public:
	LinkedList();
	~LinkedList();

	virtual bool add(const T & e);
	virtual void add(int index, T & e);
	virtual bool addAll(Collection<T> *c);
	virtual void clear();
	virtual bool contains(const T & e);
	virtual bool containsAll(Collection<T> *c);
	virtual bool isEmpty();
	virtual Iterator<T>* iterator();
	virtual bool remove(const T & e);
	virtual bool removeAll(Collection<T> *c);
	virtual bool retainAll(Collection<T> *c);
	virtual int size();
	virtual T* toArray();

	virtual bool addAll(int index, Collection<T> *c);
	virtual T get(int index);
	virtual T operator[](int index);
	virtual int lastIndexOf(const T & e);
	virtual ListIterator<T>* listIterator();
	virtual ListIterator<T>* listIterator(int index);
	virtual T remove(int index, bool isIndex);
	virtual T set(int index, const T &element);
	virtual List<T>* subList(int fromIndex, int toIndex);

protected:
	std::list<T> *innerList;
};

template<typename T>
LinkedList<T>::LinkedList(){
	innerList = new std::list<T>();
}

template<typename T>
LinkedList<T>::~LinkedList(){
	delete innerList;
}

template<typename T>
bool LinkedList<T>::add(const T &e){
	innerList->push_back(e);
	return true;
}

template<typename T>
void LinkedList<T>::add(int index, T &e){
	if(index < 0 || index >= (int)innerList->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(index);
	}
	_List_iterator<T> it = innerList->begin();
	for(int i = 0; i < index; i++){
		it++;
	}
	innerList->insert(it, e);
}

template<typename T>
bool LinkedList<T>::addAll(Collection<T> *c){
	if(c == NULL){
		throw cpp::lang::NullPointerException();
	}

	Iterator<T> *it = NULL;
	for(it = c->iterator(); it->hasNext();){
		T value = it->next();
		innerList->push_back(value);
	}
	delete it;
	return true;
}

template<typename T>
void LinkedList<T>::clear(){
	innerList->clear();
}

template<typename T>
bool LinkedList<T>::contains(const T &e){
	// This method does a simple search, without ordering the inner list
	bool ret = false;
	Iterator<T> *it = NULL;
	for(it = this->iterator(); it->hasNext();){
		T value = it->next();
		if(value == e){
			ret = true;
			break;
		}
	}
	delete it;
	return ret;
}

template<typename T>
bool LinkedList<T>::containsAll(Collection<T> *c){
	if(c == NULL){
		throw cpp::lang::NullPointerException();
	}

	// This method does a simple search, without ordering the inner list
	// or the provided collection
	bool ret = true;
	Iterator<T> *it = NULL;
	for(it = c->iterator(); it->hasNext() && ret;){
		T value = it->next();
		ret = ret && this->contains(value);
	}
	delete it;
	return ret;
}

template<typename T>
bool LinkedList<T>::isEmpty(){
	return (innerList->size() == 0);
}

template<typename T>
Iterator<T>* LinkedList<T>::iterator(){
	return listIterator();
}

template<typename T>
bool LinkedList<T>::remove(const T & e){
	Iterator<T> *it = NULL;
	for(it = this->iterator(); it->hasNext();){
		T value = it->next();
		if(value == e){
			it->remove();
			break;
		}
	}
	delete it;
	return true;
}

template<typename T>
bool LinkedList<T>::removeAll(Collection<T> *c){
	if(c == NULL){
		throw cpp::lang::NullPointerException();
	}

	Iterator<T> *it = NULL;
	for(it = c->iterator(); it->hasNext();){
		T value = it->next();
		this->remove(value);
	}
	delete it;
	return true;
}

template<typename T>
bool LinkedList<T>::retainAll(Collection<T> *c){
	if(c == NULL){
		throw cpp::lang::NullPointerException();
	}

	Iterator<T> *it = NULL;
	for(it = c->iterator(); it->hasNext();){
		T value = it->next();
		if(!this->contains(value)){
			this->remove(value);
		}
	}
	delete it;
	return true;
}

template<typename T>
int LinkedList<T>::size(){
	return innerList->size();
}

template<typename T>
T* LinkedList<T>::toArray(){
	T* ret = new T[this->size()];
	Iterator<T> *it = NULL;
	int i = 0;
	for(it = this->iterator(); it->hasNext();){
		T value = it->next();
		ret[i] = value;
		i++;
	}
	delete it;
	return ret;
}

template<typename T>
bool LinkedList<T>::addAll(int index, Collection<T> *c){
	if(c == NULL){
		throw cpp::lang::NullPointerException();
	}
	if(index < 0 || index >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(index);
	}

	ListIterator<T> *it = NULL;
	Iterator<T> *it2 = NULL;

	int i = 0;
	for(it = this->listIterator(); it->hasNext();){
		if(i >= index)
			break;
		it->next();
		i++;
	}

	for(it2 = c->iterator(); it2->hasNext();){
		T value = it2->next();
		it->add(value);
		it->next();
	}

	delete it;
	delete it2;
	return true;
}

template<typename T>
T LinkedList<T>::get(int index){
	if(index < 0 || index >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(index);
	}

	T ret;
	Iterator<T> *it = NULL;
	int i = 0;
	for(it = this->iterator(); it->hasNext();){
		T value = it->next();
		if(i == index){
			ret = value;
			break;
		}
		i++;
	}
	delete it;
	return ret;
}

template<typename T>
T LinkedList<T>::operator[](int index){
	return this->get(index);
}

template<typename T>
int LinkedList<T>::lastIndexOf(const T & e){
	int index = this->size() - 1;
	for(std::reverse_iterator<_List_iterator<T> > it = innerList->rbegin(); it != innerList->rend(); it++){
		if(*it == e)
			break;
		index--;
	}
	return index;
}

template<typename T>
ListIterator<T>* LinkedList<T>::listIterator(){
	return new ListIterator<T>(innerList);
}

template<typename T>
ListIterator<T>* LinkedList<T>::listIterator(int index){
	if(index < 0 || index >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(index);
	}
	ListIterator<T>* it = new ListIterator<T>(innerList);
	for(int i = 0; i < index; i++){
		it->next();
	}
	return it;
}

template<typename T>
T LinkedList<T>::remove(int index, bool isIndex){
	if(index < 0 || index >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(index);
	}

	T ret;
	Iterator<T> *it = NULL;
	int i = 0;
	for(it = this->iterator(); it->hasNext();){
		T value = it->next();
		if(i == index){
			ret = value;
			it->remove();
			break;
		}
		i++;
	}
	delete it;
	return ret;
}

template<typename T>
T LinkedList<T>::set(int index, const T &element){
	if(index < 0 || index >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(index);
	}

	T ret;
	ListIterator<T> *it = NULL;
	int i = 0;
	for(it = this->listIterator(); it->hasNext();){
		T value = it->next();
		if(i == index){
			ret = value;
			it->set(element);
			break;
		}
		i++;
	}
	delete it;
	return ret;
}

template<typename T>
List<T>* LinkedList<T>::subList(int fromIndex, int toIndex){
	if(fromIndex < 0 || fromIndex >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(fromIndex);
	}
	if(toIndex < 0 || toIndex >= this->size()){
		throw cpp::lang::ArrayIndexOutOfBoundException(toIndex);
	}
	if(toIndex < fromIndex){
		std::ostringstream message("", ios::out);
		message << "toIndex (" << toIndex << ") cannot be lower than fromIndex (" << fromIndex << ").";
		throw cpp::lang::IndexOutOfBoundException(message.str());
	}

	LinkedList<T> *ret = new LinkedList<T>();

	Iterator<T> *it = this->iterator();

	int i = 0;
	for(; i < fromIndex; i++){
		it->next();
	}
	for(; i < toIndex; i++){
		T value = it->next();
		ret->add(value);
	}

	delete it;
	return ret;
}

}
}

#endif /* LINKEDLIST_H_ */
