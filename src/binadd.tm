states 16
initial
isOne
isZero
addZero
addOne
equalsOne 
equalsZero
carryOne
checkOne
checkZero
checkCarryOne
goBack
num1
finished
a +
r -
alphabet 7 1 0 # x s t z
initial 1 isOne s R
initial 0 isZero s R
initial # addZero z R
isOne 1 isOne 1 R 
isOne 0 isOne 0 R 
isOne # addOne # R
isZero 1 isZero 1 R
isZero 0 isZero 0 R
isZero # addZero # R 
addOne 1 equalsZero t R
addOne 0 equalsOne x R
addOne t carryOne x R
addOne # checkOne # R
addOne x addOne x R 
addZero t addOne x R
addZero 1 equalsOne x R 
addZero 0 equalsZero x R
addZero # checkZero # R 
addZero x addZero x R
carryOne 0 equalsZero t R 
carryOne 1 equalsOne t R 
equalsOne # checkOne # R 
equalsOne 1 equalsOne 1 R 
equalsOne 0 equalsOne 0 R 
equalsOne x equalsOne x R 
equalsZero # checkZero # R 
equalsZero 1 equalsZero 1 R 
equalsZero 0 equalsZero 0 R 
equalsZero x equalsZero x R 
equalsZero _ checkZero _ R
checkOne x checkOne x R 
checkOne 0 r 0 R 
checkOne 1 goBack x L
checkOne _ r _ R
checkZero x checkZero x R 
checkZero 1 r _ L
checkZero 0 goBack x L
checkZero _ finished _ L
goBack x goBack x L 
goBack 0 goBack 0 L
goBack 1 goBack 1 L
goBack # goBack # L
goBack t goBack t L
goBack z num1 z L
goBack s num1 s R
num1 1 isOne x R 
num1 0 isZero x R 
num1 s num1 s R 
num1 x num1 x R 
num1 # addZero # R
num1 z addZero z R
finished x finished x L 
finished 0 finished 0 L 
finished 1 r 1 L 
finished # finished # L
finished s a s L
finished z a z L
