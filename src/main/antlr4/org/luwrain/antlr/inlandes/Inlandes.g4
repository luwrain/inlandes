// Copyright 2021 Michael Pozhidaev <msp@luwrain.org>

grammar Inlandes;

notation
    : ruleStatement* EOF
    ;

ruleStatement
    : ruleKeyword stageStatement? whereStatement? doStatement?
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

stageKeyword
    : 'STAGE' | 'Stage' | 'stage'
    ;

whereFixed
    : CyrilPlain | Latin | Space | Punc | JsObj | Dict | Cl
    ;

whereAlternative
    : '(' whereItem+ ')'
    ;

whereBlock
    : '{' whereItem* '}' 
    ;

whereItem
    : whereFixed Ref? Optional? | whereAlternative Ref? Optional? | whereBlock Ref? Optional?
    ;

whereStatement
    : whereKeyword whereItem+
    ;

assignment
    : Ref '=' Str | Ref '=' Js
    ;

action
    : Js
    ;

operation
    : assignment ';' | action ';'
    ;

doStatement
    : doKeyword operation+
    ;

stageStatement
    : stageKeyword Num
    ;

CyrilPlain
    : [а-яА-ЯёЁ]+
    ;

Latin
    : '\''[a-zA-Z]+'\''
    ;

Num
    : '-'?[0-9]+
    ;

Punc
    : '\''[.,?!:;$%@()_+=\-—–°£€/]'\''
    ;

Space
    : '.'
    ;

Str
    : '"' ( '\\' [btnfr"'\\] | ~[\r\n\\"] )* '"'
    ;

JsObj
    : '@'[a-zA-Z][a-zA-Z0-9_]*
    ;

Cl
    : '\\'[a-z][a-z-]*
    ;

Dict
    : '#'[a-zA-Z][a-zA-Z0-9-]*
    ;

Ref
    : '_'[0123456789]
    ;

Optional
    : '?'
    ;

Js
    : '``' ( '\\' [btnfr`\\] | ~[\r\n\\`] )* '``'
    ;

WS : [\n\r\t ] -> skip ; 
