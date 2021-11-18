// Copyright 2021 Michael Pozhidaev <msp@luwrain.org>

grammar Inlandes;

notation
    : ruleStatement* EOF
    ;

ruleStatement
    : ruleWord
    ;

ruleWord
    : 'RULE' | 'Rule' | 'rule'
    ;

Cyril
    : [а-яА-ЯёЁ]+
    ;

Latin
    : [a-zA-Z]+
    ;

Num
    : [0-9]+
    ;

Punc
    : [.,?!:;$%@()_+=\-—–°£€/]
    ;

Space
    :   [ \t\r\n]+
    ;
