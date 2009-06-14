/**
 * The super class object.
 */
Object.prototype.superclass = Object.prototype;

/**
 * Calls the method of the super class specified by the
 * provided name.
 * 
 * @param functionName A string with the name of the method
 * of the super class that will be called.
 * 
 * @return The object returned by the provided method.
 */
Object.prototype.callSuperMethod = function(functionName){
	var ret = null;
	if( arguments.length > 1 ) {
		ret = this.superclass[functionName].apply( this, Array.prototype.slice.call( arguments, 1 ) );
	}
	else {
		ret = this.superclass[functionName].call( this );
	}
	return ret;
};

/**
 * Inerithance helper function.
 * 
 * @param parent The parent class of the current object.
 */
Object.prototype.Inherits = function( parent ) {
	if( arguments.length > 1 ) {
		parent.apply( this, Array.prototype.slice.call( arguments, 1 ) );
	}
	else {
		parent.call( this );
	}
	this.superclass = parent.prototype;
};