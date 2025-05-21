package com.sonnenstahl.nukodu.utils

// canDisappear is solely because if it is the last number but it is wrong the button should
// not disappear. Only when the last
data class NumberButton (var number: Int, var canDisappear: Boolean)