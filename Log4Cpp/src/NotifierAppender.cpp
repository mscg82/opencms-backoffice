/*
 * NotifierAppender.cpp
 *
 *  Created on: 18/ago/2009
 *  Author: Giuseppe Miscione
 */

#include <log4cpp/NotifierAppender.hh>

namespace log4cpp {

	NotifierAppender::NotifierAppender(const std::string& name, bool deleteListenersOnExit) :
		LayoutAppender(name), _deleteListenersOnExit(deleteListenersOnExit) {

	}

	NotifierAppender::~NotifierAppender() {
		if(_deleteListenersOnExit){
			for(std::list<LogEventListener*>::iterator it = _listeners.begin(); it != _listeners.end(); it++){
				delete (*it);
			}
		}
		close();
	}

	void NotifierAppender::registerListener(LogEventListener *listener){
		_listeners.push_back(listener);
	}

	bool NotifierAppender::unregisterListener(LogEventListener *listener, bool deleteListener){
		bool canRemove = false;
		for(std::list<LogEventListener*>::iterator it = _listeners.begin(); it != _listeners.end(); it++){
			if((*it) == listener){
				canRemove = true;
				_listeners.erase(it);
				break;
			}
		}

		if(canRemove && deleteListener){
			delete listener;
		}

		return canRemove;
	}

	bool NotifierAppender::reopen() {
		close();
		return true;
	}

	void NotifierAppender::close(){
		_listeners.clear();
	}

	void NotifierAppender::_append(const LoggingEvent& event) {
		std::string formattedMessage = _getLayout().format(event);

		for(std::list<LogEventListener*>::iterator it = _listeners.begin(); it != _listeners.end(); it++){
			(*it)->onLogMessage(event, formattedMessage);
		}
	}

}
