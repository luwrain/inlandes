// Copyright 2021 Michael Pozhidaev <msp@luwrain.org>

grammar Inlandes;

notation
    : ruleStatement* EOF
    ;

ruleStatement
    : ruleKeyword whereStatement? doStatement?
    ;

ruleKeyword
    : 'RULE' | 'Rule' | 'rule'
    ;

whereKeyword
    : 'WHERE' | 'Where' | 'where'
    ;

doKeyword
    : 'DO' | 'Do' | 'do'
    ;

cons
    : ConsCyril | ConsLatin
    ;



whereFixed
    : cons
    ;

whereAlternative
    : '(' whereItem+ ')'
    ;

whereBlock
    : '{' whereItem* '}' 
    ;

whereItem
    : whereFixed | whereAlternative Ref? | whereBlock Ref?
    ;

whereStatement
    : whereKeyword whereItem+
    ;

assignment
    : Ref '=' Js
    ;

operation
    : assignment ';'
    ;

doStatement
    : doKeyword operation+
    ;




ConsCyril
    : [а-яА-ЯёЁ]*
    ;

ConsLatin
    : '[a-zA-Z]*'
    ;

Num
    : [0-9]+
    ;

Punc
    : [.,?!:;$%@()_+=\-—–°£€/]
    ;

Ref
    : '_'[123456789]
    ;

Js
    : '``'.+'``'
    ;

WS
    : ' '
    -> skip
    ;
