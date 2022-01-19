// Copyright 2021 Michael Pozhidaev <msp@luwrain.org>

grammar Inlandes;

notation
    : ruleStatement* EOF
    ;

cons
    : ConsCyril | ConsLatin
    ;

ruleKeyword
    : 'RULE' | 'Rule' | 'rule'
    ;

ruleStatement
    : ruleKeyword whereStatement*
    ;

whereKeyword
    : 'WHERE' | 'Where' | 'where'
    ;

whereFixed
    : cons
    ;

whereAlternative
    : '(' whereItem ')'
    ;

whereBlock
    : '{' whereItem* '}' 
    ;

whereItem
    : whereFixed | whereAlternative | whereBlock Ref*
    ;

whereStatement
    : whereKeyword whereItem+
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
    : .[123456789]
    ;

WS
    : ' '
    -> skip
    ;
