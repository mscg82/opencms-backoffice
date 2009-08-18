/*
 * StringQueueAppender.cpp
 *
 * Copyright 2000, LifeLine Networks BV (www.lifeline.nl). All rights reserved.
 * Copyright 2000, Bastiaan Bakker. All rights reserved.
 *
 * See the COPYING file for the terms of usage and distribution.
 */

#include <log4cpp/PortabilityImpl.hh>
#include <log4cpp/StringQueueAppender.hh>

namespace log4cpp {

    StringQueueAppender::StringQueueAppender(const std::string& name, int maxLines) :
        LayoutAppender(name), _maxLines(maxLines) {
    }
    
    StringQueueAppender::~StringQueueAppender() {
        close();
    }

    void StringQueueAppender::close() {
        // empty
    }

    void StringQueueAppender::_append(const LoggingEvent& event) {
        _queue.push(_getLayout().format(event));
        if(_maxLines > 0 && _queue.size() > _maxLines){
        	popMessage();
        }
    }

    bool StringQueueAppender::reopen() {
        return true;
    }      

    std::queue<std::string>& StringQueueAppender::getQueue() {
        return _queue;
    }

    const std::queue<std::string>& StringQueueAppender::getQueue() const {
        return _queue;
    }

    size_t StringQueueAppender::queueSize() const {
        return getQueue().size();
    }

    std::string StringQueueAppender::popMessage() {        
        std::string message;

        if (!_queue.empty()) {
            message = _queue.front();
            _queue.pop();
        }

        return message;
    }
}
