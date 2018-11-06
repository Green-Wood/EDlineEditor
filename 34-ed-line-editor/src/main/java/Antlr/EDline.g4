grammar EDline;

 line : (location (',' location)?)? command1 para?
      | location? MARK
      ;
 location : CURRENT
          | LAST
          | INT
          | (MIN | ADD) INT
          | ALL
          | TOEND
          | SEARCHDOWN
          | SEARCHUP
          | CURRENT (MIN | ADD) INT
          | LAST (MIN | ADD) INT
          | INT (MIN | ADD) INT
          | LOWCASE
          ;

 command1 : COMMANDSET;

 para : COUNT
      | G
      | FILENAME
      ;

 CURRENT : '.';
 LAST : '$';
 MIN : '-';
 ADD : '+';
 ALL : ',';
 TOEND : ';';
 DOWN : '/';
 UP : '?';
 SEARCHUP : UP STR UP;
 SEARCHDOWN : DOWN STR DOWN;
 COUNT : DOWN STR DOWN STR DOWN 'count';
 G : DOWN STR DOWN STR DOWN 'g';
 COMMANDSET : [aicdp=zqQfwWjumts];
 STR : [a-zA-Z]+;
 INT : [0-9]+;
 ANYCHAR : . ;
 LOWCASE : '\'' [a-z];
 FILENAME : ' ' .+;
 MARK : 'k' [a-z];
 WS : [ \t\r\n]+ -> skip ; // Define whitespace rule, toss it out