/*
 * StringQueueAppender.cpp
 *
 * Copyright 2000, LifeLine Networks BV (www.lifeline.nl). All rights reserved.
 * Copyright 2000, Bastiaan Bakker. All rights reserved.
 *
 * See the COPYING file for the terms of usage and distribution.
 */

#include <log4cpp/PortabilityImpl.hh>
#include <log4cpp/StringListAppender.hh>

namespace log4cpp {

	StringListAppender::StringListAppender(const std::string& name, int maxLines) :
        LayoutAppender(name), _maxLines(maxLines) {
    }
    
	StringListAppender::~StringListAppender() {
        close();
    }

    void StringListAppender::close() {
        for(std::map<Priority::Value, std::list<std::string>* >::iterator
        		it = _listForValues.begin(); it != _listForValues.end(); it++){
        	delete it->second;
        }
    }

    void StringListAppender::_append(const LoggingEvent& event) {
        std::string formattedMessage = _getLayout().format(event);

        _list.push_back(formattedMessage);
        if(_maxLines > 0 && (int)_list.size() > _maxLines){
        	_list.pop_front();
        }

        std::list<std::string> *listForValue = NULL;
        std::map<Priority::Value, std::list<std::string>* >::iterator it = _listForValues.find(event.priority);
        if(it == _listForValues.end()){
        	listForValue = new std::list<std::string>();
        	_listForValues[event.priority] = listForValue;
        }
        else{
        	listForValue = it->second;
        }
        listForValue->push_back(formattedMessage);
        if(_maxLines > 0 && (int)listForValue->size() > _maxLines){
        	listForValue->pop_front();
		}
    }

    bool StringListAppender::reopen() {
        return true;
    }      

    std::list<std::string>& StringListAppender::getList() {
        return _list;
    }

    std::list<std::string>& StringListAppender::getListForLevel(const Priority::PriorityLevel &level) {
    	std::list<std::string> *listForValue = NULL;
		std::map<Priority::Value, std::list<std::string>* >::iterator it = _listForValues.find(level);
		if(it == _listForValues.end()){
			listForValue = new std::list<std::string>();
			_listForValues[level] = listForValue;
		}
		else{
			listForValue = it->second;
		}
    	return *listForValue;
	}

    const std::list<std::string>& StringListAppender::getList() const {
        return _list;
    }

    const std::list<std::string>& StringListAppender::getListForLevel(const Priority::PriorityLevel &level) const {
    	std::list<std::string> *listForValue = NULL;
		std::map<Priority::Value, std::list<std::string>* >::const_iterator it = _listForValues.find(level);
		if(it == _listForValues.end()){
			listForValue = new std::list<std::string>();
		}
		else{
			listForValue = it->second;
		}
		return *listForValue;
	}

    size_t StringListAppender::listSize() const {
        return getList().size();
    }

}
