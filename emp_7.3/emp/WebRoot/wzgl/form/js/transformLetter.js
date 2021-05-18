
//数字转换字母
function transLetter(num){
	var letter = "";
	if(num == "0"){
		letter = "A";
	}else if(num == "1"){
		letter = "B";
	}else if(num == "2"){
		letter = "C";
	}else if(num == "3"){
		letter = "D";
	}else if(num == "4"){
		letter = "E";
	}else if(num == "5"){
		letter = "F";
	}
	return letter;
}