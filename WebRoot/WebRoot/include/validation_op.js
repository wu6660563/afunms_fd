// -----------------------------------------------------------------------------
// Generic Form Validation
//
// Copyright (C) 2000 Jacob Hage - [jacobhage@hotmail.com]
// Distributed under the terms of the GNU Library General Public License
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Initializing script  - setting global variables
// -----------------------------------------------------------------------------
var checkObjects		= new Array(); 	// Array containing the objects to validate.
var errors				= ""; 			// Variable holding the error message.
var returnVal			= false; 		// General return value. The validated form will only be submitted if true.
var language			= new Array(); 	// Language independent error messages!
var selectecLanguage	= "english";	// Choose between "english", "danish", "dutch", "french", "spanish", "russian", "portuguese"
language.english		= new Array();


// Error messages in english:
	language.english.header		= "·????????í?ó:"
	language.english.start		= "->";
	language.english.field		= " ???ò ";
	language.english.require	= " ±???????";
	language.english.min		= "????????°ü??";
	language.english.max		= "??×??ó???à??";
	language.english.minmax		= "????????";
	language.english.chars		= " ×?·?";
	language.english.num		= "??±???°ü????×?";
	language.english.email		= " ±????????????§???????·?·";


// -----------------------------------------------------------------------------
// define - Call this function in the beginning of the page. I.e. onLoad.
//
// n = name of the input field (Required)
// type= string, num, email (Required)
// min = the value must have at least [min] characters (Optional)
// max = the value must have maximum [max] characters (Optional)
// d = (Optional)
// -----------------------------------------------------------------------------
function define(n,type,HTMLname,min,max,d){
	var p;
	var i;
	var x;
	if(!d) d=document;
	if((p=n.indexOf("?"))>0&&parent.frames.length){
    	d=parent.frames[n.substring(p+1)].document;
    	n=n.substring(0,p);
    }
	if(!(x=d[n])&&d.all) x=d.all[n];
	
  	for (i=0;!x&&i<d.forms.length;i++){
  		x=d.forms[i][n];
  	}
	for(i=0;!x&&d.layers&&i<d.layers.length;i++){
		x=define(n,type,HTMLname,min,max,d.layers[i].document);
		return x;		
	}
	
	// Create Object. The name will be "V_something" where something is the "n" parameter above.
	eval("V_"+n+" = new formResult(x,type,HTMLname,min,max);");
	checkObjects[eval(checkObjects.length)] = eval("V_"+n);
}

// -----------------------------------------------------------------------------
// formResult - Used internally to create the objects
// -----------------------------------------------------------------------------
function formResult(form,type,HTMLname,min,max){
	this.form = form;
	this.type = type;
	this.HTMLname = HTMLname;
	this.min  = min;
	this.max  = max;
}

// -----------------------------------------------------------------------------
// validate - Call this function onSubmit and return the "returnVal". (onSubmit="validate();return returnVal;")
// -----------------------------------------------------------------------------
function validate(){
	var password = document.forms[0].passwords.value;
	var confirm = document.forms[0].confirm.value;
	if(password!=confirm){
		alert("输入密码不一致，请检查！");
		return;
	}
	
	if(checkObjects.length>0){
		errorObject = "";
		
		for(i=0;i<checkObjects.length;i++){

			validateObject 			= new Object();
			validateObject.form 	= checkObjects[i].form;
			validateObject.HTMLname = checkObjects[i].HTMLname;
			validateObject.val 		= checkObjects[i].form.value;
	alert("validateObject: "+validateObject+"\nvalidateObject.form: "+validateObject.form+"\nvalidateObject.HTMLname: "+validateObject.HTMLname+"\nvalidateObject.form.value,validateObject.max: "+validateObject.form.value+","+validateObject.max+"\nvalidateObject.type: "+validateObject.type);

			validateObject.len 		= checkObjects[i].form.value.length;
			validateObject.min 		= checkObjects[i].min;
			validateObject.max 		= checkObjects[i].max;
			validateObject.type 	= checkObjects[i].type;
			
			//Debug alert line
			//alert("validateObject: "+validateObject+"\nvalidateObject.val: "+validateObject.val+"\nvalidateObject.len: "+validateObject.len+"\nvalidateObject.min,validateObject.max: "+validateObject.min+","+validateObject.max+"\nvalidateObject.type: "+validateObject.type);
			
			// Checking input. If "min" and/or "max" is defined the input has to be within the specific range
			if(validateObject.type == "num" || validateObject.type == "string"){
				if((validateObject.type == "num" && validateObject.len <= 0) || (validateObject.type == "num" && isNaN(validateObject.val))){errors+=language[selectecLanguage].start+language[selectecLanguage].field+validateObject.HTMLname+language[selectecLanguage].require+language[selectecLanguage].num+"\n";
				} else if (validateObject.min && validateObject.max && (validateObject.len < validateObject.min || validateObject.len > validateObject.max)){errors+=language[selectecLanguage].start+language[selectecLanguage].field+validateObject.HTMLname+language[selectecLanguage].require+language[selectecLanguage].min+validateObject.min+"??"+language[selectecLanguage].minmax+"??"+validateObject.max+"??"+language[selectecLanguage].chars+"\n";
				} else if (validateObject.min && !validateObject.max && (validateObject.len < validateObject.min)){errors+=language[selectecLanguage].start+language[selectecLanguage].field+validateObject.HTMLname+language[selectecLanguage].require+language[selectecLanguage].min+"??"+validateObject.min+"??"+language[selectecLanguage].chars+"\n";
				} else if (validateObject.max && !validateObject.min &&(validateObject.len > validateObject.max)){errors+=language[selectecLanguage].start+language[selectecLanguage].field+validateObject.HTMLname+language[selectecLanguage].require+language[selectecLanguage].max+"??"+validateObject.max+"??"+language[selectecLanguage].chars+"\n";
				} else if (!validateObject.min && !validateObject.max && validateObject.len <= 0){errors+=language[selectecLanguage].start+language[selectecLanguage].field+validateObject.HTMLname+language[selectecLanguage].require+"\n";
				}
			} else if(validateObject.type == "email"){
				// Checking existense of "@" and ".". The length of the input must be at least 5 characters. The "." must neither be preceding the "@" nor follow it.
				if((validateObject.val.indexOf("@") == -1) || (validateObject.val.charAt(0) == ".") || (validateObject.val.charAt(0) == "@") ||(validateObject.len < 6) || (validateObject.val.indexOf(".") == -1) || (validateObject.val.charAt(validateObject.val.indexOf("@")+1) == ".") || (validateObject.val.charAt(validateObject.val.indexOf("@")-1) == ".")){errors+=language[selectecLanguage].start+language[selectecLanguage].field+validateObject.HTMLname+language[selectecLanguage].email+"\n";}
			}
		}
	}
	// Used to set the state of the returnVal. If errors -> show error messages in chosen language
	if(errors){
		alert(language[selectecLanguage].header.concat("\n"+errors));
		errors = "";
		returnVal = false;
	} else {
		returnVal = true;
	}
}

function pwVaild(pw,confirm){
	
}
