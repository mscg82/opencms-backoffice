Test2.prototype = new Test1();

function Test2(var1, var2, var3) {
	
	/* private */ this.var3 = null;
	
	this.Inherits(Test1, var1, var2);
	this.setVar3(var3);
};

Test2.prototype.getVar3 = function() {
	return this.var3;
};

Test2.prototype.setVar3 = function(var3) {
	this.var3 = var3;
};

Test2.prototype.stringIt = function() {
	var ret = '[' + this.callSuperMethod('stringIt', 1, 'asd') + ', var3: "' + this.getVar3() + '"]';
	return ret;
};

Test2.prototype.m1 = function() {
	alert('m1 in Test2');
	this.callSuperMethod('m1');
};

Test2.prototype.m2 = function() {
	alert('m2 in Test2');
	this.callSuperMethod('m2');
};