function Test1(var1, var2) {
	
	/* private */ this.var1 = null;
	/* private */ this.var2 = null;
	
	this.setVar1(var1);
	this.setVar2(var2);
}

Test1.prototype.getVar1 = function() {
	return this.var1;
};

Test1.prototype.getVar2 = function() {
	return this.var2;
};

Test1.prototype.setVar1 = function(var1) {
	this.var1 = var1;
};

Test1.prototype.setVar2 = function(var2) {
	this.var2 = var2;
};

Test1.prototype.stringIt = function() {
	var ret = '(var1: "' + this.getVar1() + '", var2: "' + this.getVar2() + '", [';
	for(var i = 0; i < arguments.length; i++){
		if(i != 0)
			ret += ', ';
		ret += arguments[i];
	}
	ret += '])';
	
	return ret;
};

Test1.prototype.m1 = function() {
	alert('m1 in Test1');
};

Test1.prototype.m2 = function() {
	alert('m2 in Test1');
	this.m1();
};