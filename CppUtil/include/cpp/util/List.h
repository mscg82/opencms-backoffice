/*
 * List.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef LIST_H_
#define LIST_H_

#include "Collection.h"
#include "ListIterator.h"

namespace cpp{
namespace util{

template<typename T>
class List : public Collection<T>{
	virtual void add(int index, T &e) = 0;
	virtual bool addAll(int index, Collection<T> *c) = 0;
	virtual T get(int index) = 0;
	virtual T operator[](int index) = 0;
	virtual int lastIndexOf(const T &e) = 0;
	virtual ListIterator<T>* listIterator() = 0;
	virtual ListIterator<T>* listIterator(int index) = 0;
	virtual T remove(int index, bool isIndex) = 0;
	virtual T set(int index, const T &element) = 0;
	virtual List<T>* subList(int fromIndex, int toIndex) = 0;
};

}
}

#endif /* LIST_H_ */
