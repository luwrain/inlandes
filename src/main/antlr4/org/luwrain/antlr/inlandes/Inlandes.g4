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

whereItem
    : cons
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

WS
    : ' '
    -> skip
    ;
