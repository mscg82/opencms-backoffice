/*
 * Collection.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef COLLECTION_H_
#define COLLECTION_H_

#include "Iterator.h"

namespace cpp{
namespace util{

template<typename T>
class Collection{
public:
	virtual bool add(const T &e) = 0;
	virtual bool addAll(Collection<T> *c) = 0;
	virtual void clear() = 0;
	virtual bool contains(const T &e) = 0;
	virtual bool containsAll(Collection<T> *c) = 0;
	virtual bool isEmpty() = 0;
	virtual Iterator<T>* iterator() = 0;
	virtual bool remove(const T &e) = 0;
	virtual bool removeAll(Collection<T> *c) = 0;
	virtual bool retainAll(Collection<T> *c) = 0;
	virtual int size() = 0;
	virtual T* toArray() = 0;
};

}
}

#endif /* COLLECTION_H_ */
