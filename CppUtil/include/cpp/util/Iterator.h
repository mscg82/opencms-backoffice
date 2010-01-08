/*
 * Iterator.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef ITERATOR_H_
#define ITERATOR_H_

namespace cpp{
namespace util{

template<typename T>
class Iterator{
public:
	virtual bool hasNext() = 0;
	virtual T next() = 0;
	virtual void remove() = 0;
};

}
}
#endif /* ITERATOR_H_ */
